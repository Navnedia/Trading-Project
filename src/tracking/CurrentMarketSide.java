package tracking;

import exceptions.NullArgumentException;
import price.Price;

/**
 * Holds the top-of-book state (price and volume) for a given market side, used to perform market publications.
 */
public class CurrentMarketSide {

    /** The top-of-book price for the market side. */
    private Price price;
    /** The top-of-book volume for the market side. */
    private int volume;

    public CurrentMarketSide(Price price, int volume) throws NullArgumentException {
        setPrice(price);
        setVolume(volume);
    }

    private void setPrice(Price price) throws NullArgumentException {
        if (price == null) { throw new NullArgumentException("Invalid Price argument: null"); }
        this.price = price ;
    }

    private void setVolume(int volume) {
        this.volume = volume;
    }

    public Price getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return price + "x" + volume;
    }
}
