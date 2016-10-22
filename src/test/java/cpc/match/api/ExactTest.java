package cpc.match.api;

import org.junit.jupiter.api.Test;

import java.util.List;

import static cpc.match.api.Matcher.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExactTest {

    @Test
    public void equalityNestedBranch() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(1, or(1), or(1), or(1));
        Trie<Object> trie = b.build();

        assertThat(trie.match(1, 1, 1), hasItems(1));
        assertThat(trie.match(1, 1, 0), is(asList()));
        assertThat(trie.match(1, 0, 1), is(asList()));
        assertThat(trie.match(0, 1, 1), is(asList()));
    }

    @Test
    public void anyNestedBranch() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(1, ANY, ANY, ANY);
        Trie<Object> trie = b.build();
        assertThat(trie.match(1, 1, 1), hasItems(1));
        assertThat(trie.match(1, 1, 0), hasItems(1));
        assertThat(trie.match(1, 0, 1), hasItems(1));
        assertThat(trie.match(0, 1, 1), hasItems(1));
    }

    @Test
    public void norNestedBranch() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(1, nor(0), nor(0), nor(0));
        Trie<Object> trie = b.build();
        assertThat(trie.match(0, 0, 0), is(asList()));
        assertThat(trie.match(1, 1, 0), is(asList()));
        assertThat(trie.match(1, 0, 1), is(asList()));
        assertThat(trie.match(0, 1, 1), is(asList()));
        assertThat(trie.match(1, 1, 1), hasItems(1));
    }

    @Test
    public void bSearch() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(1, or(1), or(1));
        b.insert(2, or(1), or(2));
        b.insert(3, or(2), ANY);
        b.insert(4, or(3), nor(5));
        Trie<Object> trie = b.build();
        assertThat(trie.match(1, 1), hasItems(1));
        assertThat(trie.match(1, 2), hasItems(2));
        assertThat(trie.match(2, 5), hasItems(3));
        assertThat(trie.match(3, 1), hasItems(4));
    }

    @Test
    public void onlyANotSubTrie() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(4, nor(5));
        Trie<Object> trie = b.build();
        assertThat(trie.match(1), is(asList(4)));
        assertThat(trie.match(5), is(asList()));
    }

    @Test
    public void notInSubTrie() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(0, nor(0));
        b.insert(1, nor(1));
        b.insert(2, nor(2));
        b.insert(3, nor(3));
        Trie<Object> trie = b.build();
        assertThat(trie.match(0), hasItems(1, 2, 3));
        assertThat(trie.match(1), hasItems(0, 2, 3));
        assertThat(trie.match(2), hasItems(0, 1, 3));
        assertThat(trie.match(3), hasItems(0, 1, 2));
    }

    @Test
    public void onlyAInSubTrie() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(4, or(5));
        Trie<Object> trie = b.build();
        assertThat(trie.match(3), is(asList()));
        assertThat(trie.match(5), is(asList(4)));
    }

    @Test
    public void onlyANYSubTrie() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(4, ANY);
        Trie<Object> trie = b.build();
        assertThat(trie.match(0), is(asList(4)));
    }


    @Test
    public void manyIsNotNegationInSubTrie() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(0, nor(0));
        b.insert(1, nor(1));
        b.insert(2, ANY);
        Trie<Object> trie = b.build();
        List<Object> result = trie.match(1);
        assertThat(result, hasItems(0, 2));
        assertThat(result, not(hasItems(1)));
    }


    @Test
    public void matchesManySubTrie() {
        Trie.Builder<Object> b = Trie.builder();
        b.insert(0, ANY);
        b.insert(1, or(0));
        b.insert(2, nor(1));
        Trie<Object> trie = b.build();

        assertThat(trie.match(0), hasItems(0, 1, 2));
    }
}