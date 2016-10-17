package cpc.match.builder.matchers;

import cpc.match.api.Matcher;

import java.util.List;


public class Nor implements Matcher {
    public static Object NOT = new Object() {
        @Override
        public String toString() {
            return "NOT";
        }
    };
    public final List a;

    public Nor(List a) {
        this.a = a;
    }
}
