package cl.azeda.www.azedaadministracion.Activities;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import cl.azeda.www.azedaadministracion.Adapters.MensajeAdapter;
import cl.azeda.www.azedaadministracion.Models.Mensaje;
import cl.azeda.www.azedaadministracion.Network.NetworkLoaderPreferences;
import cl.azeda.www.azedaadministracion.R;

public class MensajesActivity extends AppCompatActivity {
    private RecyclerView recyclerMensajes;
    private RecyclerView.Adapter adapterMensajes;
    private RecyclerView.LayoutManager layoutManagerMensajes;
    private List<Mensaje> listaMensajes;
    private ProgressBar progressMensajes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);
        //ASOCIAR EL PROGRESSBAR
        progressMensajes = (ProgressBar)findViewById(R.id.progress_mensajes);
        //CONFIGURAR EL RECYCLERVIEW
        recyclerMensajes = (RecyclerView)findViewById(R.id.recycler_view_mensajes);
        recyclerMensajes.setHasFixedSize(true);
        NetworkLoaderPreferences loader = new NetworkLoaderPreferences(MensajesActivity.this, progressMensajes);
        loader.cargarMensajes();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                leerMensajes();
            }
        },2000);
    }

    //Hacer peticion de todos los
    private void leerMensajes(){
        listaMensajes = new ArrayList<Mensaje>();
        SharedPreferences prefs = getSharedPreferences("cantidad_mensajes", MODE_PRIVATE);
        int cantidadMensajes = prefs.getInt("cantidad", 0);

        Log.e("cantidad de mensajes", String.valueOf(cantidadMensajes));
        for(int i = 0; i < cantidadMensajes; i++){
            String strpf = "mensaje"+String.valueOf(cantidadMensajes - i);
            SharedPreferences mensajePref = getSharedPreferences(strpf, MODE_PRIVATE);
            int idMensajeActual = mensajePref.getInt("id_mensaje", 0);
            String nombreMensajeActual = mensajePref.getString("nombre_mensaje", "nombre por defecto");
            String emailMensajeActual = mensajePref.getString("email_mensaje", "email por defecto");
            String telefonoMensajeActual = mensajePref.getString("telefono_mensaje", "telefono por defecto");
            String direccionMensajeActual = mensajePref.getString("direccion_mensaje", "direccion por defecto");
            String horaMensajeActual = mensajePref.getString("hora_mensaje", "00:00");
            String fechaMensajeActual = mensajePref.getString("fecha_mensaje", "00-00-0000");
            String textoMensajeActual = mensajePref.getString("texto_mensaje", "texto por defecto");
            listaMensajes.add(new Mensaje(idMensajeActual, nombreMensajeActual, emailMensajeActual,
                    telefonoMensajeActual, direccionMensajeActual, horaMensajeActual, fechaMensajeActual, textoMensajeActual));
        }
        layoutManagerMensajes = new LinearLayoutManager(MensajesActivity.this);
        recyclerMensajes.setLayoutManager(layoutManagerMensajes);
        adapterMensajes = new MensajeAdapter(listaMensajes);
        recyclerMensajes.setAdapter(adapterMensajes);
    }
}
