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
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ricindigus.inventarioapp.data.InventarioContract.ProductosEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    EditText edtNombre;
    EditText edtCantidad;
    EditText edtProveedor;
    EditText edtPrecio;

    Uri contentUri = null;
    boolean campoCambio = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            campoCambio = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        edtNombre = findViewById(R.id.txtxNombre);
        edtCantidad = findViewById(R.id.txtCantidad);
        edtProveedor = findViewById(R.id.txtProveedor);
        edtPrecio = findViewById(R.id.txtPrecio);



        contentUri = getIntent().getData();
        if (contentUri == null){
            invalidateOptionsMenu();
            getSupportActionBar().setTitle("Insertar Producto");
        } else{
            getSupportActionBar().setTitle("Editar Producto");
            getLoaderManager().initLoader(0,null,this);
        }

        edtProveedor.setOnTouchListener(mTouchListener);
        edtCantidad.setOnTouchListener(mTouchListener);
        edtNombre.setOnTouchListener(mTouchListener);
        edtPrecio.setOnTouchListener(mTouchListener);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (contentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                if(verificarCampos()) saveProducto();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!campoCambio) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean verificarCampos() {
        if (TextUtils.isEmpty(edtNombre.getText().toString())
                && TextUtils.isEmpty(edtCantidad.getText().toString())
                && TextUtils.isEmpty(edtPrecio.getText().toString())
                && TextUtils.isEmpty(edtProveedor.getText().toString()))
            return false;
        return true;
    }

    private void deleteProducto() {
        int rowsAffected = getContentResolver().delete(contentUri, null,null);
        if (rowsAffected != 0)
            Toast.makeText(this, "borrado!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "error al borrar", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void saveProducto(){

        String nombre = edtNombre.getText().toString();
        String proveedor = edtProveedor.getText().toString();

        int cantidad = 0;
        if (!TextUtils.isEmpty(edtCantidad.getText().toString()))
            cantidad =Integer.parseInt(edtCantidad.getText().toString());

        int precio = 0;
        if (!TextUtils.isEmpty(edtPrecio.getText().toString()))
            precio =Integer.parseInt(edtPrecio.getText().toString());


        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductosEntry.COLUMN_PRODUCTO_NOMBRE,nombre);
        contentValues.put(ProductosEntry.COLUMN_PRODUCTO_PROVEDOR,proveedor);
        contentValues.put(ProductosEntry.COLUMN_PRODUCTO_CANTIDAD,cantidad);
        contentValues.put(ProductosEntry.COLUMN_PRODUCTO_PRECIO,precio);


        if (contentUri == null){
            Uri newUri = getContentResolver().insert(ProductosEntry.CONTENT_URI, contentValues);
            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Error al insertar", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "Producto insertado", Toast.LENGTH_SHORT).show();
            }
        }else{
            int rowsAffected = getContentResolver().update(contentUri, contentValues,null,null);
            if (rowsAffected != 0)
                Toast.makeText(this, "Actualizacion correcta", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!campoCambio) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Borrar Producto");
        builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteProducto();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
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

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Salir sin guardar?");
        builder.setPositiveButton("Sí", discardButtonClickListener);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductosEntry._ID,
                ProductosEntry.COLUMN_PRODUCTO_NOMBRE,
                ProductosEntry.COLUMN_PRODUCTO_CANTIDAD,
                ProductosEntry.COLUMN_PRODUCTO_PROVEDOR,
                ProductosEntry.COLUMN_PRODUCTO_PRECIO
        };
        return new CursorLoader(this,contentUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()){
            // Find the columns of pet attributes that we're interested in
            int nombreColumnIndex = cursor.getColumnIndex(ProductosEntry.COLUMN_PRODUCTO_NOMBRE);
            int cantidadColumnIndex = cursor.getColumnIndex(ProductosEntry.COLUMN_PRODUCTO_CANTIDAD);
            int proveedorColumnIndex = cursor.getColumnIndex(ProductosEntry.COLUMN_PRODUCTO_PROVEDOR);
            int precioColumnIndex = cursor.getColumnIndex(ProductosEntry.COLUMN_PRODUCTO_PRECIO);


            // Extract out the value from the Cursor for the given column index
            String nombre = cursor.getString(nombreColumnIndex);
            String proveedor = cursor.getString(proveedorColumnIndex);
            int cantidad = cursor.getInt(cantidadColumnIndex);
            int precio = cursor.getInt(precioColumnIndex);

            edtNombre.setText(nombre);
            edtProveedor.setText(proveedor);
            edtCantidad.setText(Integer.toString(cantidad));
            edtPrecio.setText(Integer.toString(precio));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        edtNombre.setText("");
        edtCantidad.setText("");
        edtProveedor.setText("");
    }
}
