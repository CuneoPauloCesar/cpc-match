package cpc.match.builder;

import cpc.match.api.Path;
import cpc.match.common.KeyVal;
import cpc.match.api.Matcher;
import cpc.match.api.Trie;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;


/**
 * Will do O(n) insertion.
 */
public class Range<V>

        implements Trie.Builder<V> {
    public List<KeyVal<Comparable, Trie.Builder<V>>> ranges;

    public Range(List<KeyVal<Comparable, Trie.Builder<V>>> ranges) {
        this.ranges = ranges;
    }

    @Override
    public void insert(Path.BuildPath<Matcher> path, V value) {
        final Matcher first = path.first();
        final Path.BuildPath<Matcher> rest = path.rest();
        if (first instanceof cpc.match.builder.matchers.Range) {
            final cpc.match.builder.matchers.Range range = (cpc.match.builder.matchers.Range) first;
            insertRange(range, value, rest);
        }
    }

    @Override
    public Trie<V> build() {
        if (ranges.isEmpty()) {
            return Trie.EMPTY;
        }
        return new cpc.match.retrieve.Range<>(compressRanges());
    }

    @Override
    public Range<V> copy() {
        return new Range<>(copyRanges());
    }

    private List<KeyVal<Comparable, Trie.Builder<V>>> copyRanges() {
        final List<KeyVal<Comparable, Trie.Builder<V>>> result = new ArrayList<>();
        for (KeyVal<Comparable, Trie.Builder<V>> keyVal : ranges) {
            result.add(new KeyVal<>(keyVal.key, keyVal.val.copy()));
        }
        return result;
    }

    private List<KeyVal<Comparable, Trie<V>>> compressRanges() {
        final List<KeyVal<Comparable, Trie<V>>> result = new ArrayList<>();
        for (KeyVal<Comparable, Trie.Builder<V>> keyVal : ranges) {
            result.add(new KeyVal<>(keyVal.key, keyVal.val.build()));
        }
        return result;
    }


    private void insertRange(cpc.match.builder.matchers.Range range, V value, Path.BuildPath<Matcher> keys) {
        final List<KeyVal<Comparable, Trie.Builder<V>>> result = new ArrayList<>();
        final List<KeyVal<Comparable, Trie.Builder<V>>> lefts = this.ranges;
        final List<KeyVal<Comparable, V>> rights = asList(new KeyVal<>(range.from, null)
                , new KeyVal<>(range.to, value));
        int i = 0;
        int j = 0;
        for (; i < lefts.size() && j < rights.size(); ) {
            final KeyVal<Comparable, Trie.Builder<V>> left = lefts.get(i);
            final KeyVal<Comparable, V> right = rights.get(j);
            final Trie.Builder<V> vSubTrie = mergeSubtries(keys, left, right);
            final int leftCompareToRight = compare(left, right);
            if (leftCompareToRight == 0) {
                result.add(new KeyVal(left.key, vSubTrie));
                ++i;
                ++j;
            } else if (leftCompareToRight < 0) {
                result.add(new KeyVal(left.key, vSubTrie));
                ++i;
            } else {
                result.add(new KeyVal(right.key, vSubTrie));
                ++j;
            }
        }
        for (; i < lefts.size(); ++i) {
            final KeyVal<Comparable, Trie.Builder<V>> left = lefts.get(i);
            result.add(left);
        }
        for (; j < rights.size(); ++j) {
            final KeyVal<Comparable, V> right = rights.get(j);
            final Trie.Builder<V> vSubTrie = keys.builder();
            if (right.val != null) {
                vSubTrie.insert(keys, right.val);
            }
            result.add(new KeyVal<>(right.key, vSubTrie));
        }
        this.ranges = result;
    }

    private int compare(KeyVal<Comparable, Trie.Builder<V>> left, KeyVal<Comparable, V> right) {
        if (right.key == cpc.match.builder.matchers.Range.INF_NEG
                || right.key == cpc.match.builder.matchers.Range.INF_POS) {
            return -right.key.compareTo(left.key);
        }
        return left.key.compareTo(right.key);
    }

    private Trie.Builder<V> mergeSubtries(Path.BuildPath<Matcher> keys, KeyVal<Comparable, Trie.Builder<V>> left, KeyVal<Comparable, V> right) {
        final Trie.Builder<V> vSubTrie;
        if (right.val == null) {
            vSubTrie = left.val.copy();
        } else {
            vSubTrie = left.val.copy();
            vSubTrie.insert(keys, right.val);
        }
        return vSubTrie;
    }

    @Override
    public String toString() {
        return "@Range" + ranges.toString();
    }
}
