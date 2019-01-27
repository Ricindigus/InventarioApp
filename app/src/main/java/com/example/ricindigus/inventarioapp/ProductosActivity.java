package com.example.ricindigus.inventarioapp;


import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ricindigus.inventarioapp.data.InventarioContract.ProductosEntry;

public class ProductosActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ProductoCursorAdapter productoCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);



        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductosActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView listView = findViewById(R.id.listaProductos);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        productoCursorAdapter = new ProductoCursorAdapter(this, null);
        listView.setAdapter(productoCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uriQuery = Uri.withAppendedPath(ProductosEntry.CONTENT_URI,String.valueOf(id));
                Intent intent = new Intent(ProductosActivity.this,RegistrarVentaActivity.class);
                intent.setData(uriQuery);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uriQuery = Uri.withAppendedPath(ProductosEntry.CONTENT_URI,String.valueOf(id));
                Intent intent = new Intent(ProductosActivity.this,EditorActivity.class);
                intent.setData(uriQuery);
                startActivity(intent);
                return true;
            }
        });


        getLoaderManager().initLoader(0,null,this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_productosos.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_productos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                showDeleteAllConfirmationDialog();
                return true;
            case R.id.action_display_ventas:
                Intent intent = new Intent(ProductosActivity.this,VentasActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Borrar todo");
        builder.setPositiveButton("borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAllPets();
            }
        });
        builder.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void insertData() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductosEntry.COLUMN_PRODUCTO_NOMBRE, "Lapicero");
        contentValues.put(ProductosEntry.COLUMN_PRODUCTO_CANTIDAD, 10);
        contentValues.put(ProductosEntry.COLUMN_PRODUCTO_PRECIO, 20);
        contentValues.put(ProductosEntry.COLUMN_PRODUCTO_PROVEDOR, "ricindigus@gmail.com");

        Uri newUri = getContentResolver().insert(ProductosEntry.CONTENT_URI, contentValues);
    }

    private void deleteAllPets() {
        int rowsAffected = getContentResolver().delete(ProductosEntry.CONTENT_URI, null,null);
        if (rowsAffected != 0)
            Toast.makeText(this, "elementos borrados!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Error al borrar", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductosEntry._ID,
                ProductosEntry.COLUMN_PRODUCTO_NOMBRE,
                ProductosEntry.COLUMN_PRODUCTO_CANTIDAD,
                ProductosEntry.COLUMN_PRODUCTO_PROVEDOR,
                ProductosEntry.COLUMN_PRODUCTO_PRECIO
        };
        return new CursorLoader(this,ProductosEntry.CONTENT_URI,projection,
                null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        productoCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productoCursorAdapter.swapCursor(null);
    }
}
