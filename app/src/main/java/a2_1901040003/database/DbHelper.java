package a2_1901040003.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "cart.db";
    public static final int DATABASE_VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DbSchema.ProductTable.NAME_TABLE + "("
                + DbSchema.ProductTable.Cols.ID + " INT, "
                + DbSchema.ProductTable.Cols.NAME_ITEM + " TEXT,"
                + DbSchema.ProductTable.Cols.IMAGE_URL + " TEXT,"
                + DbSchema.ProductTable.Cols.PRICE_ITEM + " INT,"
                + DbSchema.ProductTable.Cols.QUANTITY_ITEM + " INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        //Drop existing table
        database.execSQL("DROP TABLE IF EXISTS " + DbSchema.ProductTable.NAME_TABLE);
        //Create a new one
        onCreate(database);
    }
}


