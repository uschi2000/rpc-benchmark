package uschi2000.benchmark;

import com.palantir.remoting1.clients.ClientConfig;
import com.palantir.remoting1.config.ssl.SslConfiguration;
import com.palantir.remoting1.config.ssl.SslSocketFactories;
import com.palantir.remoting1.jaxrs.JaxRsClient;
import io.dropwizard.Configuration;
import io.dropwizard.testing.DropwizardTestSupport;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 6, time = 3)
@Measurement(iterations = 6, time = 3)
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
public class Benchmarks {

    @State(Scope.Thread)
    public static class Services {
        GrpcServer grpcServer;
        DropwizardTestSupport<Configuration> dropwizardServer;
        DropwizardServer.BenchmarkService jaxrsHttpClient;
        DropwizardServer.BenchmarkService jaxrsHttpsClient;

        @Setup
        public void setup() throws IOException {
            grpcServer = new GrpcServer(50001);
            grpcServer.start();
            dropwizardServer = new DropwizardTestSupport<>(DropwizardServer.class, "var/conf/server.yml");
            dropwizardServer.before();
            jaxrsHttpClient = JaxRsClient.builder().build(
                    DropwizardServer.BenchmarkService.class,
                    "agent",
                    "http://localhost:51001/benchmark/api");
            ClientConfig httpsConfig = ClientConfig.builder()
                    .trustContext(SslSocketFactories.createTrustContext(
                            SslConfiguration.of(Paths.get("var/security/truststore.jks"))))
                    .build();
            jaxrsHttpsClient = JaxRsClient.builder(httpsConfig).build(
                    DropwizardServer.BenchmarkService.class,
                    "agent",
                    "https://localhost:51002/benchmark/api");
        }

        @TearDown
        public void after() throws InterruptedException {
            grpcServer.stop();
            dropwizardServer.after();
        }
    }

    @State(Scope.Benchmark)
    public static class Data {
        GrpcClient grpcClient;

        @Param({"1", "32", "1024", "32768", "1048576"})
        int scale;

        // TODO(rfink) test with TLS
        @Param({"false"})
        boolean useSsl;

        int numStrings;
        int numInts;

        @Setup
        public void setup() {
            numStrings = scale;
            numInts = scale;
            grpcClient = new GrpcClient("localhost", 50001, useSsl);
        }

        @TearDown
        public void after() throws InterruptedException {
            grpcClient.shutdown();
        }
    }

    @Benchmark
    public void benchmarkGrpc(Services services, Data data) {
        data.grpcClient.query(data.numStrings, data.numInts);
    }

    @Benchmark
    public void benchmarkDropwizardJaxRs(Services services, Data data) {
        DropwizardServer.BenchmarkService service = data.useSsl ? services.jaxrsHttpsClient : services.jaxrsHttpClient;
        service.query(data.numStrings, data.numInts);
    }
}
