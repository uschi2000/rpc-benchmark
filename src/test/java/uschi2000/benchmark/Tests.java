package uschi2000.benchmark;

import com.palantir.remoting1.jaxrs.JaxRsClient;
import io.dropwizard.Configuration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class Tests {

    private GrpcServer grpcServer;
    private GrpcClient grpcClient;
    private DropwizardServer.BenchmarkService jaxrsClient;

    @ClassRule
    public static final DropwizardAppRule<Configuration> dropwizardServer =
            new DropwizardAppRule<>(DropwizardServer.class, "var/conf/server.yml");

    @Before
    public void before() throws IOException {
        grpcServer = new GrpcServer(50001);
        grpcServer.start();
        grpcClient = new GrpcClient("localhost", 50001, false);
        jaxrsClient = JaxRsClient.builder().build(
                DropwizardServer.BenchmarkService.class,
                "agent",
                "http://localhost:" + dropwizardServer.getLocalPort() + "/benchmark/api");
    }

    @After
    public void after() throws InterruptedException {
        grpcServer.stop();
        grpcClient.shutdown();
    }

    @Test
    public void testSanity() {
        ImmutableBenchmarkData expected = ImmutableBenchmarkData.builder()
                .addStrings(Generators.PREFIX + "0", Generators.PREFIX + "1")
                .addInts(0, 1, 2, 3)
                .build();

        assertThat(grpcClient.query(2, 4)).isEqualTo(expected);
        assertThat(jaxrsClient.query(2, 4)).isEqualTo(expected);
    }
}
