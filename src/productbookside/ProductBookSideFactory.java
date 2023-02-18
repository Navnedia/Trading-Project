package productbookside;

import exceptions.NullArgumentException;

public abstract class ProductBookSideFactory {
    public static ProductBookSide makeSide(BookSide side) throws NullArgumentException {
        return new ProductBookSide(side);
    }
}