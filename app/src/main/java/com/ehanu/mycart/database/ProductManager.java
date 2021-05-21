package com.ehanu.mycart.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.ehanu.mycart.model.Product;

import java.util.List;

public class ProductManager {
    private static ProductManager instance;
    private static final String INSERT="INSERT INTO "+
            DbSchema.ProductTable.NAME +"("+DbSchema.ProductTable.Cols.NAME+")" +
            " VALUES (?)";
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
        String sql="SELECT*FROM "+DbSchema.ProductTable.NAME;
        Cursor cursor = db.rawQuery(sql, null);
        ProductCursorWrapper noteCursorWrapper = new ProductCursorWrapper(cursor);
        return noteCursorWrapper.getProducts();
    }

    public Product findProductById(Long id){
        String sql="SELECT * FROM "+ DbSchema.ProductTable.NAME
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

        contentValues.put(DbSchema.ProductTable.Cols.QUANTITY, quantity);
        db.update(DbSchema.ProductTable.NAME, contentValues,"id = ?", new String[]{idString});
        return true;
    }

    public boolean add(Product product){
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Create a new map of values, where column names are the keys
        values.put(DbSchema.ProductTable.Cols.ID, product.getId());
        values.put(DbSchema.ProductTable.Cols.NAME, product.getName());
        values.put(DbSchema.ProductTable.Cols.IMG_URL, product.getImgUrl());
        values.put(DbSchema.ProductTable.Cols.PRICE, product.getPrice());
        values.put(DbSchema.ProductTable.Cols.QUANTITY, product.getQuantity());


        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DbSchema.ProductTable.NAME, null, values);

        return true;
    }

    public boolean delete(Long id){
        int result = db.delete(DbSchema.ProductTable.NAME, "id= ?", new String[]{id+""});
        return result>0;
    }
}