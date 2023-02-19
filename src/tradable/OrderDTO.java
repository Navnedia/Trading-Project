package tradable;

import price.Price;
import book.BookSide;

public class OrderDTO {

    public String user;
    public String product;
    public String id;
    public BookSide side;
    public Price price;

    public int originalVolume;
    public int remainingVolume;
    public int filledVolume;
    public int cancelledVolume;

    public OrderDTO(String user, String product, String id, BookSide side, Price price,
                    int originalVolume, int remainingVolume, int filledVolume, int cancelledVolume) {
        this.user = user;
        this.product = product;
        this.id = id;
        this.side = side;
        this.price = price;
        this.originalVolume = originalVolume;
        this.remainingVolume = remainingVolume;
        this.filledVolume = filledVolume;
        this.cancelledVolume = cancelledVolume;
    }

    @Override
    public String toString() {
        return user + " order: " + side + " " + product + " at " + price + ", Orig Vol: " + originalVolume
                + ", Rem Vol: " + remainingVolume + ", Fill Vol: " + filledVolume + ", CXL Vol: " + cancelledVolume + ", ID: " + id;
    }

}
