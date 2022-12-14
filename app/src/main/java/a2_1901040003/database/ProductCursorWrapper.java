package a2_1901040003.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import a2_1901040003.model.Product;

import java.util.ArrayList;
import java.util.List;


public class ProductCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ProductCursorWrapper(Cursor cursor) {

        super(cursor);
    }

    //get the product
    public Product getProduct() {

        Product item = null;

        int id = getInt(getColumnIndex(DbSchema.ProductTable.Cols.ID));
        String nameItem = getString(getColumnIndex(DbSchema.ProductTable.Cols.NAME_ITEM));
        int priceItem = getInt(getColumnIndex(DbSchema.ProductTable.Cols.PRICE_ITEM));
        String imageUrl = getString(getColumnIndex(DbSchema.ProductTable.Cols.IMAGE_URL));
        int quantityItem = getInt(getColumnIndex(DbSchema.ProductTable.Cols.QUANTITY_ITEM));

        if (id != 0 && valid(nameItem) && valid(imageUrl) && priceItem > 0 && quantityItem > 0)
            item = new Product(id, imageUrl, nameItem, priceItem, quantityItem);
        return item;
    }

    public Product getAProduct() {
        moveToFirst();

        if (getCount() == 0) {
            return null;
        }

        //
        Product item = null;

        int id = getInt(getColumnIndex(DbSchema.ProductTable.Cols.ID));
        String nameItem = getString(getColumnIndex(DbSchema.ProductTable.Cols.NAME_ITEM));
        int priceItem = getInt(getColumnIndex(DbSchema.ProductTable.Cols.PRICE_ITEM));
        String imageUrl = getString(getColumnIndex(DbSchema.ProductTable.Cols.IMAGE_URL));
        int quantityItem = getInt(getColumnIndex(DbSchema.ProductTable.Cols.QUANTITY_ITEM));

        if (id != 0 && valid(nameItem) && valid(imageUrl) && priceItem > 0 && quantityItem > 0)
            item = new Product(id, imageUrl, nameItem, priceItem, quantityItem);
        return item;
    }


    public List<Product> getProducts() {
        List<Product> items = new ArrayList<>();
        moveToFirst();
        while (!isAfterLast()) {
            Product product = getProduct();
            items.add(product);
            moveToNext();
        }
        return items;
    }

    private boolean valid(String s) {
        return s != null && s.length() > 0;
    }
}