package cpc.match.builder.matchers;

import cpc.match.api.Matcher;

/**
 * Created by paulo on 16/10/16.
 */
public class Range implements Matcher {

    public static final Comparable INF_POS = new Comparable() {
        @Override
        public int compareTo(Object o) {
            return (o == this) ? 0 : 1;
        }

        @Override
        public String toString() {
            return "+Inf";
        }
    };

    public static final Comparable INF_NEG = new Comparable() {
        @Override
        public int compareTo(Object o) {
            return (o == this) ? 0 : -1;
        }

        @Override
        public String toString() {
            return "-Inf";
        }
    };
    public final Comparable from;
    public final Comparable to;

    public Range(Comparable from, Comparable to) {
        this.from = from;
        this.to = to;
    }
}
