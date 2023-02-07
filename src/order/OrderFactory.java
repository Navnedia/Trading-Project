package order;

import price.Price;
import productbookside.BookSide;

import exceptions.InvalidArgumentException;
import exceptions.InvalidRangeException;
import exceptions.NullArgumentException;

public abstract class OrderFactory {

    public static Order makeOrder(String user, String product, BookSide side, Price price, int volume) throws NullArgumentException, InvalidRangeException, InvalidArgumentException {
        return new Order(user, product, side, price, volume);
    }
}
