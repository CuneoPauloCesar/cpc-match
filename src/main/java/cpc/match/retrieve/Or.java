package cpc.match.retrieve;

import cpc.match.api.Path;
import cpc.match.api.Trie;

import java.util.List;
import java.util.function.Consumer;


public class Or<V> implements Trie<V> {
    public final Trie<V> one;
    public final Trie<V> two;

    public Or(Trie<V> one, Trie<V> two) {
        this.one = one;
        this.two = two;
    }

    @Override
    public void collectMatches(Path path, Consumer<List<V>> result) {
        one.collectMatches(path, result);
        two.collectMatches(path, result);
    }

    @Override
    public String toString() {
        return "[" + one + ", " + two + "]";
    }
}

