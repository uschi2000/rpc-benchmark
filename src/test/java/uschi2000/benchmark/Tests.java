package uschi2000.benchmark;

import com.google.common.collect.ImmutableList;
import com.palantir.remoting2.clients.CipherSuites;
import com.palantir.remoting2.config.service.ServiceConfiguration;
import com.palantir.remoting2.config.ssl.SslConfiguration;
import com.palantir.remoting2.config.ssl.SslSocketFactories;
import com.palantir.remoting2.ext.jackson.ObjectMappers;
import com.palantir.remoting2.jaxrs.JaxRsClient;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.TlsVersion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class Tests {

    private GrpcServer grpcServer;
    private GrpcClient grpcClient;
    private WitchcraftServer witchcraftServer;
    private WitchcraftServer.BenchmarkService jaxrsClient;
    private OkHttpClient okhttpClient;

    @Before
    public void before() throws IOException {
        grpcServer = new GrpcServer(50001);
        grpcServer.start();
        grpcClient = new GrpcClient("localhost", 50001, false);
        witchcraftServer = WitchcraftServer.start(50002);
        SslConfiguration sslConfig = SslConfiguration.of(Paths.get("var/security/truststore.jks"));
        jaxrsClient = JaxRsClient.create(
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

    @After
    public void after() throws InterruptedException {
        grpcServer.stop();
        grpcClient.shutdown();
        witchcraftServer.stop();
    }

    @Test
    public void testSanity() throws IOException {
        ImmutableBenchmarkData expected = ImmutableBenchmarkData.builder()
                .addStrings(Generators.PREFIX + "0", Generators.PREFIX + "1")
                .addInts(0, 1, 2, 3)
                .build();

        assertThat(grpcClient.query(2, 4)).isEqualTo(expected);
        assertThat(jaxrsClient.query(2, 4)).isEqualTo(expected);
        assertThat(execOkHttp(2, 4)).isEqualTo(expected);
    }

    private BenchmarkData execOkHttp(int numStrings, int numInts) throws IOException {
        String result = okhttpClient.newCall(new Request.Builder()
                .url(String.format("https://localhost:50002/api/query/%d/%d", numStrings, numInts))
                .get()
                .build()).execute().body().string();
        return ObjectMappers.newClientObjectMapper().readValue(result, BenchmarkData.class);
    }
}
