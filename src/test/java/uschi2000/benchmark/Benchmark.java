package uschi2000.benchmark;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class Benchmark {

    private GrpcServer server;
    private GrpcClient client;

    @Before
    public void before() throws IOException {
        server = new GrpcServer(50001);
        server.start();
        client = new GrpcClient("localhost", 50001);
    }

    @After
    public void after() throws InterruptedException {
        server.stop();
        client.shutdown();
    }

    @Test
    public void testSanity() {
        assertThat(client.query(2, 3, "foo")).isEqualTo(
                ImmutableBenchmarkData.builder()
                        .addStrings("foo1", "foo2")
                        .addInts(1, 2, 3)
                        .build());
    }
}
