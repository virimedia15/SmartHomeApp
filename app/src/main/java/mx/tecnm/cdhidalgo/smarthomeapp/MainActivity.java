package mx.tecnm.cdhidalgo.smarthomeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    //Variables
    EditText etUser, etPass;
    Button bInicio;
    //almacenar token
    SharedPreferences sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //enlasamos las variables
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        bInicio = findViewById(R.id.bInicio);
        //archivo xml funciona como json para guardar cosas
        sesion = getSharedPreferences("sesion", 0);

        //Codigo boton
        bInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clic en boton mandamos a llamar la funcion login
                login();
            }
        });

    }
     //alt enter crea esto
    private void login() {
        //construir peticion para get del token
        String url = Uri.parse(Config.URL + "login.php")
                .buildUpon()
                //parametros nombre de cable
                .appendQueryParameter("user", etUser.getText().toString())
                .appendQueryParameter("pass", etPass.getText().toString())
                .build().toString();

        JsonObjectRequest peticion = new  JsonObjectRequest(Request.Method.GET, url, null,
        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                respuesta(response);

            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(peticion);



    }
    private void respuesta(JSONObject response){
        try {
            if (response.getString("login").compareTo("y") == 0) {
                String jwt = response.getString("token");
                SharedPreferences.Editor editor = sesion.edit();
                //nomre de variable y valor que lo tiene
                editor.putString("user",etUser.getText().toString() );
                editor.putString("token", jwt);
                //mandamos a llamr al activity2
                editor.commit();
                startActivity(new Intent(this, MainActivity2.class));

            }else{
                Toast.makeText(this, "Error de usuario o contraseña",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }
    }
}