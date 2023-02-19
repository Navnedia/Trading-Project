import tradable.OrderDTO;
import tradable.OrderFactory;
import price.Price;
import price.PriceFactory;

import productbook.ProductBook;
import productbook.ProductBookFactory;
import productbookside.BookSide;
import exceptions.TradingApplicationException;
import exceptions.InvalidArgumentException;
import exceptions.NullArgumentException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
//        testPrice();
        testProductBook();
    }

    // Testing Helper Methods:
    public static List<Price> priceList(long[] values) {
        List<Price> prices = new ArrayList<>();
        for (long val : values) {
            prices.add(PriceFactory.makePrice(val));
        }
        return prices;
    }

    public static void testStrings(String[] values) {
        for (String val : values) {
            try {
                Price p = PriceFactory.makePrice(val);
                System.out.println("Input: " + val + " | Coins: " + p.getValue() + " | Print: " + p);
            } catch (NullArgumentException | InvalidArgumentException e) {
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
        } catch (NullArgumentException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void testPrice() {
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
        long[] intTests = {-1, 0, 1, 20, 300, 4000, 50000};
        for (long test : intTests) {
            Price price = PriceFactory.makePrice(test);
            System.out.println(price.getValue() + " = " + price);
        }

        System.out.println("\n--- Test sort Price list ---");
        List<Price> prices = priceList(new long[]{100, -54, 5432, -999, 101, 100, 1234, 5, 0, 99,  -1234, -8});
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
        } catch (NullArgumentException e) {
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

    public static void testProductBook() {
        // Test ProductBook Functionality:
        // Obviously it would be more ideal to read in tests from a file, but this seemed quicker for this sake :)
        try {
            ProductBook book = ProductBookFactory.makeBook("AMZN");

            // 1)
            book.add(OrderFactory.makeOrder("AAA", "AMZN", BookSide.BUY, PriceFactory.makePrice(1000), 50));
            System.out.println(book);
            // 2)
            book.add(OrderFactory.makeOrder("BBB", "AMZN", BookSide.BUY, PriceFactory.makePrice(1000), 60));
            System.out.println(book);
            // 3)
            book.add(OrderFactory.makeOrder("CCC", "AMZN", BookSide.BUY, PriceFactory.makePrice(995), 70));
            System.out.println(book);
            // 4)
            book.add(OrderFactory.makeOrder("DDD", "AMZN", BookSide.BUY, PriceFactory.makePrice(990), 25));
            System.out.println(book);
            // 5)
            book.add(OrderFactory.makeOrder("EEE", "AMZN", BookSide.SELL, PriceFactory.makePrice(1010), 120));
            System.out.println(book);
            // 6)
            OrderDTO cancel = book.add(OrderFactory.makeOrder("FFF", "AMZN", BookSide.SELL, PriceFactory.makePrice(1020), 45));
            System.out.println(book);
            // 7)
            book.add(OrderFactory.makeOrder("GGG", "AMZN", BookSide.SELL, PriceFactory.makePrice(1025), 90));
            System.out.println(book);
            // 8)
            book.add(OrderFactory.makeOrder("HHH", "AMZN", BookSide.SELL, PriceFactory.makePrice(1000), 200));
            System.out.println(book);
            // 9)
            book.add(OrderFactory.makeOrder("III", "AMZN", BookSide.BUY, PriceFactory.makePrice(1010), 200));
            System.out.println(book);
            // 10)
            book.cancel(BookSide.SELL, cancel.id);
            System.out.println(book);
            // 11)
            book.add(OrderFactory.makeOrder("JJJ", "AMZN", BookSide.SELL, PriceFactory.makePrice(990), 95));
            System.out.println(book);
            // 12)
            book.add(OrderFactory.makeOrder("KKK", "AMZN", BookSide.BUY, PriceFactory.makePrice(1025), 100));
            System.out.println(book);
        } catch (TradingApplicationException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }
}
