package cl.azeda.www.azedaadministracion.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cl.azeda.www.azedaadministracion.Adapters.MensajeAdapter;
import cl.azeda.www.azedaadministracion.Models.Mensaje;
import cl.azeda.www.azedaadministracion.Network.NetworkLoaderPreferences;
import cl.azeda.www.azedaadministracion.R;

public class MensajeActivity extends AppCompatActivity {

    private TextView tvNombreMensaje;
    private TextView tvEmailMensaje;
    private TextView tvTelefonoMensaje;
    private TextView tvDireccionMensaje;
    private TextView tvTextoMensaje;

    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String texto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);

        tvNombreMensaje = (TextView)findViewById(R.id.tv_nombre_mensaje);
        tvEmailMensaje = (TextView)findViewById(R.id.tv_email_mensaje);
        tvTelefonoMensaje = (TextView)findViewById(R.id.tv_telefono_mensaje);
        tvDireccionMensaje = (TextView)findViewById(R.id.tv_direccion_mensaje);
        tvTextoMensaje = (TextView)findViewById(R.id.tv_texto_mensaje);



        getMensaje();

        tvTelefonoMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri numero = Uri.parse("tel:"+telefono);
                Intent i = new Intent(Intent.ACTION_DIAL, numero);
                startActivity(i);
            }
        });

        tvEmailMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de sandwich gourmet");
                startActivity(emailIntent);
            }
        });

        tvDireccionMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[]direccionPedido = direccion.split(" ");
                Uri location = Uri.parse("geo:0,0?q=" + direccionPedido[0] + "+" + direccionPedido[1] + "+" + direccionPedido[2] + "+" + direccionPedido[3] );
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });

    }

    private void getMensaje(){
        //obtener el id del mensaje
        String idMensaje = getIntent().getStringExtra("id_mensaje");
        String strPrefs = "mensaje" + idMensaje;

        Log.e("ID MENSAJEEE", strPrefs);

        //buscar en las sharedPreferences y pasar a las vars
        SharedPreferences mensajesPrefs = getSharedPreferences(strPrefs, MODE_PRIVATE);
        nombre = mensajesPrefs.getString("nombre_mensaje","nombre por defecto");
        email = mensajesPrefs.getString("email_mensaje", "email por defecto");
        telefono = mensajesPrefs.getString("telefono_mensaje", "+56921212121");
        direccion = mensajesPrefs.getString("direccion_mensaje", "direccion por defecto");
        texto = mensajesPrefs.getString("texto_mensaje", "texto por defecto");

        //Enviar las vars a los textview
        tvNombreMensaje.setText(nombre);
        tvEmailMensaje.setText(email);
        tvTelefonoMensaje.setText(telefono);
        tvDireccionMensaje.setText(direccion);
        tvTextoMensaje.setText(texto);
    }
}
