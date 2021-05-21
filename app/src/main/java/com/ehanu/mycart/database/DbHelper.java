package com.ehanu.mycart.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "cart.db";
    public static final int DB_VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DbSchema.ProductTable.NAME + "("
                    + DbSchema.ProductTable.Cols.ID + " INT, "
                    + DbSchema.ProductTable.Cols.NAME + " TEXT,"
                    + DbSchema.ProductTable.Cols.IMG_URL + " TEXT,"
                    + DbSchema.ProductTable.Cols.PRICE + " INT,"
                    + DbSchema.ProductTable.Cols.QUANTITY + " INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop existing table
        db.execSQL("DROP TABLE IF EXISTS "+DbSchema.ProductTable.NAME);
        //Create a new one
        onCreate(db);
    }
}
