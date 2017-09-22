package uschi2000.benchmark;

import com.palantir.status.health.HealthCheckSharedSecret;
import com.palantir.witchcraft.Witchcraft;
import com.palantir.witchcraft.config.HealthChecksConfiguration;
import com.palantir.witchcraft.config.InstallConfiguration;
import com.palantir.witchcraft.config.RuntimeConfiguration;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public final class WitchcraftServer {

    private final Witchcraft witchcraft;

    private WitchcraftServer(Witchcraft witchcraft) {
        this.witchcraft = witchcraft;
    }

    public static WitchcraftServer start(int port) {
        return new WitchcraftServer(Witchcraft.with(
                InstallConfiguration.builder()
                        .port(port)
                        .useConsoleLog(true)
                        .build(),
                RuntimeConfiguration.builder().healthChecks(HealthChecksConfiguration.builder().sharedSecret(HealthCheckSharedSecret.valueOf("a")).build()).build())
                .api(new BenchmarkResource())
                .start());
    }

    public void stop() {
        witchcraft.stop();
    }

    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public interface BenchmarkService {

        @GET
        @Path("/query/{numStrings}/{numInts}")
        BenchmarkData query(
                @PathParam("numStrings") int numStrings,
                @PathParam("numInts") int numInts);
    }

    private static class BenchmarkResource implements BenchmarkService {

        @Override
        public BenchmarkData query(int numStrings, int numInts) {
            return ImmutableBenchmarkData.builder()
                    .authHeader("my-auth-header")
                    .addAllStrings(Generators.STRINGS.get(numStrings))
                    .addAllInts(Generators.INTS.get(numInts))
                    .build();
        }
    }
}
