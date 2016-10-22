package cpc.match.builder;

import cpc.match.api.Path.BuildPath;
import cpc.match.api.Trie;
import cpc.match.builder.matchers.Nor;
import cpc.match.builder.matchers.Or;
import cpc.match.api.Matcher;
import cpc.match.api.Path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class Exact<V>
        implements Trie.Builder<V> {
    public Map<Object, Trie.Builder<V>> branches;

    public Exact(Map<Object, Trie.Builder<V>> branches) {
        this.branches = branches;
    }

    @Override
    public void insert(BuildPath<Matcher> path, V value) {
        final Matcher first = path.first();
        final BuildPath<Matcher> rest = path.rest();
        if (Matcher.ANY.equals(first)) {
            insertFirst(value, Matcher.ANY, rest);
        } else if (first instanceof Or) {
            insertIn(value, (Or) first, rest);
        } else if (first instanceof Nor) {
            insertNotIn(value, (Nor) first, rest);
        }
    }

    private void insertFirst(V value, Matcher first, BuildPath<Matcher> rest) {
        final Trie.Builder<V> subTrie = getSubTrie(first, rest);
        subTrie.insert(rest, value);
    }

    private void insertIn(V value, Or first, BuildPath<Matcher> rest) {
        for (Object key : first.a) {
            final Trie.Builder<V> subTrie = getSubTrie(key, rest);
            subTrie.insert(rest, value);
        }
    }

    private void insertNotIn(V value, Nor first, BuildPath<Matcher> rest) {
        final Trie.Builder<V> notTrie = getSubTrie(Nor.NOT, rest);
        for (Object key : first.a) {
            if (!branches.containsKey(key)) {
                branches.put(key, notTrie.copy());
            }
            for (Map.Entry<Object, Trie.Builder<V>> entry : branches.entrySet()) {
                if (!Matcher.ANY.equals(entry.getKey())
                        && !key.equals(entry.getKey())) {
                    entry.getValue().insert(rest, value);
                }
            }
        }
    }

    private Trie.Builder<V> getSubTrie(Object key, BuildPath<Matcher> keys) {
        Trie.Builder<V> subTrie = branches.get(key);
        if (subTrie == null) {
            subTrie = keys.builder();
            branches.put(key, subTrie);
        }
        return subTrie;
    }

    @Override
    public Trie<V> build() {
        if (branches.isEmpty()) {
            return Trie.EMPTY;
        }
        final Trie.Builder<V> anyTrie = branches.get(Matcher.ANY);
        if (branches.size() == 1
                && anyTrie != null) {
            final Trie anyCompress = anyTrie.build();
            return new Trie<V>() {
                @Override
                public void collectMatches(Path path, Consumer<List<V>> result) {
                    anyCompress.collectMatches(path.rest(), result);
                }
            };
        }

        final Map<Object, Trie<V>> subtrees = compressBranches();
        return new cpc.match.retrieve.Exact<>(subtrees);
    }

    @Override
    public Exact<V> copy() {
        final Map<Object, Trie.Builder<V>> subtrees = copyBranches();
        return new Exact<>(subtrees);
    }

    private Map<Object, Trie.Builder<V>> copyBranches() {
        final Map<Object, Trie.Builder<V>> result = new HashMap<>();
        for (Map.Entry<Object, Trie.Builder<V>> e : branches.entrySet()) {
            result.put(e.getKey(), e.getValue().copy());
        }
        return result;
    }

    private Map<Object, Trie<V>> compressBranches() {
        final Map<Object, Trie<V>> result = new HashMap<>();
        for (Map.Entry<Object, Trie.Builder<V>> e : branches.entrySet()) {
            result.put(e.getKey(), e.getValue().build());
        }
        return result;
    }
}
