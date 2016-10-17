package cpc.match.builder;

import cpc.match.api.Path;
import cpc.match.api.Matcher;
import cpc.match.api.Trie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Success<V> implements Trie.Builder<V> {
    public final List<V> values;

    public Success(List<V> values) {
        this.values = values;
    }

    @Override
    public void insert(Path<Matcher> path, V value) {
        values.add(value);
    }

    @Override
    public Trie<V> build() {
        if (values == null || values.isEmpty()) {
            return Trie.EMPTY;
        }
        return new cpc.match.retrieve.Success(Collections.unmodifiableList(values));
    }

    @Override
    public Success<V> copy() {
        return new Success<>(new ArrayList<>(values));
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
