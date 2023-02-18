package productbookside;

import exceptions.InvalidArgumentException;
import order.Order;
import order.OrderDTO;
import price.Price;
import exceptions.InvalidRangeException;
import exceptions.NullArgumentException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ProductBookSide {

    private BookSide side;
    private final HashMap<Price, ArrayList<Order>> bookEntries;

    ProductBookSide(BookSide side) throws NullArgumentException {
        setSide(side);
        bookEntries = new HashMap<>();
    }

    private void setSide(BookSide side) throws NullArgumentException {
        if (side == null) { throw new NullArgumentException("Invalid BookSide argument: null"); }
        this.side = side;
    }

    public OrderDTO add(Order o) throws NullArgumentException {
        if (o == null) { throw new NullArgumentException("Invalid Order argument: null"); }
        Price price = o.getPrice();
        if (!bookEntries.containsKey(price)) { // Add the price key if it doesn't exist already.
            bookEntries.put(price, new ArrayList<Order>());
        }
        bookEntries.get(price).add(o);

        return o.makeTradableDTO();
    }

    public OrderDTO cancel(String orderId) throws NullArgumentException, InvalidRangeException {
        if (orderId == null) { throw new NullArgumentException("Invalid orderId argument: null"); }
        OrderDTO dto = null;
        // Search for an order with the given id under each price:
        for (Price price : bookEntries.keySet()) {
            for (Order order : bookEntries.get(price)) {
                if (order.getId().equals(orderId)) { // If we find the order, we remove and cancel it.
                    bookEntries.get(price).remove(order);
                    // Cancel the remaining volume and zero out:
                    order.setCancelledVolume(order.getRemainingVolume());
                    order.setRemainingVolume(0);
                    dto = order.makeTradableDTO();
                    // Clean up, remove the price entry if it's empty:
                    if (bookEntries.get(price).size() == 0) {
                        bookEntries.remove(price);
                    }
                    break;
                }
            }
        }

        return dto;
    }

    private ArrayList<Price> getOrderedPrices() {
        ArrayList<Price> prices = new ArrayList<>(bookEntries.keySet()); // Put arraylist so we can sort.
        switch (side) { // Sort price keys in the appropriate order:
            case BUY -> prices.sort(Collections.reverseOrder());
            case SELL -> prices.sort(null);
        }

        return prices;
    }

    public Price topOfBookPrice() {
        ArrayList<Price> prices = getOrderedPrices();
        return (prices.size() > 0) ? prices.get(0) : null; // Return the top price of there is one.
    }

    public int topOfBookVolume() {
        int totalVolume = 0;
        ArrayList<Order> priceOrders = bookEntries.get(topOfBookPrice()); // Get orders for the top price.
        if (priceOrders != null) { // Add up the volumes if the orders exist.
            for (Order order : priceOrders) {
                totalVolume += order.getRemainingVolume();
            }
        }

        return totalVolume;
    }

    public void tradeOut(Price price, int tradeVolume) throws NullArgumentException, InvalidRangeException, InvalidArgumentException {
        if (price == null) { throw new NullArgumentException("Invalid price argument: null"); }
        ArrayList<Order> orders = bookEntries.get(price);
        if (orders == null) { throw new InvalidArgumentException("Invalid argument: no orders found for price " + price); }

        // Continue to trade till we have traded all the volume requested:
        while (tradeVolume > 0) {
            // Trade as much volume as we can from the first order.
            Order currentOrder = orders.get(0);
            int orderRemainingVol = currentOrder.getRemainingVolume();
            if (orderRemainingVol <= tradeVolume) { // If the requested volume is lager than the order we can fully fill this oder and remove it.
                orders.remove(0);
                currentOrder.setFilledVolume(currentOrder.getFilledVolume() + orderRemainingVol);
                currentOrder.setRemainingVolume(0);
                tradeVolume -= orderRemainingVol;
                System.out.println("FILL: (" + side + " " + orderRemainingVol + ") " + currentOrder);
            } else { // If the requested volume is less than the order, we will just do a partial fill.
                currentOrder.setFilledVolume(currentOrder.getFilledVolume() + tradeVolume);
                currentOrder.setRemainingVolume(orderRemainingVol - tradeVolume);
                System.out.println("PARTIAL FILL: (" + side + " " + tradeVolume + ") " + currentOrder);
                tradeVolume = 0;
            }
        }

        if (orders.size() == 0) { // Clean up, remove the price entry if it's empty:
            bookEntries.remove(price);
        }
    }

    @Override
    public String toString() {
        ArrayList<Price> prices = getOrderedPrices();
        StringBuilder out = new StringBuilder("Side: " + side);
        if (prices.size() == 0) { // If this book side contains no entries, then use empty as a placeholder.
            return out.append("\n\t<Empty>").toString();
        }
        // Format each price and the entries:
        for (Price price: prices) {
            out.append("\n\tPrice: ").append(price);
            for (Order order: bookEntries.get(price)) {
                out.append("\n\t\t").append(order);
            }
        }

        return out.toString();
    }
}
