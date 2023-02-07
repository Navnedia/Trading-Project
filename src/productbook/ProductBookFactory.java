package productbook;

import exceptions.InvalidArgumentException;
import exceptions.NullArgumentException;

public abstract class ProductBookFactory {

    public static ProductBook makeBook(String product) throws NullArgumentException, InvalidArgumentException {
        return new ProductBook(product);
    }
}