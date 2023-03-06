package tracking;

import exceptions.NullArgumentException;
import price.Price;
import price.PriceFactory;

public final class CurrentMarketTracker {

    /** Singleton class instance. */
    private static CurrentMarketTracker instance;

    public static CurrentMarketTracker getInstance() {
        // Create a new instance if the singleton instance doesn't already exist:
        if (instance == null) {
            instance = new CurrentMarketTracker();
        }
        return instance;
    }

    private CurrentMarketTracker() {}

    public void updateMarket(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws NullArgumentException {
        if (symbol == null) { throw new NullArgumentException("Invalid product symbol argument: null"); }

        // Calculate the market width between the buy and sell prices. Zero width if either side has no price.
        Price marketWidth = (buyPrice == null || sellPrice == null) ?
                PriceFactory.makePrice(0) : sellPrice.subtract(buyPrice);
        // Store market state values:
        CurrentMarketSide buySide = new CurrentMarketSide((buyPrice == null) ? PriceFactory.makePrice(0) : buyPrice, buyVolume);
        CurrentMarketSide sellSide = new CurrentMarketSide((sellPrice == null) ? PriceFactory.makePrice(0) : sellPrice, sellVolume);

        // Log current market message in the console:
        System.out.println("*********** Current Market ***********");
        System.out.println("* " + symbol + " " + buySide + " - " + sellSide + " [" + marketWidth + "]");
        System.out.println("**************************************");

        // Send market update to the publisher to notify the observers.
        CurrentMarketPublisher.getInstance().acceptCurrentMarket(symbol, buySide, sellSide);
    }
}
