package order;

import exceptions.InvalidOrderOperation;
import price.Price;
import productbookside.BookSide;

public abstract class OrderFactory {

    public static Order makeOrder(String user, String product, BookSide side, Price price, int volume) throws InvalidOrderOperation {
        return new Order(user, product, side, price, volume);
    }

    //! Do I want to keep this?
//    public static Order makeOrder(String user, String product, BookSide side, int priceInt, int volume) throws InvalidOrderOperation {
//        return new Order(user, product, side, PriceFactory.makePrice(priceInt), volume);
//    }

    //! Do I want to keep this? MAYBE NOT because if price is null then it could get confused for the other method version.
//    public static Order makeOrder(String user, String product, BookSide side, String priceStr, int volume) throws InvalidOrderOperation, InvalidPriceOperation {
//        return new Order(user, product, side, PriceFactory.makePrice(priceStr), volume);
//    }

    //! Do I want to keep this? Technically it wouldn't even be the same since we wouldn't be passing it the other volumes.
//    public static Order makeOrder(OrderDTO dto) throws InvalidOrderOperation {
//        return new Order(dto.user, dto.product, dto.side, dto.price, dto.originalVolume);
//    }
}
