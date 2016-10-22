package cpc.match.api;


import cpc.match.builder.Exact;
import cpc.match.builder.Range;
import cpc.match.builder.RangeOrExact;
import cpc.match.common.ArrayPath;
import cpc.match.common.Empty;

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

    BuildPath EMPTY = new Empty();

    static <T> Path<T> cons(T... items) {
        if (items == null || items.length == 0) {
            return Path.EMPTY;
        }
        return new ArrayPath<>(0, items);
    }

    interface BuildPath<T> extends Path<T> {


        BuildPath<T> rest();

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
    }


}
