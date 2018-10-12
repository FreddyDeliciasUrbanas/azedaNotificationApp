package cl.azeda.www.azedaadministracion.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cl.azeda.www.azedaadministracion.Network.CustomRequest;
import cl.azeda.www.azedaadministracion.Network.MySingleton;
import cl.azeda.www.azedaadministracion.R;

public class MainActivity extends AppCompatActivity {
    private Button btnMensajes;
    private Button btnToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMensajes = (Button)findViewById(R.id.btn_mensajes);
        btnToken = (Button)findViewById(R.id.btn_token);
        btnToken.setOnClickListener(tokenListener);
        btnMensajes.setOnClickListener(mensajesListener);
        initComprobarToken();
    }

    private View.OnClickListener tokenListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            initGuardarToken();
        }
    };

    private View.OnClickListener mensajesListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, MensajesActivity.class);
            startActivity(i);
        }
    };

    private void initGuardarToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        guardarTokenEnDB(token);
                        Log.e("TAG", token);
                    }
                });
    }

    private void initComprobarToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        comprobarToken(token);
                        // Log and toast
                        Log.e("TAG", token);
                    }
                });
    }

    private void guardarTokenEnDB(String token){
        String url = "https://azeda.cl/mensajes/guardar_token";
        Map<String, String> params = new HashMap<>();
        params.put("token", token);

        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    String status = response.getString("status");
                    if(status.equals("ok")){
                        btnToken.setEnabled(false);
                        btnToken.setOnClickListener(null);
                        btnToken.setText("Conectado con azeda notificaciones");
                        Toast.makeText(MainActivity.this, "Token guardado en base de datos AZEDA", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(MainActivity.this, "hubo un error al intentar conectarse a la base de datos", Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void comprobarToken (String token){
        String url = "https://azeda.cl/mensajes/comprobar_token";
        Map<String, String> params = new HashMap<>();
        params.put("token", token);

        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    String status = response.getString("status");
                    if(status.equals("ok")){
                        btnToken.setEnabled(false);
                        btnToken.setOnClickListener(null);
                        btnToken.setText("Conectado con azeda notificaciones");
                        Toast.makeText(MainActivity.this, "El token ya se encuentra registrado en la base de datos AZEDA", Toast.LENGTH_LONG).show();
                    }else{
                        btnToken.setEnabled(true);
                        btnToken.setOnClickListener(tokenListener);
                        Toast.makeText(MainActivity.this, "Debe registrar este dispositivo", Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
}
