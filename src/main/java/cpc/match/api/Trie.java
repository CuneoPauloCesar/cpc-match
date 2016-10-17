package cpc.match.api;

import cpc.match.builder.Exact;
import cpc.match.builder.Range;
import cpc.match.builder.RangeOrExact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;


public interface Trie<V> {

    /**
     * For the given trie, traverse the trie using the @path
     * and returns the list of matching results.
     *
     * @param path path to match.
     * @return items matching the path.
     */
    default List<V> match(Object... path) {
        List<V> result = new ArrayList<>();
        collectMatches(Path.cons(path), (v) -> result.addAll(v));
        return result;
    }

    /**
     * For the given trie, traverse the trie using the @path
     * and send to @link Collector.accept the matching results
     * as the are found. These means Collector.accept
     * will be called many times. Each with possibly different items.
     *
     * @param path   path to match.
     * @param result matching values acceptor.
     */
    void collectMatches(Path path, Consumer<List<V>> result);


    /**
     * Used internally.
     * Many be remove form here.
     */
    Trie EMPTY = (keys, result) -> {
    };

    interface Builder<V> {
        /**
         * For the given @param path describing a path, insert the value in that path.
         *
         * @param path  path descriptors
         * @param value value to be retrieve by path.
         */
        void insert(Path<Matcher> path, V value);

        /**
         * Convinience method. Will convert no mather args into Matcher.eq.
         *
         * @param path  descriptors to match a path.
         * @param value value to be retrieve by path.
         */
        default void insert(V value, Object... path) {
            final List<Matcher> ms = new ArrayList<>();
            for (Object o : path) {
                if (o instanceof Matcher) {
                    ms.add((Matcher) o);
                } else {
                    ms.add(Matcher.eq(o));
                }
            }
            insert(Path.cons(ms), value);
        }

        /**
         * @return a copy of the given builder.
         */
        Builder<V> copy();

        /**
         * @return a trie for the builder a this point in time.
         */
        Trie<V> build();
    }


    static <V> Trie.Builder<V> builder() {
        return new RangeOrExact<>(new Exact<>(new HashMap<>())
                , new Range<>(new ArrayList<>()));
    }

}
