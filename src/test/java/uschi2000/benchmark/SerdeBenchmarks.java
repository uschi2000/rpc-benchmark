package uschi2000.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 6, time = 3)
@Measurement(iterations = 6, time = 3)
@BenchmarkMode(Mode.AverageTime)
@Fork(1)
public class SerdeBenchmarks {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Gson GSON = new Gson();

    @State(Scope.Benchmark)
    public static class Data {
        @Param({"1", "32", "1024", "32768", "1048576"})
        int scale;

        int numStrings;
        int numInts;

        @Setup
        public void setup() {
            numStrings = scale;
            numInts = scale;
        }
    }

    @Benchmark
    public void benchmarkProtobuf(Data data) {
        BenchmarkReply reply = BenchmarkReply.newBuilder()
                .addAllStrings(Generators.STRINGS.get(data.numStrings))
                .addAllInts(Generators.INTS.get(data.numInts))
                .build();
        assertThat(reply.toByteArray()).isNotNull();
    }

    @Benchmark
    public void benchmarkJackson(Data data) throws JsonProcessingException {
        BenchmarkData reply = ImmutableBenchmarkData.builder()
                .addAllStrings(Generators.STRINGS.get(data.numStrings))
                .addAllInts(Generators.INTS.get(data.numInts))
                .build();
        assertThat(MAPPER.writeValueAsString(reply)).isNotNull();
    }

    @Benchmark
    public void benchmarkGson(Data data) {
        BenchmarkData reply = ImmutableBenchmarkData.builder()
                .addAllStrings(Generators.STRINGS.get(data.numStrings))
                .addAllInts(Generators.INTS.get(data.numInts))
                .build();
        assertThat(GSON.toJson(reply)).isNotNull();
    }
}
