package cl.azeda.www.azedaadministracion.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class NetworkLoaderPreferences {
    private Context context;
    private ProgressBar progressBar;

    public NetworkLoaderPreferences(Context context){
        this.context = context;
    }
    public NetworkLoaderPreferences(Context context, ProgressBar progressBar){
        this.context = context;
        this.progressBar = progressBar;
    }


    public void cargarMensajes(){
        String url = "https://azeda.cl/mensajes/leer_mensajes_android";
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Leer base de datos y pasar a shared prefs
                try {
                    JSONArray mensajes = response.getJSONArray("mensajes");
                    Log.v("mensajes", mensajes.toString());

                    for(int i = 0; i < mensajes.length(); i++){
                        JSONObject mensajeActual = mensajes.getJSONObject(i);
                        String strPrefs = "mensaje"+ mensajeActual.getString("id_mensaje");
                        SharedPreferences mensajesPrefs = context.getSharedPreferences(strPrefs, MODE_PRIVATE);
                        SharedPreferences.Editor editMensajesPrefs = mensajesPrefs.edit();

                        //Log.v("mensaje actual", mensajeActual.toString());
                        //Log.v("nombre mensaje actual" , mensajeActual.getString("nombre_mensaje"));

                        int idMensajeActual = mensajeActual.getInt("id_mensaje");
                        String nombreMensajeActual = mensajeActual.getString("nombre_mensaje");
                        String emailMensajeActual = mensajeActual.getString("email_mensaje");
                        String telefonoMensajeActual = mensajeActual.getString("telefono_mensaje");
                        String direccionMensajeActual = mensajeActual.getString("direccion_mensaje");
                        String horaMensajeActual = mensajeActual.getString("hora_mensaje");
                        String fechaMensajeActual = mensajeActual.getString("fecha_mensaje");
                        String textoMensajeActual = mensajeActual.getString("texto_mensaje");


                        editMensajesPrefs.putInt("id_mensaje", idMensajeActual);
                        editMensajesPrefs.putString("nombre_mensaje", nombreMensajeActual);
                        editMensajesPrefs.putString("email_mensaje", emailMensajeActual);
                        editMensajesPrefs.putString("telefono_mensaje", telefonoMensajeActual);
                        editMensajesPrefs.putString("direccion_mensaje", direccionMensajeActual);
                        editMensajesPrefs.putString("hora_mensaje", horaMensajeActual);
                        editMensajesPrefs.putString("fecha_mensaje", fechaMensajeActual);
                        editMensajesPrefs.putString("texto_mensaje", textoMensajeActual);
                        editMensajesPrefs.apply();
                    }

                    SharedPreferences pf = context.getSharedPreferences("cantidad_mensajes", MODE_PRIVATE);
                    SharedPreferences.Editor pfEditor = pf.edit();
                    pfEditor.putInt("cantidad", mensajes.length());
                    pfEditor.apply();

                }catch(JSONException e){
                    e.printStackTrace();
                }

                //OCULTAR PROGRESSBAR
               progressBar.setVisibility(View.GONE);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               progressBar.setVisibility(View.GONE);
                error.printStackTrace();
                Log.e("error volley", error.toString());
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }
}
