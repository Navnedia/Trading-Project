package simulation;

import book.BookSide;
import book.ProductManager;
import exceptions.*;
import price.Price;
import price.PriceFactory;
import tracking.CurrentMarketPublisher;
import tradable.Order;
import tradable.OrderDTO;
import user.User;
import user.UserManager;

import java.util.HashMap;

public final class TrafficSim {

    /** Store base prices for realistic simulation. Mapping the stock symbol to the base price. */
    private static final HashMap<String, Double> basePrices = new HashMap<>();
    /** The number of trade operations to simulate. */
    private static final int SIM_ITERATIONS = 1000;

    // Constant parameters for generating prices:
    private static final double PRICE_WIDTH = 0.02;
    private static final double START_POINT = 0.01;
    private static final double TICK_SIZE = 0.1;

    private TrafficSim() {} // Don't let anyone create an instance of this class.

    /**
     * Set up data for simulation initializing Users, ProductBooks, and observers by subscribing users
     * to market publications.
     */
    private static void initData() throws InvalidArgumentException, NullArgumentException {
        // Store base prices for stock symbols:
        basePrices.put("WMT", 140.98);
        basePrices.put("TGT", 174.76);
        basePrices.put("AMZN", 102.11);
        basePrices.put("TSLA", 196.81);
        for (String symbol : basePrices.keySet()) { // Initialize ProductBooks.
            ProductManager.getInstance().addProduct(symbol);
        }

        UserManager.getInstance().init(new String[]{"ANN", "BOB", "CAT", "DOG", "EGG"}); // Initialize all users.
        // Subscribe users to current market publications:
        User u1 = UserManager.getInstance().getUser("ANN");
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("WMT", u1);
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TGT", u1);

        User u2 = UserManager.getInstance().getUser("BOB");
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TGT", u2);
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TSLA", u2);

        User u3 = UserManager.getInstance().getUser("CAT");
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("AMZN", u3);
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TGT", u3);
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("WMT", u3);

        User u4 = UserManager.getInstance().getUser("DOG");
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TSLA", u4);

        User u5 = UserManager.getInstance().getUser("EGG");
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("WMT", u5);
    }

    /**
     * Clean up after simulation by unsubscribing users from current market publications.
     */
    private static void tearDown() throws NullArgumentException {
        User u1 = UserManager.getInstance().getUser("ANN");
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("WMT", u1);
        CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TGT", u1);

        User u2 = UserManager.getInstance().getUser("BOB");
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("TGT", u2);
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("TSLA", u2);

        User u3 = UserManager.getInstance().getUser("CAT");
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("AMZN", u3);
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("TGT", u3);
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("WMT", u3);

        User u4 = UserManager.getInstance().getUser("DOG");
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("TSLA", u4);

        User u5 = UserManager.getInstance().getUser("EGG");
        CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("WMT", u5);
    }

    public static void runSim() {
        try {
            initData(); // Set up Users, ProductBooks, and user market subscriptions for the simulation.

            // Simulate user unSubscribe to test.
            CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("TGT",
                UserManager.getInstance().getUser("BOB"));

            // Run a simulation loop to perform stock trade operations:
            for (int i = 0; i < SIM_ITERATIONS; i++) {
                User user = UserManager.getInstance().getRandomUser();

                if (Math.random() < 0.9) { // Add a random order to the user.
                    // Pick a random options for product, side, volume, and price:
                    String productSymbol = ProductManager.getInstance().getRandomProduct();
                    BookSide side = (Math.random() < 0.5) ? BookSide.BUY : BookSide.SELL;
                    int volume = (int) (25 + (Math.random() * 300));
                    volume = (int) Math.round(volume / 5.0) * 5;
                    Price price = getPrice(productSymbol, side);

                    try { // Create and add the new order on the product books and in the user.
                        Order order = new Order(user.getUserId(), productSymbol, side, price, volume);
                        System.out.println((i + 1) + ") ADD: " + side + ": " + order); // Log add message in the console.

                        OrderDTO dto  = ProductManager.getInstance().addOrder(order);
                        user.addOrder(dto);
                    } catch (DataValidationException e) {
                        e.printStackTrace();
                    }
                } else { // Cancel an order with remaining volume on the user.
                    if (user.hasOrderWithRemainingQty()) {
                        try {
                            OrderDTO order = user.getOrderWithRemainingQty(); // Get an order from the user.
                            OrderDTO canceled = ProductManager.getInstance().cancel(order); // Cancel the order in the product books.

                            if (canceled != null) {
                                user.addOrder(canceled); // Update the users copy of the order.
                                System.out.println((i + 1) + ") CANCEL: " + canceled.side // Log cancel message in the console.
                                        + " Order: " + canceled.id + " CXL Vol: " + canceled.cancelledVolume);
                            }
                        } catch (DataValidationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            displayApplicationState(); // Call helper to log current market, product data, and user data.
            tearDown(); // Clean up unsubscribe users from stocks.
        } catch (TradingApplicationException e) {
            e.printStackTrace();
        }
    }

    private static Price getPrice(String symbol, BookSide side) throws NullArgumentException, InvalidArgumentException {
        if (symbol == null) { throw new NullArgumentException("Invalid symbol argument: null"); }
        if (side == null) { throw new NullArgumentException("Invalid BookSide argument: null"); }

        // Get base price for the symbol and validate the output:
        Double basePrice = basePrices.get(symbol);
        if (basePrice == null) { throw new InvalidArgumentException("Invalid symbol argument: no base price exists for the symbol " + symbol); }

        // Calculate a random price variation:
        double gapFromBase = basePrice * PRICE_WIDTH;
        double priceVariance = gapFromBase * Math.random();

        // Adjust the price with the variation based on book side.
        double priceToUse = 0.0;
        switch (side) {
            case BUY -> priceToUse = (basePrice * (1 - START_POINT)) + priceVariance;
            case SELL -> priceToUse = (basePrice * (1 + START_POINT)) - priceVariance;
        }

        double priceToTick = Math.round(priceToUse * 1/TICK_SIZE) / 20.0;
        return PriceFactory.makePrice(String.valueOf(priceToTick));
    }

    private static void displayApplicationState() {
        System.out.println("\n------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Current Markets:\n");
        // Log out a summary for the current market values from each user:
        for (String id : new String[]{"ANN", "BOB", "CAT", "DOG", "EGG"}) {
            try {
                System.out.println(id + ":" + UserManager.getInstance().getUser(id).getCurrentMarkets() + "\n");
            } catch (NullArgumentException e) {
                e.printStackTrace();
            }
        }

        // Display data left in the ProductManager and UserManager:
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(ProductManager.getInstance());
        System.out.println("\n------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(UserManager.getInstance());
    }
}
