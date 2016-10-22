package cpc.match.api;

import org.junit.jupiter.api.Test;

import static cpc.match.api.Matcher.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RangeTest {
    @Test
    public void singleRange() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(0, rg(-1, 1));
        Trie<Object> trie = b.build();
        assertThat(trie.match(0), hasItems(0));
        assertThat(trie.match(-1), is(asList()));
        assertThat(trie.match(1), is(asList()));
    }

    @Test
    public void lessThanRange() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(0, lt(1));
        Trie<Object> trie = b.build();
        assertThat(trie.match(0), hasItems(0));
    }

    @Test
    public void greatherThanRange() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(0, gt(-1));
        Trie<Object> trie = b.build();
        assertThat(trie.match(0), hasItems(0));
    }


    @Test
    public void overlapGtRanges() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(0, rg(0, 10));
        b.insert(1, gt(1));
        b.insert(2, rg(3, 10));
        Trie<Object> trie = b.build();
        assertThat(trie.match(5), hasItems(0, 1, 2));
    }


    @Test
    public void overlapGtLtRanges() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(-1, gt(-1));
        b.insert(0, rg(0, 10));
        b.insert(1, rg(2, 8));
        b.insert(2, rg(4, 6));
        b.insert(3, lt(20));
        Trie<Object> trie = b.build();
        assertThat(trie.match(5), hasItems(0, 1, 2, -1, 3));
    }

    @Test
    public void overlapRanges() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(0, rg(0, 10));
        b.insert(1, rg(2, 8));
        b.insert(2, rg(4, 6));
        Trie<Object> trie = b.build();
        assertThat(trie.match(5), hasItems(0, 1, 2));
    }

    @Test
    public void overlapInsertionOrderGTFirstRanges() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(2, rg(4, 6));
        b.insert(1, rg(2, 8));
        b.insert(0, rg(0, 10));
        Trie<Object> trie = b.build();
        assertThat(trie.match(5), hasItems(0, 1, 2));
    }


    @Test
    public void nonOverlapInsertOrderGTFirstRanges() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(2, rg(6, 10));
        b.insert(1, rg(2, 6));
        b.insert(0, rg(0, 2));
        Trie<Object> trie = b.build();
        assertThat(trie.match(-1), is(asList()));

        assertThat(trie.match(1), is(asList(0)));
        assertThat(trie.match(5), is(asList(1)));
        assertThat(trie.match(8), is(asList(2)));

        assertThat(trie.match(20), is(asList()));
    }

    @Test
    public void nonOverlapRanges() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(0, rg(0, 2));
        b.insert(1, rg(2, 6));
        b.insert(2, rg(6, 10));
        Trie<Object> trie = b.build();
        assertThat(trie.match(-1), is(asList()));

        assertThat(trie.match(1), is(asList(0)));
        assertThat(trie.match(5), is(asList(1)));
        assertThat(trie.match(8), is(asList(2)));

        assertThat(trie.match(20), is(asList()));
    }


    @Test
    public void nonOverlapGtLtRanges() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(-1, lt(0));
        b.insert(0, rg(0, 2));
        b.insert(1, rg(2, 6));
        b.insert(2, rg(6, 10));
        b.insert(3, gt(10));
        Trie<Object> trie = b.build();
        assertThat(trie.match(-1), is(asList(-1)));

        assertThat(trie.match(1), is(asList(0)));
        assertThat(trie.match(5), is(asList(1)));
        assertThat(trie.match(8), is(asList(2)));

        assertThat(trie.match(20), is(asList(3)));
    }


    @Test
    public void nestedRanges() {
        Trie.Builder<Object> b = Trie.builder();

        b.insert(0, rg(0, 2), rg(0, 2));
        b.insert(1, rg(2, 6), rg(0, 2));
        b.insert(2, rg(6, 10), rg(0, 2));

        Trie<Object> trie = b.build();

        assertThat(trie.match(-1, 1), is(asList()));
        assertThat(trie.match(1, 1), is(asList(0)));
        assertThat(trie.match(1, -1), is(asList()));

        assertThat(trie.match(5, 1), is(asList(1)));
        assertThat(trie.match(5, -1), is(asList()));

        assertThat(trie.match(8, 1), is(asList(2)));
        assertThat(trie.match(8, -1), is(asList()));

        assertThat(trie.match(-20, 1), is(asList()));
    }

}