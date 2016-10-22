package cpc.match.common;

import cpc.match.api.Path;

public class ArrayPath<T> implements  Path<T>, Path.BuildPath<T> {

    public final int i;
    public final T[] items;

    public ArrayPath(int i, T[] items) {
        this.i = i;
        this.items = items;
    }

    public T first() {
        return items[i];
    }

    public BuildPath<T> rest() {
        if (i + 1 < items.length) {
            return new ArrayPath<>(i + 1, items);
        }
        return EMPTY;
    }
}
