package com.example.ricindigus.inventarioapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ricindigus.inventarioapp.data.InventarioContract.VentasEntry;
import com.example.ricindigus.inventarioapp.data.InventarioContract.ProductosEntry;


import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrarVentaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    TextView txtNombre;
    TextView txtFecha;
    EditText edtDni;
    EditText edtCantidad;
    Button btnGuardar;

    Uri contentUri = null;
    int _id = -1;
    String fecha = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_venta);

        txtNombre = findViewById(R.id.txtNombre);
        txtFecha = findViewById(R.id.txtFecha);
        edtDni = findViewById(R.id.txtDNI);
        edtCantidad = findViewById(R.id.txtCantidad);
        btnGuardar = findViewById(R.id.btnGuardar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Venta Producto");

        contentUri = getIntent().getData();


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarCampos()){
                    guardarVenta();
                    finish();
                } else Toast.makeText(RegistrarVentaActivity.this, "Faltan llenar campos", Toast.LENGTH_SHORT).show();
            }
        });

        getLoaderManager().initLoader(1,null,this);
    }

    private void guardarVenta() {
        String fecha = txtFecha.getText().toString();
        String dni = edtDni.getText().toString();
        int cantidad = 0 ;

        if (!TextUtils.isEmpty(edtCantidad.getText().toString()))
            cantidad =Integer.parseInt(edtCantidad.getText().toString());

        ContentValues contentValues = new ContentValues();
        contentValues.put(VentasEntry.COLUMN_VENTA_ID_PRODUCTO,_id);
        contentValues.put(VentasEntry.COLUMN_VENTA_FECHA,fecha);
        contentValues.put(VentasEntry.COLUMN_VENTA_CANTIDAD,cantidad);
        contentValues.put(VentasEntry.COLUMN_VENTA_DNI,dni);

        // Insert a new pet into the provider, returning the content URI for the new pet.

        Uri newUri = getContentResolver().insert(VentasEntry.CONTENT_URI, contentValues);
        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, "Error al registrar",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, "Venta registrada",
                    Toast.LENGTH_SHORT).show();
        }


    }

    private boolean verificarCampos() {
        if (TextUtils.isEmpty(txtFecha.getText().toString())
                && TextUtils.isEmpty(txtNombre.getText().toString())
                && TextUtils.isEmpty(edtDni.getText().toString())
                && TextUtils.isEmpty(edtCantidad.getText().toString()))
            return false;
        return true;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductosEntry._ID,
                ProductosEntry.COLUMN_PRODUCTO_NOMBRE,
        };
        return new CursorLoader(this,contentUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()){
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(ProductosEntry.COLUMN_PRODUCTO_NOMBRE);
            int idColumnIndex = cursor.getColumnIndex(ProductosEntry._ID);


            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            _id = cursor.getInt(idColumnIndex);
            long currentTime = System.currentTimeMillis();


            txtNombre.setText(name);
            txtFecha.setText(formatDate(new Date(currentTime)));
        }
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy, hh:mm:ss");
        return dateFormat.format(dateObject);
    }


    @Override
    public void onLoaderReset(Loader loader) {
        txtFecha.setText("");
        txtNombre.setText("");
        edtCantidad.setText("");
        edtDni.setText("");
    }

}
