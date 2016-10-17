package cpc.match.retrieve;

import cpc.match.api.Path;
import cpc.match.api.Trie;

import java.util.List;
import java.util.function.Consumer;

public class Success<V> implements Trie<V> {
    public final List<V> values;

    public Success(List<V> values) {
        this.values = values;
    }

    @Override
    public void collectMatches(Path path, Consumer<List<V>> result) {
        result.accept(values);
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
