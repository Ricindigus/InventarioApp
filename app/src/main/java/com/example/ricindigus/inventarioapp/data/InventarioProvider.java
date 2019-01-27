package com.example.ricindigus.inventarioapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.example.ricindigus.inventarioapp.data.InventarioContract.ProductosEntry;
import com.example.ricindigus.inventarioapp.data.InventarioContract.VentasEntry;


public class InventarioProvider extends ContentProvider {

    private InventarioDbHelper inventarioDbHelper;

    private static final int PRODUCTOS = 100;
    private static final int PRODUCTO_ID = 101;

    private static final int VENTAS = 200;
    private static final int VENTA_ID = 201;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(InventarioContract.CONTENT_AUTHORITY,InventarioContract.PATH_PRODUCTOS,PRODUCTOS);
        mUriMatcher.addURI(InventarioContract.CONTENT_AUTHORITY,InventarioContract.PATH_PRODUCTOS+"/#",PRODUCTO_ID);

        mUriMatcher.addURI(InventarioContract.CONTENT_AUTHORITY,InventarioContract.PATH_VENTAS,VENTAS);
        mUriMatcher.addURI(InventarioContract.CONTENT_AUTHORITY,InventarioContract.PATH_VENTAS+"/#",VENTA_ID);
    }




    @Override
    public boolean onCreate() {
        inventarioDbHelper = new InventarioDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase sqLiteDatabase = inventarioDbHelper.getWritableDatabase();

        Cursor cursor = null;

        int match = mUriMatcher.match(uri);

        switch (match){
            case PRODUCTOS:
                cursor = sqLiteDatabase.query(ProductosEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PRODUCTO_ID:
                // Crea la consulta where =?
                selection = ProductosEntry._ID + "=?";
                // Obtiene el ID del Uri
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                // Hace la query
                cursor = sqLiteDatabase.query(ProductosEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case VENTAS:
                cursor = sqLiteDatabase.query(VentasEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case VENTA_ID:
                // Crea la consulta where =?
                selection = VentasEntry._ID + "=?";
                // Obtiene el ID del Uri
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                // Hace la query
                cursor = sqLiteDatabase.query(VentasEntry.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default: throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCTOS:
                return insertProducto(uri, values);
            case VENTAS:
                return insertVenta(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProducto(Uri uri, ContentValues values) {
        // Verifica nombre no nulo
        String name = values.getAsString(ProductosEntry.COLUMN_PRODUCTO_NOMBRE);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        // Verifica cantidad no cero
        Integer cantidad = values.getAsInteger(ProductosEntry.COLUMN_PRODUCTO_CANTIDAD);
        if (cantidad != null && cantidad < 0) {
            throw new IllegalArgumentException("Se requiere indicar la cantidad");
        }

        // Verifica precio no cero
        Integer precio = values.getAsInteger(ProductosEntry.COLUMN_PRODUCTO_PRECIO);
        if (precio != null && precio <= 0) {
            throw new IllegalArgumentException("Se requiere indicar el precio");
        }

        // Verifica el provedor no nulo
        String provedor = values.getAsString(ProductosEntry.COLUMN_PRODUCTO_PROVEDOR);
        if (provedor == null) {
            throw new IllegalArgumentException("Se requiere correo provedor");
        }

        SQLiteDatabase database = inventarioDbHelper.getReadableDatabase();
        long id = database.insert(ProductosEntry.TABLE_NAME,null,values);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it

        // notifica los cambios despues de la insercion
        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertVenta(Uri uri, ContentValues values) {
        // Verifica nombre no nulo
        String id_producto = values.getAsString(VentasEntry.COLUMN_VENTA_ID_PRODUCTO);
        if (id_producto == null) {
            throw new IllegalArgumentException("FALTA EL ID DEL PRODUCTO VENDIDO");
        }

        String fecha = values.getAsString(VentasEntry.COLUMN_VENTA_FECHA);
        if (fecha == null) {
            throw new IllegalArgumentException("falta la fecha de venta");
        }
        // Verifica cantidad no cero
        Integer cantidad = values.getAsInteger(VentasEntry.COLUMN_VENTA_CANTIDAD);
        if (cantidad != null && cantidad < 0) {
            throw new IllegalArgumentException("Se requiere indicar la cantidad");
        }

        // Verifica cantidad no cero
        Integer montoPagado = values.getAsInteger(VentasEntry.COLUMN_VENTA_MONTO_PAGADO);
        if (montoPagado != null && montoPagado <= 0) {
            throw new IllegalArgumentException("Se requiere indicar el monto pagado");
        }

        // Verifica el provedor no nulo
        Integer dni = values.getAsInteger(VentasEntry.COLUMN_VENTA_DNI);
        if (dni == null || (dni!=null && dni < 10000000)) {
            throw new IllegalArgumentException("DNI INCORRECTO");
        }

        SQLiteDatabase database = inventarioDbHelper.getReadableDatabase();
        long id = database.insert(VentasEntry.TABLE_NAME,null,values);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it

        // notifica los cambios despues de la insercion
        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCTOS:
                return deleteProducto(ProductosEntry.TABLE_NAME,uri, selection,selectionArgs);
            case PRODUCTO_ID:
                selection = ProductosEntry._ID + "=?";
                // Obtiene el ID del Uri
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return deleteProducto(ProductosEntry.TABLE_NAME,uri, selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    public int deleteProducto(String tableName, Uri uri, String selection, String[] selectionArgs){
        SQLiteDatabase sqLiteDatabase = inventarioDbHelper.getWritableDatabase();
        int rowsDeleted = sqLiteDatabase.delete(tableName,selection,selectionArgs);
        if (rowsDeleted != 0) getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCTO_ID:
                selection = ProductosEntry._ID + "=?";
                // Obtiene el ID del Uri
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateProducto(uri,values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private int updateProducto(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        if (values.size() == 0) {
            return 0;
        }

        if (values.containsKey(ProductosEntry.COLUMN_PRODUCTO_NOMBRE)){
            // Verifica nombre no nulo
            String name = values.getAsString(ProductosEntry.COLUMN_PRODUCTO_NOMBRE);
            if (name == null) {
                throw new IllegalArgumentException("Se requiere un  nombre");
            }
        }

        if (values.containsKey(ProductosEntry.COLUMN_PRODUCTO_CANTIDAD)){
            // Verifica cantidad no cero
            Integer cantidad = values.getAsInteger(ProductosEntry.COLUMN_PRODUCTO_CANTIDAD);
            if (cantidad != null && cantidad < 0) {
                throw new IllegalArgumentException("Se requiere indicar la cantidad");
            }
        }

        if (values.containsKey(ProductosEntry.COLUMN_PRODUCTO_PRECIO)){
            // Verifica cantidad no cero
            Integer precio = values.getAsInteger(ProductosEntry.COLUMN_PRODUCTO_CANTIDAD);
            if (precio != null && precio <= 0) {
                throw new IllegalArgumentException("Se requiere indicar el precio");
            }
        }

        if (values.containsKey(ProductosEntry.COLUMN_PRODUCTO_PROVEDOR)){
            // Verifica el provedor no nulo
            String provedor = values.getAsString(ProductosEntry.COLUMN_PRODUCTO_PROVEDOR);
            if (provedor == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }


        SQLiteDatabase sqLiteDatabase = inventarioDbHelper.getWritableDatabase();
        int rowsAffected = sqLiteDatabase.update(ProductosEntry.TABLE_NAME,values,selection,selectionArgs);
        if (rowsAffected != 0) getContext().getContentResolver().notifyChange(uri,null);
        return rowsAffected;
    }
}
