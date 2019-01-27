package com.example.ricindigus.inventarioapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventarioDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "inventario.db";
    public static final int DATABASE_VERSION = 1;

    public InventarioDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(InventarioContract.ProductosEntry.SQL_CREATE_PRODUCTOS_TABLE);
        sqLiteDatabase.execSQL(InventarioContract.VentasEntry.SQL_CREATE_VENTAS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
