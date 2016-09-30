package uschi2000.benchmark;

import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 5)
@Fork(1)
public class Benchmarks {

    @State(Scope.Thread)
    public static class Services {
        GrpcServer server;

        @Setup
        public void setup() throws IOException {
            server = new GrpcServer(50001);
            server.start();
        }

        @TearDown
        public void after() throws InterruptedException {
            server.stop();
        }
    }

    @State(Scope.Benchmark)
    public static class Data {
        GrpcClient client;

        @Param({"1", "32", "1024", "32768", "1048576"})
        int scale;

        @Param({"false"})  // TODO(rfink) test with TLS
        boolean useSsl;

        int numStrings;
        int numInts;

        @Setup
        public void setup() {
            numStrings = scale;
            numInts = scale;
            client = new GrpcClient("localhost", 50001, useSsl);
        }

        @TearDown
        public void after() throws InterruptedException {
            client.shutdown();
        }
    }

    @Benchmark
    @Measurement(iterations = 1, time = 1)
    public void testGrpc(Services services, Data data) {
        data.client.query(data.numStrings, data.numInts);
    }
}
