package cpc.match.builder.matchers;

import cpc.match.api.Matcher;

import java.util.List;


public class Or implements Matcher {
    public final List a;

    public Or(List a) {
        this.a = a;
    }

}
