import price.Price;
import price.PriceFactory;
import exceptions.InvalidPriceOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    // Testing Helper Methods:
    public static List<Price> priceList(int[] values) {
        List<Price> prices = new ArrayList<>();
        for (int val : values) {
            prices.add(PriceFactory.makePrice(val));
        }
        return prices;
    }

    public static void testStrings(String[] values) {
        for (String val : values) {
            try {
                Price p = PriceFactory.makePrice(val);
                System.out.println("Input: " + val + " | Coins: " + p.getValue() + " | Print: " + p);
            } catch (InvalidPriceOperation e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void testComparing(Price p, Price pOther) {
        System.out.println("\n" + p + " Compare with " + pOther);
        try {
            System.out.println(p.compareTo(pOther));
            System.out.println(p.lessThan(pOther));
            System.out.println(p.greaterThan(pOther));
            System.out.println(p.lessOrEqual(pOther));
            System.out.println(p.greaterOrEqual(pOther));
        } catch (InvalidPriceOperation e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Test valid strings ---");
        String[] strTests = {"12.85", "0.75", "12345.67", "1,234,567.89", "12", ".89",
                          "-12.85", "-0.75", "-12345.67", "-1,234,567.89", "-12", "-.89",
                          "$12.85", "$0.75", "$12345.67", "$1,234,567.89", "$12", "$.89",
                          "$-12.85", "$-0.75", "$-12345.67", "$-1,234,567.89", "$-12",
                          "$-.89", "$-34.54231", "0"};
        testStrings(strTests);

        System.out.println("\n--- Test invalid strings ---");
        String[] fails = {null, "", "1.2.3"};
        testStrings(fails);

        System.out.println("\n--- Test valid integers ---");
        int[] intTests = {-1, 0, 1, 20, 300, 4000, 50000};
        for (int test : intTests) {
            Price price = PriceFactory.makePrice(test);
            System.out.println(price.getValue() + " = " + price);
        }

        System.out.println("\n--- Test sort Price list ---");
        List<Price> prices = priceList(new int[]{100, -54, 5432, -999, 101, 100, 1234, 5, 0, 99,  -1234, -8});
        Collections.sort(prices);
        System.out.println(prices);


        Price p = PriceFactory.makePrice(2500);
        Price pMatch = PriceFactory.makePrice(2500);
        Price pLarger = PriceFactory.makePrice(4499);
        Price pSmaller = PriceFactory.makePrice(-1234);

        System.out.println("\n" + pSmaller + " is negative: " + pSmaller.isNegative());
        System.out.println(p + " is negative: " + p.isNegative());

        System.out.println("\n--- Math Operations ---");
        try {
            System.out.println(p + " + " + pLarger + " = " + p.add(pLarger));
            System.out.println(p + " - " + pLarger + " = " + p.subtract(pLarger));
            System.out.println(p + " * 2 = " + p.multiply(2));
        } catch (InvalidPriceOperation e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        System.out.println("\n--- Test Equals & Not-equals --");
        System.out.println(p.equals(pMatch));
        System.out.println(p.equals(pLarger));

        System.out.println("\n--- Matching Hash --");
        System.out.println(p.hashCode() + " = " + pMatch.hashCode());

        testComparing(p, pMatch);
        testComparing(p, pLarger);
        testComparing(p, pSmaller);
    }
}
