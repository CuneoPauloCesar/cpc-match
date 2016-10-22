package cpc.match.common;

import cpc.match.api.Path;

import java.util.List;

public class ListPath<T> implements Path<T>, Path.BuildPath<T> {

    public final int i;
    public final List<T> items;

    public ListPath(int i, List<T> items) {
        this.i = i;
        this.items = items;
    }

    public T first() {
        return items.get(i);
    }

    public BuildPath<T> rest() {
        if (i + 1 < items.size()) {
            return new ListPath<>(i + 1, items);
        }
        return EMPTY;
    }

}
