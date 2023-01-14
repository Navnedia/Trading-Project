package price;

import exceptions.NullArgumentException;

import java.util.Objects;

public class Price implements Comparable<Price> {

    private int value; // Price value stored in cents.

    /**
     * Constructs a Price object with the specified value.
     * @param value an integer representing the price value in cents.
     */
    Price(int value) {
        setValue(value);
    }

    /**
     * @return the value of this Price object in cents.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * @param value an integer representing the price value in cents.
     */
    private void setValue(int value) {
        this.value = value;
    }

    /**
     * @return true if this Price value is negative, false if positive or zero.
     */
    public boolean isNegative() {
        return value < 0;
    }

    /**
     * @param p a Price object to add.
     * @return a new Price object holding the sum of the current price plus the price object
     * passed in.
     * @throws NullArgumentException if the price argument is null.
     */
    public Price add(Price p) throws NullArgumentException {
        if (p == null) { throw new NullArgumentException("Invalid price argument: null"); }
        return new Price(value + p.value);
    }

    /**
     * @param p a Price object to subtract.
     * @return a new Price object holding the difference between the current price
     * minus the price object passed in.
     * @throws NullArgumentException if the price argument is null.
     */
    public Price subtract(Price p) throws NullArgumentException {
        if (p == null) { throw new NullArgumentException("Invalid price argument: null"); }
        return new Price(value - p.value);
    }

    /**
     * @param n an integer value to multiply the price by.
     * @return a new Price object holding the product of the current price and the
     * integer value passed in.
     */
    public Price multiply(int n) {
        return new Price(value * n);
    }

    /**
     * @param p a Price object to test against.
     * @return true if the current Price object is greater than or equal to the
     * Price object passed in.
     * @throws NullArgumentException if the price argument is null.
     */
    public boolean greaterOrEqual(Price p) throws NullArgumentException {
        if (p == null) { throw new NullArgumentException("Invalid price argument: null"); }
        return (value - p.value) >= 0;
    }

    /**
     * @param p a Price object to test against.
     * @return true if the current Price object is less than or equal to the Price
     * object passed in.
     * @throws NullArgumentException if the price argument is null.
     */
    public boolean lessOrEqual(Price p) throws NullArgumentException {
        if (p == null) { throw new NullArgumentException("Invalid price argument: null"); }
        return (value - p.value) <= 0;
    }

    /**
     * @param p a Price object to test against.
     * @return true if the current Price object is greater than the Price object
     * passed in.
     * @throws NullArgumentException if the price argument is null.
     */
    public boolean greaterThan(Price p) throws NullArgumentException {
        if (p == null) { throw new NullArgumentException("Invalid price argument: null"); }
        return (value - p.value) > 0;
    }

    /**
     * @param p a Price object to test against.
     * @return true if the current Price object is less than the Price object passed
     * in.
     * @throws NullArgumentException if the price argument is null.
     */
    public boolean lessThan(Price p) throws NullArgumentException {
        if (p == null) { throw new NullArgumentException("Invalid price argument: null"); }
        return (value - p.value) < 0;
    }

    /**
     * @param p a Price object to compare against.
     * @return the difference in cents between the current price object and the price
     * object passed in.
     * @throws NullPointerException if the price argument is null.
     */
    @Override
    public int compareTo(Price p) throws NullPointerException {
        if (p == null) { throw new NullPointerException("Invalid price argument: null"); }
        return value - p.value;
    }

    /**
     * A static method for formatting a cents integer to a price string
     * representation.
     *
     * @param value an integer representing the price value in cents.
     * @return a string representation of the price value (ex: "$12.34").
     */
    public static String toString(int value) {
        // Put the value in a StringBuilder without the minus sign.
        StringBuilder out = new StringBuilder(Integer.toString(value).replace("-", ""));
        // Format the numbers differently based on how many digits they have:
        if (out.length() >= 3) { // Separate the dollars and cents with a decimal.
            out.insert(out.length() - 2, ".");
        } else if (out.length() == 2) { // 2 digits requires a leading zero and the decimal first.
            out.insert(0, "0.");
        } else { // Just 1 digit requires a leading zero, the decimal, and another zero before our hundreds place.
            out.insert(0, "0.0");
        }
        // Add a dollar sign, and a minus sign to output if the price is negative.
        return "$" + ((value < 0) ? "-" : "") + out;
    }

    /**
     *  Formats the price into a money string representation, exactly as if the cents value
     *  were given to the static {@link price.Price#toString(int)} method.
     *
     * @return a string representation of the price value (ex: "$12.34").
     */
    @Override
    public String toString() {
        return toString(value);
    }

    /**
     * Checks an Object for equality against this Price based on the value.
     *
     * @param other an object to test for equality.
     * @return true if the current Price object equals the Price object passed in.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) { return true; }
        if (other == null || getClass() != other.getClass()) { return false; }
        Price price = (Price) other;
        return value == price.value;
    }

    /**
     *  Generates a hash code based on the value of the Price. Two Price objects with
     *  the same value will always have the same hash.
     *
     * @return an integer hash code value for this Price object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
