package uschi2000.benchmark;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

import java.util.stream.Collectors;

public class Generators {

    public static Iterable<Integer> generateInts(int num) {
        return ContiguousSet.create(Range.closed(1, num), DiscreteDomain.integers());
    }

    public static Iterable<String> generateStrings(String prefix, int num) {
        return ContiguousSet.create(Range.closed(1, num), DiscreteDomain.integers())
                .stream()
                .map(i -> prefix + i)
                .collect(Collectors.toList());
    }
}
