package user;

import tracking.CurrentMarketObserver;
import tracking.CurrentMarketSide;
import tradable.OrderDTO;
import exceptions.InvalidArgumentException;
import exceptions.NullArgumentException;

import java.util.HashMap;

public class User implements CurrentMarketObserver {

    private String userId;

    /** Store orders mapping the order ID to the {@link OrderDTO}. */
    private final HashMap<String, OrderDTO> orders;

    /**
     * Store current market values for the stock symbols the user is subscribed to.
     * Mapping the stock symbol to a {@link CurrentMarketSide} array where index
     * ([0] = Buy market values, [1] = Sell market values).
     */
    private final HashMap<String, CurrentMarketSide[]> currentMarkets;

    public User(String id) throws NullArgumentException, InvalidArgumentException {
        setUserId(id);
        orders = new HashMap<>();
        currentMarkets = new HashMap<>();
    }

    private void setUserId(String id) throws NullArgumentException, InvalidArgumentException {
        if (id == null) { throw new NullArgumentException("Invalid userId argument: null"); }
        if (id.length() != 3) { throw new InvalidArgumentException("Invalid userId argument: must be 3 characters in length"); }
        if (!id.matches("[a-zA-Z]*")) { throw new InvalidArgumentException("Invalid userId argument: must only contain letter characters"); }
        this.userId = id.toUpperCase();
    }

    public String getUserId() {
        return userId;
    }

    public void addOrder(OrderDTO order) throws NullArgumentException {
        if (order == null) { throw new NullArgumentException("Invalid OrderDTO argument: null"); }
        orders.put(order.id, order);
    }

    public boolean hasOrderWithRemainingQty() {
        return getOrderWithRemainingQty() != null;
    }

    public OrderDTO getOrderWithRemainingQty() {
        // Loop though all the orders and return the first order with volume remaining:
        for (OrderDTO order : orders.values()) {
            if (order.remainingVolume > 0) { return order; }
        }

        return null;
    }

    @Override
    public void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) {
        if (symbol != null && buySide != null && sellSide != null) {
            // Store local copy of the top current market values for the given stock symbol.
            currentMarkets.put(symbol, new CurrentMarketSide[]{buySide, sellSide});
        }
    }

    public String getCurrentMarkets() {
        StringBuilder out = new StringBuilder();
        // Add a current market summary of each stock symbol the users is subscribed to:
        for (String symbol : currentMarkets.keySet()) {
            CurrentMarketSide[] sides = currentMarkets.get(symbol);
            out.append("\n").append(symbol).append(" ").append(sides[0]).append(" - ").append(sides[1]);
        }

        return out.toString();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("User Id: ").append(userId); // Add User Id header.
        // If there are no orders, then use empty placeholder.
        if (orders.size() == 0) { return out.append("\n\t<Empty>").toString(); }
        for (OrderDTO dto : orders.values()) { // Add all order summaries.
            out.append("\n\t").append(dto);
        }

        return out.toString();
    }
}
