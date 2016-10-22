package cpc.match.common;

import cpc.match.api.Path;
import cpc.match.api.Trie;
import cpc.match.builder.Success;

import java.util.ArrayList;

public class Empty<T> implements  Path<T>, Path.BuildPath<T> {

    @Override
    public T first() {
        return null;
    }

    @Override
    public BuildPath<T> rest() {
        return this;
    }

    @Override
    public Trie.Builder builder() {
        return new Success<>(new ArrayList<>());
    }
}
