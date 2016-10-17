package cpc.match.api;

import cpc.match.builder.matchers.Nor;
import cpc.match.builder.matchers.Or;
import cpc.match.builder.matchers.Range;

import static java.util.Arrays.asList;

public interface Matcher {

    /**
     * THE matcher that matches anything.
     */
    Matcher ANY = new Matcher() {
        @Override
        public String toString() {
            return "ANY";
        }
    };

    /**
     * @param a list of alternatives to match by equality.
     * @return a matcher that matches any of the given items.
     */
    static Matcher or(Object... a) {
        return new Or(asList(a));
    }

    /**
     * @param a value to match by equality.
     * @return a matcher that matches the value given.
     * <p>
     * You may want to @See Matcher.or
     */
    static Matcher eq(Object a) {
        return or(a);
    }

    /**
     * @param a list of values that wont match.
     * @return a matcher that wont match any of the given items.
     */
    static Matcher nor(Object... a) {
        return new Nor(asList(a));
    }

    /**
     * @param from strict lower bound of range. WONT match equality.
     * @param to   strict higher bound of range. WONT match equality.
     * @return a matcher that will match comparable values to the between the given to and from.
     * Only works as strict comparison.
     */
    static Matcher rg(Comparable from, Comparable to) {
        if ((to == Range.INF_NEG)
                || (from == Range.INF_POS)
                || !(to == Range.INF_POS || from.compareTo(to) <= 0)) {
            throw new IllegalArgumentException(
                    "Range: 'from: " + from + "' argument must be strictly less than 'to: " + to + "' argument.");
        }
        return new Range(from, to);
    }

    /**
     * @param to strict higher bound of range. WONT match equality.
     * @return a matcher that will match comparable values to the between the given to and from.
     * Only works as strict comparison.
     */
    static Matcher lt(Comparable to) {
        return rg(Range.INF_NEG, to);
    }

    /**
     * @param from strict lower bound of range. WONT match equality.
     * @return a matcher that will match comparable values to the between the given to and from.
     * Only works as strict comparison.
     */
    static Matcher gt(Comparable from) {
        return rg(from, Range.INF_POS);
    }

}
