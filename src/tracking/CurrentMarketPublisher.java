package tracking;

import exceptions.NullArgumentException;

import java.util.ArrayList;
import java.util.HashMap;

public final class CurrentMarketPublisher {

    /** Singleton class instance. */
    private static CurrentMarketPublisher instance;

    /**
     * Store the observers subscribed to be notified about changes to specific stock symbols.
     * Used to filter updates by mapping the stock symbol key to a list of {@link CurrentMarketObserver}.
     */
    private final HashMap<String, ArrayList<CurrentMarketObserver>> filters;

    public static CurrentMarketPublisher getInstance() {
        // Create a new instance if the singleton instance doesn't already exist:
         if (instance == null) {
             instance = new CurrentMarketPublisher();
         }
         return instance;
    }

    private CurrentMarketPublisher() { filters = new HashMap<>(); }

    public void subscribeCurrentMarket(String symbol, CurrentMarketObserver cmo) throws NullArgumentException {
        if (symbol == null) { throw new NullArgumentException("Invalid stock symbol argument: null"); }
        if (cmo == null) { throw new NullArgumentException("Invalid CurrentMarketObserver argument: null"); }

        if (!filters.containsKey(symbol)) { // Add the stock symbol if it's not already in the filter.
            filters.put(symbol, new ArrayList<>());
        }
        filters.get(symbol).add(cmo); // Add the observer to the stock symbol filter.
    }

    public void unSubscribeCurrentMarket(String symbol, CurrentMarketObserver cmo) throws NullArgumentException {
        if (symbol == null) { throw new NullArgumentException("Invalid stock symbol argument: null"); }
        if (cmo == null) { throw new NullArgumentException("Invalid CurrentMarketObserver argument: null"); }

        if (filters.containsKey(symbol)) { // Check that the symbol exists in the filter.
            filters.get(symbol).remove(cmo); // Remove the observer to the stock symbol filter.

            if (filters.get(symbol).size() == 0) { // Remove symbol from filters when there are no observers.
                filters.remove(symbol);
            }
        }
    }

    public void acceptCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) throws NullArgumentException {
        ArrayList<CurrentMarketObserver> symbolObservers = filters.get(symbol);
        if (symbolObservers != null) { // Ensure the symbol has a valid filter entry.
            if (buySide == null) { throw new NullArgumentException("Invalid buySide argument: null"); }
            if (sellSide == null) { throw new NullArgumentException("Invalid sellSide argument: null"); }

            // Notify the observers of the current market values:
            for (CurrentMarketObserver observer : symbolObservers) {
                observer.updateCurrentMarket(symbol, buySide, sellSide);
            }
        }
    }
}
