package price;

import exceptions.InvalidArgumentException;
import exceptions.NullArgumentException;

import java.util.HashMap;

/** A factory class for creating Price objects.*/
public abstract class PriceFactory {

    /**
     * Track the previously created Price objects mapped to their value to implement the flyweight pattern.
     */
    private static final HashMap<Long, Price> createdPrices = new HashMap<Long, Price>();

    /**
     * A Factory method to construct a Price object.
     *
     * @param value a long/integer number representing the price value in cents.
     * @return a new Price object for the specified cents value.
     */
    public static Price makePrice(long value) {
        // Uses the flyweight pattern to avoid creating multiple Price objects for the same value:
        Price price = createdPrices.get(value); // Get the Price obj for the given value from the map.
        if (price == null) {
            // If the object doesn't exist, then create a new Price and add it to the map for later.
            price = new Price(value);
            createdPrices.put(value, price);
        }

        return price;
    }

    /**
     * A Factory method that converts a string representation to construct a Price object; accepts a range of
     * different string representations.<br><br>
     *
     * <b>String parameter price format: “[$][-]d*[.cc]”</b><br>
     * (d = dollars, c = cents, [$] = optional dollar sign, [-] = optional minus sign)
     *
     * <ul>
     *     <li>Any additional digits after the 2 cents-digits will be truncated - NOT ROUNDED! (ex: 12.4567 ➔ 12.45)</li>
     *     <li>Optional commas separating thousands-places of the dollars are allowed (ex: “1,234.56”).</li>
     * </ul>
     *
     * <b>Example representations include:</b><br>
     * "$12.85", "$-12.85", "-0.75", "$.89", "$1,234,567.89", "$12345.67", etc.
     *
     * @param valueStr a string price representation.
     * @return a new Price object for the specified value.
     * @throws NullArgumentException if the value string is null.
     * @throws InvalidArgumentException if the value string is empty, or not a valid number value.
     */
    public static Price makePrice(String valueStr) throws NullArgumentException, InvalidArgumentException {
        if (valueStr == null) { throw new NullArgumentException("Invalid valueStr argument: null"); }
        if (valueStr.isEmpty()) { throw new InvalidArgumentException("Invalid valueStr argument: empty string"); }

        try {
            valueStr = valueStr.strip().replaceAll("[$,]", ""); // Strip out the dollar sign, commas, and unnecessary spaces.
            Double.parseDouble(valueStr); // Check for proper format by parsing.
            String[] valueParts = valueStr.split("\\."); // Split into dollars and cents.

            long dollars = 0;
            // Process cents: Don't try to parse if it's blank or just a negative sign.
            if (valueParts[0].length() != 0 && !valueParts[0].equals("-")) {
                dollars = Long.parseLong(valueParts[0]);
            }

            long cents = 0;
            // Process cents: Don't try to parse if it's blank.
            if (valueParts.length == 2 && valueParts[1].length() != 0) {
                String centsStr = valueParts[1];
                if (centsStr.length() > 2) { // Truncate to 2 decimal places if needed.
                    centsStr = centsStr.substring(0, 2);
                } else if (centsStr.length() == 1) { // Add 2nd decimal place if only one is specified (ex: .8 = .80).
                    centsStr += "0";
                }

                cents = Long.parseLong(centsStr); // Parse cents to a long.
                if (valueStr.startsWith("-")) {
                    // If the number is negative then make cents negative so that math works.
                    cents = -cents;
                }
            }

            return makePrice((dollars * 100) + cents); // Create a Price with the dollars and cents combined.
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Invalid valueStr argument '" + valueStr +"': " + e.getMessage());
        }
    }
}
