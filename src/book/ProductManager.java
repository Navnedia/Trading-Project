package book;

import exceptions.InvalidArgumentException;
import exceptions.InvalidOperationException;
import exceptions.InvalidRangeException;
import exceptions.NullArgumentException;
import tradable.Order;
import tradable.OrderDTO;

import java.util.HashMap;

public final class ProductManager {

    /** Singleton class instance. */
    private static volatile ProductManager instance;
    /** Store books mapping the product symbol to the ProductBook.*/
    private final HashMap<String, ProductBook> productBooks;

    public static ProductManager getInstance() {
        // Create a new instance if the singleton instance doesn't already exist:
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    private ProductManager() { productBooks = new HashMap<>(); }

    public void addProduct(String symbol) throws NullArgumentException, InvalidArgumentException {
        if (symbol == null) { throw new NullArgumentException("Invalid product symbol argument: null"); }
        productBooks.put(symbol, new ProductBook(symbol));
    }

    public String getRandomProduct() throws InvalidOperationException {
        if (productBooks.size() == 0) { throw new InvalidOperationException("ProductManager is empty with no product book symbols to return"); }
        String[] symbols = productBooks.keySet().toArray(new String[0]); // Get all the symbols in an array.
        return symbols[(int)(Math.random() * symbols.length)]; // Pick a random symbol from the array.
    }

    public OrderDTO addOrder(Order order) throws NullArgumentException, InvalidArgumentException, InvalidRangeException {
        if (order == null) { throw new NullArgumentException("Invalid Order argument: null"); }
        // Call and add to the product book for the relevant symbol.
        ProductBook pb = productBooks.get(order.getProduct());
        if (pb != null) { // Ensure it exists first.
            return pb.add(order);
        } else {
            throw new InvalidArgumentException("Invalid Order argument: the orders product symbol "+ order.getProduct() +" does not exist");
        }
    }

    public OrderDTO cancel(OrderDTO order) throws NullArgumentException, InvalidRangeException, InvalidArgumentException {
        if (order == null) { throw new NullArgumentException("Invalid Order argument: null"); }
        // Call to remove from the product book with the relevant symbol.
        ProductBook pb = productBooks.get(order.product);
        if (pb != null) { // Ensure it exists first.
            return pb.cancel(order.side, order.id);
        } else {
            throw new InvalidArgumentException("Invalid OrderDTO argument: the orders product symbol " + order.product + " does not exist");
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("ProductBooks:"); // Add ProductBooks section header.
        // If there are no ProductBooks, then use empty placeholder.
        if (productBooks.size() == 0) { return out.append(" <Empty>\n").toString(); }
        for (ProductBook pb : productBooks.values()) { // Add all ProductBook summaries.
            out.append("\n\n").append(pb);
        }

        return out.toString();
    }
}
