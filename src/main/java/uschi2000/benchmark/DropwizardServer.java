package uschi2000.benchmark;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public final class DropwizardServer extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new DropwizardServer().run(args);
    }

    @Override
    public void run(Configuration config, Environment env) throws Exception {
        env.jersey().register(new BenchmarkResource());
    }

    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public interface BenchmarkService {

        @GET
        @Path("/query/{numStrings}/{numInts}")
        BenchmarkData query(
                @QueryParam("numStrings") int numStrings,
                @QueryParam("numInts") int numInts);
    }

    private static class BenchmarkResource implements BenchmarkService {

        @Override
        public BenchmarkData query(int numStrings, int numInts) {
            return ImmutableBenchmarkData.builder()
                    .addAllStrings(Generators.STRINGS.get(numStrings))
                    .addAllInts(Generators.INTS.get(numInts))
                    .build();
        }
    }
}
