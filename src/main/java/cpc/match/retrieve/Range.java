package cpc.match.retrieve;

import cpc.match.api.Path;
import cpc.match.common.KeyVal;
import cpc.match.api.Trie;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;


public class Range<V> implements Trie<V> {
    public final List<KeyVal<Trie<V>>> ranges;

    public Range(List<KeyVal<Trie<V>>> ranges) {
        this.ranges = ranges;
    }

    @Override
    public void collectMatches(Path path, Consumer<List<V>> result) {
        if (ranges.isEmpty()) {
            return;
        }
        final Object first = path.first();
        if (!(first instanceof Comparable)) {
            return;
        }

        final Path rest = path.rest();
        final Comparable comparableKey = (Comparable) first;
        int pos = insertionPos(comparableKey);

        if (pos >= ranges.size()) {
            return;
        }

        final KeyVal<Trie<V>> top = ranges.get(pos);
        if (top.key.compareTo(comparableKey) == 0) {
            // Because ranges are meant to be strict dis-equality
            return;
        }
        if (top.key.compareTo(first) < 0) {
            throw new RuntimeException("Range compute bound error.");
        }
        top.val.collectMatches(rest, result);
    }

    private int insertionPos(Comparable comparableKey) {
        int pos = Collections.binarySearch(ranges,
                new KeyVal(comparableKey, null)
                , (c1, c2) -> c1.key.compareTo(c2.key));
        if (pos < 0) {
            pos = -(pos + 1);
        }
        return pos;
    }

    @Override
    public String toString() {
        return "@Range" +ranges.toString();
    }
}
