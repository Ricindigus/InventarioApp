package com.example.ricindigus.inventarioapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URISyntaxException;

public final class InventarioContract{

    public static final String CONTENT_AUTHORITY = "com.example.ricindigus.inventarioapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PRODUCTOS = "productos";
    public static final String PATH_VENTAS = "ventas";



    public InventarioContract() {
    }

    public static final class ProductosEntry{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTOS);

        public final static String TABLE_NAME = "productos";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCTO_NOMBRE = "nombre";
        public static final String COLUMN_PRODUCTO_CANTIDAD = "cantidad";
        public static final String COLUMN_PRODUCTO_PRECIO = "precio";
        public static final String COLUMN_PRODUCTO_PROVEDOR = "provedor";

        public static final String SQL_CREATE_PRODUCTOS_TABLE =
                "CREATE TABLE " + ProductosEntry.TABLE_NAME + " (" +
                        ProductosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ProductosEntry.COLUMN_PRODUCTO_NOMBRE + " TEXT NOT NULL, " +
                        ProductosEntry.COLUMN_PRODUCTO_CANTIDAD + " INTEGER NOT NULL DEFAULT 0, " +
                        ProductosEntry.COLUMN_PRODUCTO_PRECIO + " INTEGER NOT NULL DEFAULT 0, " +
                        ProductosEntry.COLUMN_PRODUCTO_PROVEDOR + " TEXT NOT NULL" +
                        ");";
    }

    public static final class VentasEntry{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VENTAS);

        public final static String TABLE_NAME = "ventas";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_VENTA_ID_PRODUCTO = "id_producto";
        public static final String COLUMN_VENTA_NOMBRE_PRODUCTO = "nombre_producto";
        public static final String COLUMN_VENTA_FECHA = "fecha";
        public static final String COLUMN_VENTA_MONTO_PAGADO = "monto_pagado";
        public static final String COLUMN_VENTA_DNI = "dni";
        public static final String COLUMN_VENTA_CANTIDAD = "cantidad";

        public static final String SQL_CREATE_VENTAS_TABLE =
                "CREATE TABLE " + VentasEntry.TABLE_NAME + " (" +
                        ProductosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        VentasEntry.COLUMN_VENTA_ID_PRODUCTO + " INTEGER NOT NULL, " +
                        VentasEntry.COLUMN_VENTA_NOMBRE_PRODUCTO + " TEXT NOT NULL, " +
                        VentasEntry.COLUMN_VENTA_DNI + " INTEGER NOT NULL, " +
                        VentasEntry.COLUMN_VENTA_MONTO_PAGADO + " INTEGER NOT NULL DEFAULT 0, " +
                        VentasEntry.COLUMN_VENTA_FECHA + " TEXT NOT NULL, " +
                        VentasEntry.COLUMN_VENTA_CANTIDAD + " INTEGER NOT NULL" +
                        ");";
    }


}
