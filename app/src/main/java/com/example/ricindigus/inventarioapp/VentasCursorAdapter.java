package com.example.ricindigus.inventarioapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.ricindigus.inventarioapp.data.InventarioContract.VentasEntry;

public class VentasCursorAdapter extends CursorAdapter {

    public VentasCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.venta_item,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtNombre = view.findViewById(R.id.nombre);
        TextView txtCantidad = view.findViewById(R.id.cantidad);
        TextView txtMontoPagado = view.findViewById(R.id.monto);
        TextView txtFecha = view.findViewById(R.id.fecha);



        txtNombre.setText(cursor.getString(cursor.getColumnIndex(VentasEntry.COLUMN_VENTA_NOMBRE_PRODUCTO)));
        txtFecha.setText("Fecha de compra: " + cursor.getString(cursor.getColumnIndex(VentasEntry.COLUMN_VENTA_FECHA)));
        int cantidad = cursor.getInt(cursor.getColumnIndex(VentasEntry.COLUMN_VENTA_CANTIDAD));
        txtCantidad.setText("Catidad comprada:" + String.valueOf(cantidad));
        int montoPagado = cursor.getInt(cursor.getColumnIndex(VentasEntry.COLUMN_VENTA_MONTO_PAGADO));
        txtMontoPagado.setText("Monto pagado: S/." + String.valueOf(montoPagado)+".00");
    }
}
