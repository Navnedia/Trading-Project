package tracking;

/**
 * An interface defining methods required to subscribe to and accept market updates
 * from the {@link CurrentMarketPublisher}.
 */
public interface CurrentMarketObserver {

        /**
         *
         * @param symbol The product symbol related to the update.
         * @param buySide A {@link CurrentMarketSide} object representing the current
         * buy state (price and volume) of the product.
         * @param sellSide A {@link CurrentMarketSide} object representing the current
         * sell state (price and volume) of the product.
         */
        void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide);
}