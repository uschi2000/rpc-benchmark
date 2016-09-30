package uschi2000.benchmark;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableBenchmarkData.class)
@JsonDeserialize(as = ImmutableBenchmarkData.class)
public interface BenchmarkData {
    List<String> strings();

    List<Integer> ints();
}
