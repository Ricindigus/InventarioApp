package com.example.ricindigus.inventarioapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.ricindigus.inventarioapp.data.InventarioContract.VentasEntry;

public class VentasActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    VentasCursorAdapter ventasCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        ListView listView = findViewById(R.id.listaVentas);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        ventasCursorAdapter = new VentasCursorAdapter(this,null);
        listView.setAdapter(ventasCursorAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getLoaderManager().initLoader(2,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                VentasEntry._ID,
                VentasEntry.COLUMN_VENTA_NOMBRE_PRODUCTO,
                VentasEntry.COLUMN_VENTA_CANTIDAD,
                VentasEntry.COLUMN_VENTA_MONTO_PAGADO,
                VentasEntry.COLUMN_VENTA_FECHA
        };
        return new CursorLoader(this,VentasEntry.CONTENT_URI,projection,
                null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ventasCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ventasCursorAdapter.swapCursor(null);
    }
}
