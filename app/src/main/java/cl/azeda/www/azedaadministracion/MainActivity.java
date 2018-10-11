package cl.azeda.www.azedaadministracion;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cl.azeda.www.azedaadministracion.Network.CustomRequest;
import cl.azeda.www.azedaadministracion.Network.MySingleton;

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
    }

    private View.OnClickListener tokenListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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
                            //guardarTokenEnDB(token);
                            // Log and toast

                            Log.e("TAG", token);
                            Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    };

    private void guardarTokenEnDB(String token){
        String url = "";
        Map<String, String> params = new HashMap<>();
        params.put("token", token);

        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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
