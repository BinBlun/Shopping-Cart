package a2_1901040003.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import a2_1901040003.model.Product;

import java.util.List;

public class ProductManager {
    private static ProductManager instance;
    //    private static final String INSERT="INSERT INTO "+
//            DbSchema.ProductTable.NAME_TABLE +"("+DbSchema.ProductTable.Cols.NAME_TABLE+")" +
//            " VALUES (?)";
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    public static ProductManager getInstance(Context context){
        if(instance==null){
            instance=new ProductManager(context);

        }
        return instance;
    }

    private ProductManager(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public DbHelper getDbHelper(){
        return this.dbHelper;
    }

    /* public SQLiteDatabase getSQLiteDatabase(){
         return this.getSQLiteDatabase();
     }*/
    public List<Product> all(){
        String sql="SELECT*FROM "+DbSchema.ProductTable.NAME_TABLE;
        Cursor cursor = db.rawQuery(sql, null);
        ProductCursorWrapper noteCursorWrapper = new ProductCursorWrapper(cursor);
        return noteCursorWrapper.getProducts();
    }

    public Product findProductById(Long id){
        String sql="SELECT * FROM "+ DbSchema.ProductTable.NAME_TABLE
                +" WHERE id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{id+""});
        ProductCursorWrapper noteCursorWrapper=new ProductCursorWrapper(cursor);
        return noteCursorWrapper.getAProduct();
    }

    public boolean update(Product product){
        ContentValues contentValues = new ContentValues();

        int id = product.getId();
        String idString=String.valueOf(id);

        int quantity = product.getQuantity();

        contentValues.put(DbSchema.ProductTable.Cols.QUANTITY_ITEM, quantity);
        db.update(DbSchema.ProductTable.NAME_TABLE, contentValues,"id = ?", new String[]{idString});
        return true;
    }

    public boolean add(Product product){
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Product productInDb = findProductById((long) product.getId());
        if (productInDb != null){
            int quantity = productInDb.getQuantity();
            productInDb.setQuantity(quantity + 1);
            update(productInDb);
            return true;
        }

        ContentValues values = new ContentValues();

        // Create a new map of values, where column names are the keys
        values.put(DbSchema.ProductTable.Cols.ID, product.getId());
        values.put(DbSchema.ProductTable.Cols.NAME_ITEM, product.getName());
        values.put(DbSchema.ProductTable.Cols.IMAGE_URL, product.getImageUrl());
        values.put(DbSchema.ProductTable.Cols.PRICE_ITEM, product.getPrice());
        values.put(DbSchema.ProductTable.Cols.QUANTITY_ITEM, product.getQuantity() + 1);


        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DbSchema.ProductTable.NAME_TABLE, null, values);

        return true;
    }

    public boolean delete(Long id){
        int result = db.delete(DbSchema.ProductTable.NAME_TABLE, "id= ?", new String[]{id+""});
        return result>0;
    }

    public boolean deleteAll(){
        db.delete(DbSchema.ProductTable.NAME_TABLE, null, null);
        return true;
    }
}
