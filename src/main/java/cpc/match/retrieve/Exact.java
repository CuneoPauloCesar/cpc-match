package cpc.match.retrieve;

import cpc.match.api.Path;
import cpc.match.builder.matchers.Nor;
import cpc.match.api.Trie;
import cpc.match.api.Matcher;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class Exact<V> implements Trie<V> {
    public final Map<Object, Trie<V>> branches;

    public Exact(Map<Object, Trie<V>> branches) {
        this.branches = branches;
    }

    @Override
    public void collectMatches(Path path, Consumer<List<V>> result) {
        final Object first = path.first();
        final Path rest = path.rest();
        final Trie<V> subTree = branches.get(first);
        if (subTree != null) {
            subTree.collectMatches(rest, result);
        } else {
            final Trie<V> notTrie = branches.get(Nor.NOT);
            if (notTrie != null) {
                notTrie.collectMatches(rest, result);
            }
        }
        final Trie<V> anyTree = branches.get(Matcher.ANY);
        if (anyTree != null) {
            anyTree.collectMatches(rest, result);
        }
    }

    @Override
    public String toString() {
        return "@Exact" + branches;
    }
}
