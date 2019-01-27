package com.example.ricindigus.inventarioapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
    TextView txtMontoPagado;
    Button btnGuardar;

    Uri contentUri = null;
    int _id = -1;
    String nombreProducto = "";
    int precioProducto = 0;
    int cantidadProducto = 0;

    String fecha = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_venta);

        txtNombre = findViewById(R.id.txtNombre);
        txtFecha = findViewById(R.id.txtFecha);

        edtDni = findViewById(R.id.txtDNI);
        txtMontoPagado = findViewById(R.id.txtMontoPagado);

        edtCantidad = findViewById(R.id.txtCantidad);
        btnGuardar = findViewById(R.id.btnGuardar);

        edtCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int cantidad = 0;
                if (!TextUtils.isEmpty(s)){
                    cantidad = Integer.parseInt(s.toString());
                }
                txtMontoPagado.setText(String.valueOf(cantidad * precioProducto));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

        int montoPagado = 0 ;
        if (!TextUtils.isEmpty(txtMontoPagado.getText().toString()))
            montoPagado =Integer.parseInt(txtMontoPagado.getText().toString());

        ContentValues contentValues = new ContentValues();
        contentValues.put(VentasEntry.COLUMN_VENTA_ID_PRODUCTO,_id);
        contentValues.put(VentasEntry.COLUMN_VENTA_NOMBRE_PRODUCTO,nombreProducto);
        contentValues.put(VentasEntry.COLUMN_VENTA_FECHA,fecha);
        contentValues.put(VentasEntry.COLUMN_VENTA_CANTIDAD,cantidad);
        contentValues.put(VentasEntry.COLUMN_VENTA_DNI,dni);
        contentValues.put(VentasEntry.COLUMN_VENTA_MONTO_PAGADO,montoPagado);


        // Insert a new pet into the provider, returning the content URI for the new pet.

        Uri newUri = getContentResolver().insert(VentasEntry.CONTENT_URI, contentValues);
        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, "Error al registrar",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            ContentValues cv = new ContentValues();
            cv.put(ProductosEntry.COLUMN_PRODUCTO_CANTIDAD,cantidadProducto-cantidad);
            int rowsUpdated = getContentResolver().update(Uri.withAppendedPath(ProductosEntry.CONTENT_URI,String.valueOf(_id)),cv,null,null);
            if (rowsUpdated != 0){
                Toast.makeText(this, "Venta registrada", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(this, "Error al registrar",
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
                ProductosEntry.COLUMN_PRODUCTO_PRECIO,
                ProductosEntry.COLUMN_PRODUCTO_CANTIDAD
        };
        return new CursorLoader(this,contentUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()){
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(ProductosEntry.COLUMN_PRODUCTO_NOMBRE);
            int idColumnIndex = cursor.getColumnIndex(ProductosEntry._ID);
            int precioColumnIndex = cursor.getColumnIndex(ProductosEntry.COLUMN_PRODUCTO_PRECIO);
            int cantidadColumnIndex = cursor.getColumnIndex(ProductosEntry.COLUMN_PRODUCTO_CANTIDAD);




            // Extract out the value from the Cursor for the given column index
            nombreProducto = cursor.getString(nameColumnIndex);
            _id = cursor.getInt(idColumnIndex);
            precioProducto = cursor.getInt(precioColumnIndex);
            cantidadProducto = cursor.getInt(cantidadColumnIndex);

            long currentTime = System.currentTimeMillis();
            txtNombre.setText(nombreProducto);
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
