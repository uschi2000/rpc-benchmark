package uschi2000.benchmark;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface BenchmarkData {
    List<String> strings();

    List<Integer> ints();
}
