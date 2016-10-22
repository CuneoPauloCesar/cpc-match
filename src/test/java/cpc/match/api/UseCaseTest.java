package cpc.match.api;

import org.junit.jupiter.api.Test;

import java.util.List;

import static cpc.match.api.Matcher.rg;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
* Product table example taken from:
* http://toomuchcoding.com/blog/2013/02/03/drools-decision-tables-with-camel-and/
*/
public class UseCaseTest {
    public static final String NO_DISCOUNT = "Sorry, no discount will be granted.";
    public static final String GRANTED_A_DISCOUNT = "Congratulations, you are granted a discount";
    public static final String AMERICAN_PRODUCTS_GRANTED_A_30_DISCOUNT = "Thank you for buying the american products - you will be granted a 30 discount.";
    public static final String GRANTED_A_50_DISCOUNT = "Thank you for your cooperation - we are giving you a 50 discount.";
    public static final String GRANTED_A_10_DISCOUNT = "Congratulations - you've been granted a 10 discount!";

    public static final String POLISH_PRODUCT_PRICE = "Polish product price";
    public static final String AMERICAN_PRODUCT_PRICE = "American product price";
    public static final String GERMAN_PRODUCT_PRICE = "German product price";
    public static final String SWEDISH_PRODUCT_PRICE = "Swedish product price";
    public static final String BRITISH_PRODUCT_PRICE = "British product price";
    public static final String SPANISH_PRODUCT_PRICE = "Spanish product price";

    public static final String ELECTRONIC = "ELECTRONIC";
    public static final String MEDICAL = "MEDICAL";

    public static final String ACCEPTED = "ACCEPTED";

    public static final String ESP = "ESP";
    public static final String PL = "PL";
    public static final String USA = "USA";
    public static final String GER = "GER";
    public static final String SWE = "SWE";
    public static final String UK = "UK";


    static class Discount {
        public final int amount;
        public final String msg;

        Discount(int amount, String msg) {
            this.amount = amount;
            this.msg = msg;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Discount discount = (Discount) o;

            if (amount != discount.amount) return false;
            return msg.equals(discount.msg);

        }

        @Override
        public int hashCode() {
            int result = amount;
            result = 31 * result + msg.hashCode();
            return result;
        }

        public static Discount make(int amount, String msg) {
            return new Discount(amount, msg);
        }
    }

    @Test
    public void testWithOutDefault() {
        Trie.Builder<Discount> b = Trie.builder();
        initTable(b);

        Trie<Discount> t = b.build();

        List<Discount> discounts = t.match(SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, MEDICAL, ESP, 10);
        assertThat(discounts, is(asList(Discount.make(25, GRANTED_A_DISCOUNT))));
    }

    @Test
    public void testWithDefault() {
        Trie.Builder<Discount> b = Trie.builder();
        initTable(b);
        // If we add a default, ie a row that match always
        Discount A_DEFAULT = Discount.make(0, NO_DISCOUNT);
        b.insert(A_DEFAULT, Matcher.ANY, Matcher.ANY, Matcher.ANY, Matcher.ANY, Matcher.ANY, Matcher.ANY);

        Trie<Discount> t = b.build();
        List<Discount> discounts = t.match(SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, MEDICAL, ESP, 10);
        // the default, will be returned as well
        assertThat(discounts, hasItems(A_DEFAULT, Discount.make(25, GRANTED_A_DISCOUNT)));
        assertEquals(discounts.size(), 2);
    }

    private void initTable(Trie.Builder<Discount> b) {
        //Base discounts rules,User Country,User acceptance Decision,Product Type,Product's country of origin,Quantity,Discount (in %),Additional Info
        b.insert(Discount.make(0, NO_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, ELECTRONIC, PL, rg(0, 5));
        b.insert(Discount.make(10, GRANTED_A_10_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, ELECTRONIC, PL, rg(5, 10));
        b.insert(Discount.make(50, GRANTED_A_50_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, ELECTRONIC, PL, rg(10, 100));
        b.insert(Discount.make(0, NO_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, ELECTRONIC, USA, rg(0, 10));
        b.insert(Discount.make(30, AMERICAN_PRODUCTS_GRANTED_A_30_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, ELECTRONIC, USA, rg(10, 100));
        b.insert(Discount.make(30, GRANTED_A_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, ELECTRONIC, GER, rg(5, 100));
        b.insert(Discount.make(40, GRANTED_A_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, ELECTRONIC, SWE, rg(5, 100));
        b.insert(Discount.make(20, GRANTED_A_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, ELECTRONIC, UK, rg(5, 100));
        b.insert(Discount.make(20, GRANTED_A_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, ELECTRONIC, ESP, rg(5, 100));
        b.insert(Discount.make(0, NO_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, MEDICAL, PL, rg(0, 5));
        b.insert(Discount.make(10, GRANTED_A_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, MEDICAL, PL, rg(5, 10));
        b.insert(Discount.make(50, GRANTED_A_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, MEDICAL, PL, rg(10, 100));
        b.insert(Discount.make(0, NO_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, MEDICAL, USA, rg(0, 10));
        b.insert(Discount.make(30, GRANTED_A_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, MEDICAL, USA, rg(10, 100));
        b.insert(Discount.make(30, GRANTED_A_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, MEDICAL, GER, rg(5, 100));
        b.insert(Discount.make(40, GRANTED_A_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, MEDICAL, SWE, rg(5, 100));
        b.insert(Discount.make(20, GRANTED_A_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, MEDICAL, UK, rg(5, 100));
        b.insert(Discount.make(20, GRANTED_A_DISCOUNT), POLISH_PRODUCT_PRICE, PL, ACCEPTED, MEDICAL, ESP, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), AMERICAN_PRODUCT_PRICE, USA, ACCEPTED, ELECTRONIC, PL, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), AMERICAN_PRODUCT_PRICE, USA, ACCEPTED, ELECTRONIC, USA, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), AMERICAN_PRODUCT_PRICE, USA, ACCEPTED, ELECTRONIC, GER, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), AMERICAN_PRODUCT_PRICE, USA, ACCEPTED, ELECTRONIC, SWE, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), AMERICAN_PRODUCT_PRICE, USA, ACCEPTED, ELECTRONIC, UK, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), AMERICAN_PRODUCT_PRICE, USA, ACCEPTED, ELECTRONIC, ESP, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), AMERICAN_PRODUCT_PRICE, USA, ACCEPTED, MEDICAL, PL, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), AMERICAN_PRODUCT_PRICE, USA, ACCEPTED, MEDICAL, USA, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), AMERICAN_PRODUCT_PRICE, USA, ACCEPTED, MEDICAL, GER, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), AMERICAN_PRODUCT_PRICE, USA, ACCEPTED, MEDICAL, SWE, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), AMERICAN_PRODUCT_PRICE, USA, ACCEPTED, MEDICAL, UK, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), AMERICAN_PRODUCT_PRICE, USA, ACCEPTED, MEDICAL, ESP, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), GERMAN_PRODUCT_PRICE, GER, ACCEPTED, ELECTRONIC, PL, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), GERMAN_PRODUCT_PRICE, GER, ACCEPTED, ELECTRONIC, USA, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), GERMAN_PRODUCT_PRICE, GER, ACCEPTED, ELECTRONIC, GER, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), GERMAN_PRODUCT_PRICE, GER, ACCEPTED, ELECTRONIC, SWE, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), GERMAN_PRODUCT_PRICE, GER, ACCEPTED, ELECTRONIC, UK, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), GERMAN_PRODUCT_PRICE, GER, ACCEPTED, ELECTRONIC, ESP, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), GERMAN_PRODUCT_PRICE, GER, ACCEPTED, MEDICAL, PL, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), GERMAN_PRODUCT_PRICE, GER, ACCEPTED, MEDICAL, USA, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), GERMAN_PRODUCT_PRICE, GER, ACCEPTED, MEDICAL, GER, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), GERMAN_PRODUCT_PRICE, GER, ACCEPTED, MEDICAL, SWE, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), GERMAN_PRODUCT_PRICE, GER, ACCEPTED, MEDICAL, UK, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), GERMAN_PRODUCT_PRICE, GER, ACCEPTED, MEDICAL, ESP, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SWEDISH_PRODUCT_PRICE, SWE, ACCEPTED, ELECTRONIC, PL, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SWEDISH_PRODUCT_PRICE, SWE, ACCEPTED, ELECTRONIC, USA, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SWEDISH_PRODUCT_PRICE, SWE, ACCEPTED, ELECTRONIC, GER, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SWEDISH_PRODUCT_PRICE, SWE, ACCEPTED, ELECTRONIC, SWE, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SWEDISH_PRODUCT_PRICE, SWE, ACCEPTED, ELECTRONIC, UK, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SWEDISH_PRODUCT_PRICE, SWE, ACCEPTED, ELECTRONIC, ESP, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SWEDISH_PRODUCT_PRICE, SWE, ACCEPTED, MEDICAL, PL, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SWEDISH_PRODUCT_PRICE, SWE, ACCEPTED, MEDICAL, USA, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SWEDISH_PRODUCT_PRICE, SWE, ACCEPTED, MEDICAL, GER, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SWEDISH_PRODUCT_PRICE, SWE, ACCEPTED, MEDICAL, SWE, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SWEDISH_PRODUCT_PRICE, SWE, ACCEPTED, MEDICAL, UK, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SWEDISH_PRODUCT_PRICE, SWE, ACCEPTED, MEDICAL, ESP, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), BRITISH_PRODUCT_PRICE, UK, ACCEPTED, ELECTRONIC, PL, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), BRITISH_PRODUCT_PRICE, UK, ACCEPTED, ELECTRONIC, USA, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), BRITISH_PRODUCT_PRICE, UK, ACCEPTED, ELECTRONIC, GER, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), BRITISH_PRODUCT_PRICE, UK, ACCEPTED, ELECTRONIC, SWE, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), BRITISH_PRODUCT_PRICE, UK, ACCEPTED, ELECTRONIC, UK, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), BRITISH_PRODUCT_PRICE, UK, ACCEPTED, ELECTRONIC, ESP, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), BRITISH_PRODUCT_PRICE, UK, ACCEPTED, MEDICAL, PL, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), BRITISH_PRODUCT_PRICE, UK, ACCEPTED, MEDICAL, USA, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), BRITISH_PRODUCT_PRICE, UK, ACCEPTED, MEDICAL, GER, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), BRITISH_PRODUCT_PRICE, UK, ACCEPTED, MEDICAL, SWE, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), BRITISH_PRODUCT_PRICE, UK, ACCEPTED, MEDICAL, UK, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), BRITISH_PRODUCT_PRICE, UK, ACCEPTED, MEDICAL, ESP, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, ELECTRONIC, PL, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, ELECTRONIC, USA, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, ELECTRONIC, GER, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, ELECTRONIC, SWE, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, ELECTRONIC, UK, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, ELECTRONIC, ESP, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, MEDICAL, PL, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, MEDICAL, USA, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, MEDICAL, GER, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, MEDICAL, SWE, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, MEDICAL, UK, rg(5, 100));
        b.insert(Discount.make(25, GRANTED_A_DISCOUNT), SPANISH_PRODUCT_PRICE, ESP, ACCEPTED, MEDICAL, ESP, rg(5, 100));
    }
}
