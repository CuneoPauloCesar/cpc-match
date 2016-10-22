package cpc.match.builder;

import cpc.match.api.Path;
import cpc.match.api.Trie;
import cpc.match.retrieve.Or;

import static cpc.match.api.Trie.EMPTY;

import cpc.match.api.Matcher;


public class RangeOrExact<V>
        implements Trie.Builder<V> {
    public Exact<V> branches;
    public Range<V> ranges;

    public RangeOrExact(Exact<V> branches, Range<V> ranges) {
        this.branches = branches;
        this.ranges = ranges;
    }

    @Override
    public void insert(Path.BuildPath<Matcher> path, V value) {
        branches.insert(path, value);
        ranges.insert(path, value);
    }

    @Override
    public Trie<V> build() {
        final Trie<V> rangeCompressed = ranges.build();
        final Trie<V> exactCompressed = branches.build();
        if (rangeCompressed == EMPTY &&
                exactCompressed == EMPTY) {
            return EMPTY;
        } else if (rangeCompressed == EMPTY) {
            return exactCompressed;
        } else if (exactCompressed == EMPTY) {
            return rangeCompressed;
        }
        return new Or<>(exactCompressed
                , rangeCompressed);
    }

    @Override
    public RangeOrExact<V> copy() {
        return new RangeOrExact<>(branches.copy(), ranges.copy());
    }

}
