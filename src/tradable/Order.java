package tradable;

import price.Price;
import price.PriceFactory;
import book.BookSide;

import exceptions.InvalidArgumentException;
import exceptions.InvalidRangeException;
import exceptions.NullArgumentException;

public class Order {

    private String user;
    private String product;
    private String id;
    private BookSide side;
    private Price price;

    private int originalVolume;
    private int remainingVolume;
    private int filledVolume = 0;
    private int cancelledVolume = 0;

    public Order(String user, String product, BookSide side, Price price, int volume) throws NullArgumentException, InvalidRangeException, InvalidArgumentException {
        setUser(user);
        setProduct(product);
        setSide(side);
        setPrice(price);
        setVolume(volume);
        this.id = this.user + this.product + price + System.nanoTime();
    }

    private void setUser(String user) throws NullArgumentException, InvalidArgumentException {
        if (user == null) { throw new NullArgumentException("Invalid user argument: null"); }
        if (user.length() != 3) { throw new InvalidArgumentException("Invalid user argument: must be 3 characters in length"); }
        if (!user.matches("[a-zA-Z]*")) { throw new InvalidArgumentException("Invalid user argument: must only contain letter characters"); }
        this.user = user.toUpperCase();
    }

    private void setProduct(String product) throws NullArgumentException, InvalidArgumentException {
        if (product == null) { throw new NullArgumentException("Invalid product argument: null"); }
        if (product.length() == 0 || product.length() > 5) { throw new InvalidArgumentException("Invalid product argument: length must be 1 to 5 characters"); }
        if (!product.matches("[a-zA-Z0-9.]*")) { throw new InvalidArgumentException("Invalid product argument: must only contain letters, numbers, or a period"); }
        this.product = product.toUpperCase();
    }

    private void setPrice(Price price) throws NullArgumentException {
        if (price == null) { throw new NullArgumentException("Invalid Price argument: null"); }
        this.price = price;
    }

    private void setSide(BookSide side) throws NullArgumentException {
        if (side == null) { throw new NullArgumentException("Invalid BookSide argument: null"); }
        this.side = side;
    }

    private void setVolume(int volume) throws InvalidRangeException {
        if (volume <= 0 || volume >= 10000) { throw new InvalidRangeException("Invalid volume argument " +
                volume +": must be greater than 0, and less than 10000"); }
        this.originalVolume = volume;
        this.remainingVolume = volume;
    }

    public void setRemainingVolume(int volume) throws InvalidRangeException {
        if (volume < 0 || volume > originalVolume) { throw new InvalidRangeException("Invalid remaining volume argument " +
                volume + ": cannot be negative or above the original volume ->" + originalVolume); }
        remainingVolume = volume;
    }

    public void setFilledVolume(int volume) throws InvalidRangeException {
        if (volume < 0 || volume > originalVolume) { throw new InvalidRangeException("Invalid filled volume argument " +
                volume + ": cannot be negative or above the original volume ->" + originalVolume); }
        filledVolume = volume;
    }

    public void setCancelledVolume(int volume) throws InvalidRangeException {
        if (volume < 0 || volume > originalVolume) { throw new InvalidRangeException("Invalid canceled volume argument " +
                volume + ": cannot be negative or above the original volume ->" + originalVolume); }
        cancelledVolume = volume;
    }

    public String getUser() {
        return user;
    }

    public String getProduct() {
        return product;
    }

    public String getId() { return id; }

    public BookSide getSide() { return side; }

    public Price getPrice() {
        return PriceFactory.makePrice(price.getValue());
    }

    public int getOriginalVolume() {
        return originalVolume;
    }

    public int getRemainingVolume() {
        return remainingVolume;
    }

    public int getFilledVolume() {
        return filledVolume;
    }

    public int getCancelledVolume() {
        return cancelledVolume;
    }

    public OrderDTO makeTradableDTO() {
        return new OrderDTO(user, product, id, side, PriceFactory.makePrice(price.getValue()),
                            originalVolume, remainingVolume, filledVolume, cancelledVolume);
    }

    @Override
    public String toString() {
        return user + " order: " + side + " " + product + " at " + price + ", Orig Vol: " + originalVolume
                + ", Rem Vol: " + remainingVolume + ", Fill Vol: " + filledVolume + ", CXL Vol: " + cancelledVolume + ", ID: " + id;
    }
}
