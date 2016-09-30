package uschi2000.benchmark;

import com.google.common.collect.*;

import java.util.List;
import java.util.Map;

public class Generators {

    public static final String PREFIX = "iuhgo oi fjihjfoiewfg78bveh2j g7 jk b78 brhjbl";
    public static final Map<Integer, List<Integer>> INTS = Maps.newHashMap();
    public static final Map<Integer, List<String>> STRINGS = Maps.newHashMap();

    static {
        range(21).stream()
                .map(i -> 1 << i)
                .forEach(i -> INTS.put(i, range(i)));
        range(21).stream()
                .map(i -> 1 << i)
                .forEach(i -> STRINGS.put(i, generateStrings(PREFIX, i)));
    }

    private static List<String> generateStrings(String prefix, int num) {
        return Lists.transform(range(num), i -> prefix + i);
    }

    private static List<Integer> range(int num) {
        return ImmutableList.copyOf(ContiguousSet.create(Range.closedOpen(0, num), DiscreteDomain.integers()));
    }
}
