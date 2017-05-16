package uschi2000.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.palantir.remoting2.clients.CipherSuites;
import com.palantir.remoting2.config.service.ServiceConfiguration;
import com.palantir.remoting2.config.ssl.SslConfiguration;
import com.palantir.remoting2.config.ssl.SslSocketFactories;
import com.palantir.remoting2.ext.jackson.ObjectMappers;
import com.palantir.remoting2.http2.Http2Agent;
import com.palantir.remoting2.jaxrs.JaxRsClient;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.TlsVersion;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 6, time = 3)
@Measurement(iterations = 6, time = 3)
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
public class RpcBenchmarks {
    private static final ObjectMapper mapper = ObjectMappers.newClientObjectMapper();

    @State(Scope.Thread)
    public static class Services {
        GrpcServer grpcServer;
        WitchcraftServer witchcraftServer;
        WitchcraftServer.BenchmarkService jaxssClient;
        OkHttpClient okhttpClient;

        @Setup
        public void setup() throws IOException {
            Http2Agent.install();
            grpcServer = new GrpcServer(50001);
            grpcServer.start();
            witchcraftServer = WitchcraftServer.start(50002);
            SslConfiguration sslConfig = SslConfiguration.of(Paths.get("var/security/truststore.jks"));
            jaxssClient = JaxRsClient.create(
                    WitchcraftServer.BenchmarkService.class,
                    "agent",
                    ServiceConfiguration.builder()
                            .addUris("https://localhost:50002/api")
                            .security(Optional.of(sslConfig))
                            .enableGcmCipherSuites(true)
                            .build());
            okhttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(SslSocketFactories.createSslSocketFactory(sslConfig),
                            SslSocketFactories.createX509TrustManager(sslConfig))
                    .connectionSpecs(createConnectionSpecs(true))
                    .build();
        }

        private static ImmutableList<ConnectionSpec> createConnectionSpecs(boolean enableGcmCipherSuites) {
            return ImmutableList.of(
                    new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                            .tlsVersions(TlsVersion.TLS_1_2)
                            .cipherSuites(enableGcmCipherSuites
                                    ? CipherSuites.allCipherSuites()
                                    : CipherSuites.fastCipherSuites())
                            .build(),
                    ConnectionSpec.CLEARTEXT);
        }

        @TearDown
        public void after() throws InterruptedException {
            grpcServer.stop();
            witchcraftServer.stop();
        }
    }

    @State(Scope.Benchmark)
    public static class Data {
        GrpcClient grpcClient;

        @Param({"1", "1024", "1048576"})
//        @Param({"1", "32", "1024", "32768", "1048576"})
                int scale;

        int numStrings;
        int numInts;

        @Setup
        public void setup() {
            numStrings = scale;
            numInts = scale;
            grpcClient = new GrpcClient("localhost", 50001, false);
        }

        @TearDown
        public void after() throws InterruptedException {
            grpcClient.shutdown();
        }
    }

    //    @Benchmark
    public void benchmarkGrpc(Services services, Data data) {
        data.grpcClient.query(data.numStrings, data.numInts);
    }

    @Benchmark
    public void benchmarkDropwizardJaxRs(Services services, Data data) {
        assertThat(services.jaxssClient.query(data.numStrings, data.numInts).ints()).hasSize(data.numInts);
    }

    @Benchmark
    public void benchmarkOkHttp(Services services, Data data) throws IOException {
        assertThat(execOkHttp(services.okhttpClient, data.numStrings, data.numInts).ints()).hasSize(data.numInts);
    }

    private BenchmarkData execOkHttp(OkHttpClient client, int numStrings, int numInts) throws IOException {
        String result = client.newCall(new Request.Builder()
                .url(String.format("https://localhost:50002/api/query/%d/%d", numStrings, numInts))
                .get()
                .build()).execute().body().string();
        return mapper.readValue(result, BenchmarkData.class);
    }
}
