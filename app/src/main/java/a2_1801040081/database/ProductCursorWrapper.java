package a2_1801040081.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import a2_1801040081.model.Product;

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
    public Product getProduct(){
        Product product = null;

        int id = getInt(getColumnIndex(DbSchema.ProductTable.Cols.ID));
        String name = getString(getColumnIndex(DbSchema.ProductTable.Cols.NAME));
        String imgUrl = getString(getColumnIndex(DbSchema.ProductTable.Cols.IMG_URL));
        int price = getInt(getColumnIndex(DbSchema.ProductTable.Cols.PRICE));
        int quantity = getInt(getColumnIndex(DbSchema.ProductTable.Cols.QUANTITY));

        if(id != 0 && valid(name) && valid(imgUrl) && price > 0 && quantity > 0)
            product = new Product(id, imgUrl, name, price, quantity);
        return product;
    }

    public Product getAProduct(){
        moveToFirst();

        if(getCount()==0){
            return null;
        }

        Product product = null;

        int id = getInt(getColumnIndex(DbSchema.ProductTable.Cols.ID));
        String name = getString(getColumnIndex(DbSchema.ProductTable.Cols.NAME));
        String imgUrl = getString(getColumnIndex(DbSchema.ProductTable.Cols.IMG_URL));
        int price = getInt(getColumnIndex(DbSchema.ProductTable.Cols.PRICE));
        int quantity = getInt(getColumnIndex(DbSchema.ProductTable.Cols.QUANTITY));

        if(id != 0 && valid(name) && valid(imgUrl) && price > 0 && quantity > 0)
            product = new Product(id, imgUrl, name, price, quantity);
        return product;
    }


    public List<Product> getProducts(){
        List<Product> products = new ArrayList<>();
        moveToFirst();
        while(!isAfterLast()){
            Product product = getProduct();
            products.add(product);
            moveToNext();
        }
        return products;
    }

    private boolean valid(String string){
        return string != null && string.length() > 0;
    }
}