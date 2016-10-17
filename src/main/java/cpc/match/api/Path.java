package cpc.match.api;


import cpc.match.builder.Exact;
import cpc.match.builder.Range;
import cpc.match.builder.RangeOrExact;
import cpc.match.builder.Success;

import java.util.ArrayList;
import java.util.HashMap;

public interface Path<T> {

    /**
     * @return the first element of the path.
     */
    T first();

    /**
     * @return the rest elements in the path.
     */
    Path<T> rest();

    /**
     * This will be called when a trie needs
     * to create a sub-trie for the rest of the path.
     *
     * @return a trie builder compatible with this, path.
     */
    default Trie.Builder builder() {
        return new RangeOrExact<>(new Exact<>(new HashMap<>())
                , new Range<>(new ArrayList<>()));
    }

    Path EMPTY = new Path() {

        @Override
        public Object first() {
            return null;
        }

        @Override
        public Path rest() {
            return this;
        }

        @Override
        public Trie.Builder builder() {
            return new Success<>(new ArrayList<>());
        }
    };

    static <T> Path<T> cons(java.util.List<T> items) {
        if (items == null || items.isEmpty()) {
            return EMPTY;
        }
        return new List<>(0, items);
    }

    static <T> Path<T> cons(T... items) {
        if (items == null || items.length == 0) {
            return EMPTY;
        }
        return new Array<>(0, items);
    }

    class List<T> implements Path<T> {

        public final int i;
        public final java.util.List<T> items;

        public List(int i, java.util.List items) {
            this.i = i;
            this.items = items;
        }

        public T first() {
            return items.get(i);
        }

        public Path<T> rest() {
            if (i + 1 < items.size()) {
                return new List<>(i + 1, items);
            }
            return EMPTY;
        }

    }

    class Array<T> implements Path<T> {

        public final int i;
        public final T[] items;

        public Array(int i, T[] items) {
            this.i = i;
            this.items = items;
        }

        public T first() {
            return items[i];
        }

        public Path<T> rest() {
            if (i + 1 < items.length) {
                return new Array<>(i + 1, items);
            }
            return EMPTY;
        }
    }
}
