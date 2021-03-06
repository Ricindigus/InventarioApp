package com.example.ricindigus.inventarioapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.ricindigus.inventarioapp.data.InventarioContract;

public class ProductoCursorAdapter extends CursorAdapter {

    public ProductoCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.producto_item,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtNombre = view.findViewById(R.id.nombre);
        TextView txtCantidad = view.findViewById(R.id.cantidad);
        TextView txtPrecio = view.findViewById(R.id.precio);


        txtNombre.setText(cursor.getString(cursor.getColumnIndex(InventarioContract.ProductosEntry.COLUMN_PRODUCTO_NOMBRE)));
        int cantidad = cursor.getInt(cursor.getColumnIndex(InventarioContract.ProductosEntry.COLUMN_PRODUCTO_CANTIDAD));
        int precio = cursor.getInt(cursor.getColumnIndex(InventarioContract.ProductosEntry.COLUMN_PRODUCTO_PRECIO));

        txtCantidad.setText("Cantidad disponible: " + String.valueOf(cantidad) + " unidades");
        txtPrecio.setText("Precio unitario: S/." + String.valueOf(precio) + ".00");
    }
}
