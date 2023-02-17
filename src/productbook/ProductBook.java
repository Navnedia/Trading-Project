package productbook;

import order.Order;
import order.OrderDTO;
import price.Price;
import productbookside.BookSide;
import productbookside.ProductBookSide;
import productbookside.ProductBookSideFactory;

import exceptions.InvalidArgumentException;
import exceptions.InvalidRangeException;
import exceptions.NullArgumentException;

public class ProductBook {

    private String product;
    private ProductBookSide buySide;
    private ProductBookSide sellSide;

    ProductBook(String product) throws NullArgumentException, InvalidArgumentException {
        setProduct(product);
        buySide = ProductBookSideFactory.makeSide(BookSide.BUY);
        sellSide = ProductBookSideFactory.makeSide(BookSide.SELL);
    }

  private void setProduct(String product) throws NullArgumentException, InvalidArgumentException {
        if (product == null) { throw new NullArgumentException("Invalid product argument: null"); }
        if (product.length() == 0 || product.length() > 5) { throw new InvalidArgumentException("Invalid product argument: length must be 1 to 5 characters"); }
        if (!product.matches("[a-zA-Z0-9.]*")) { throw new InvalidArgumentException("Invalid product argument: must only contain letters, numbers, or a period"); }
        this.product = product.toUpperCase();
    }

    public String getProduct() { return product; }

    public OrderDTO add(Order o) throws NullArgumentException, InvalidRangeException, InvalidArgumentException {
        if (o == null) { throw new NullArgumentException("Invalid Order argument: null"); }
        OrderDTO dto = null;
        switch (o.getSide()) { // Send the add request to the appropriate book side:
            case BUY -> dto = buySide.add(o);
            case SELL -> dto = sellSide.add(o);
        }

        tryTrade(); // Attempt to trade to see if we have new valid trades.
        return dto;
    }

    public OrderDTO cancel(BookSide side, String orderId) throws NullArgumentException, InvalidRangeException {
        if (side == null) { throw new NullArgumentException("Invalid BookSide argument: null"); }
        OrderDTO dto = null;
        switch (side) { // Send the cancel request to the appropriate book side:
            case BUY -> dto = buySide.cancel(orderId);
            case SELL -> dto = sellSide.cancel(orderId);
        }

        return dto;
    }

    public void tryTrade() throws NullArgumentException, InvalidRangeException, InvalidArgumentException {
        Price topBuy = buySide.topOfBookPrice();
        Price topSell = sellSide.topOfBookPrice();
        // While the buy price has a valid sell price for less or equal, continue trading:
        while (((topBuy != null) && (topSell != null)) && (topBuy.greaterOrEqual(topSell))) {
            int tradeVolume = Math.min(buySide.topOfBookVolume(), sellSide.topOfBookVolume()); // Trade the lowest common volume.
            sellSide.tradeOut(topSell, tradeVolume);
            buySide.tradeOut(topBuy, tradeVolume);
            // Get next highest prices.
            topBuy = buySide.topOfBookPrice();
            topSell = sellSide.topOfBookPrice();
        }
    }

    @Override
    public String toString() {
        return "Product: " + product + "\n" + buySide + "\n" + sellSide + "\n";
    }
}