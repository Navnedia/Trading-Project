package user;

import tradable.OrderDTO;
import exceptions.InvalidArgumentException;
import exceptions.NullArgumentException;

import java.util.HashMap;

public class User {

    private String userId;
    /** Store orders mapping the order ID to the OrderDTO. */
    private final HashMap<String, OrderDTO> orders;

    public User(String id) throws NullArgumentException, InvalidArgumentException {
        setUserId(id);
        orders = new HashMap<>();
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
