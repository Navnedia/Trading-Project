package order;

import price.Price;
import price.PriceFactory;
import productbookside.BookSide;
import exceptions.InvalidOrderOperation;

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

    Order(String user, String product, BookSide side, Price price, int volume) throws InvalidOrderOperation {
        setUser(user);
        setProduct(product);
        setSide(side);
        setPrice(price);
        setVolume(volume);
        this.id = this.user + this.product + price + System.nanoTime();
    }

    private void setUser(String user) throws InvalidOrderOperation {
        if (user == null) { throw new InvalidOrderOperation("Invalid user argument: null"); }
        if (user.length() != 3) { throw new InvalidOrderOperation("Invalid user argument: must be 3 characters in length"); }
        if (!user.matches("[a-zA-Z]*")) { throw new InvalidOrderOperation("Invalid user argument: must only contain letter characters"); }
        this.user = user.toUpperCase();
    }

    private void setProduct(String product) throws InvalidOrderOperation {
        if (product == null) { throw new InvalidOrderOperation("Invalid product argument: null"); }
        if (product.length() == 0 || product.length() > 5) { throw new InvalidOrderOperation("Invalid product argument: length must be 1 to 5 characters"); }
        if (!product.matches("[a-zA-Z0-9.]*")) { throw new InvalidOrderOperation("Invalid product argument: must only contain letters, numbers, or a period"); }
        this.product = product.toUpperCase();
    }

    private void setPrice(Price price) throws InvalidOrderOperation {
        if (price == null) { throw new InvalidOrderOperation("Invalid Price argument: null"); }
        this.price = price;
    }

    private void setSide(BookSide side) throws InvalidOrderOperation {
        if (side == null) { throw new InvalidOrderOperation("Invalid BookSide argument: null"); }
        this.side = side;
    }

    //! Might be changed when I work on the trading aspect.
    private void setVolume(int volume) throws InvalidOrderOperation {
        if (volume > 0 && volume < 10000) { throw new InvalidOrderOperation("Invalid volume argument: must be between 1 and 10000"); }
        this.originalVolume = volume;
        this.remainingVolume = volume;
    }

    //! Should this be private?
    public void setRemainingVolume(int volume) throws InvalidOrderOperation {
        //! Add validation.
        //! Should I separate out conditions for better error?
        if (volume < 0 || volume > originalVolume) { throw new InvalidOrderOperation("Invalid remaining volume argument: cannot be negative or above the original volume " + originalVolume); }
        remainingVolume = volume;
    }

    //! Might be changed when I work on the trading aspect.
    //! Do I want to have this maintain the remaining volume as well?
    //! Should this be private?
    public void setFilledVolume(int volume) throws InvalidOrderOperation {
        //! Add validation.
        //! Should I separate out conditions for better error?
        if (volume < 0 || volume > originalVolume) { throw new InvalidOrderOperation("Invalid filled volume argument: cannot be negative or above the original volume " + originalVolume); }
        filledVolume = volume;
    }

    //! Should this be private?
    public void setCancelledVolume(int volume) throws InvalidOrderOperation {
        //! Add validation.
        //! Should I separate out conditions for better error?
        if (volume < 0 || volume > originalVolume) { throw new InvalidOrderOperation("Invalid canceled volume argument: cannot be negative or above the original volume " + originalVolume); }
        cancelledVolume = volume;
    }

    public String getUser() {
        return user;
    }

    public String getProduct() {
        return product;
    }

    public String getId() {
        return id;
    }

    public BookSide getSide() {
        return side;
    }

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

    public void fillVolume(int volume) throws InvalidOrderOperation {
        //! Should I separate out conditions for better error?
        if (volume < 0 || volume > remainingVolume) { throw new InvalidOrderOperation("Invalid fill volume argument: cannot be negative or above the remaining volume" + remainingVolume); }
        filledVolume += volume;
        remainingVolume -= volume;
    }

    public void cancelOrder() {
        cancelledVolume = remainingVolume;
        remainingVolume = 0;
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
