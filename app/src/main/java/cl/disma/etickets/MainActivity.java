package cl.disma.etickets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    EditText txtid,txtfecha,txtreferencia,txtdescripcion,txtestado,txtsolicitadopor;
    Button btnAgregar,btnEditar,btnEliminar,btnBuscar;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtid=(EditText)findViewById(R.id.txtid);
        txtfecha=(EditText)findViewById(R.id.txtfecha);
        txtreferencia=(EditText)findViewById(R.id.txtreferencia);
        txtdescripcion=(EditText)findViewById(R.id.txtdescripcion);
        txtestado=(EditText)findViewById(R.id.txtestado);
        txtsolicitadopor=(EditText)findViewById(R.id.txtsolicitadopor);
        btnAgregar=(Button)findViewById(R.id.btnAgregar) ;
        btnBuscar=(Button)findViewById(R.id.btnBuscar) ;
        btnEditar=(Button)findViewById(R.id.btnEditar) ;
        btnEliminar=(Button)findViewById(R.id.btnEliminar) ;

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  ejecutarServicio("http://www.iqcontapro.cl/Tickets/android/insertar_ticket.php");
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarTicket("http://www.iqcontapro.cl/Tickets/android/buscar_ticket.php?id="+txtid.getText());
            }
        });
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServicio("http://www.iqcontapro.cl/Tickets/android/update_ticket.php");
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarServicio("http://www.iqcontapro.cl/Tickets/android/eliminar_ticket.php");
            }
        });

    }

    private void ejecutarServicio(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Operacion Exitosa", Toast.LENGTH_SHORT).show();
               limpiar();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("id",txtid.getText().toString());
                parametros.put("fecha",txtfecha.getText().toString());
                parametros.put("referencia",txtreferencia.getText().toString());
                parametros.put("descripcion",txtdescripcion.getText().toString());
                parametros.put("estado",txtestado.getText().toString());
                parametros.put("solicitadopor",txtsolicitadopor.getText().toString());
                return parametros;

            }
        };
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void eliminarServicio(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                limpiar();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("id",txtid.getText().toString());
                return parametros;

            }
        };
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


     private void limpiar(){
        txtid.setText("");
         txtfecha.setText("");
         txtreferencia.setText("");
         txtdescripcion.setText("");
         txtestado.setText("");
         txtsolicitadopor.setText("");


    }


    private void buscarTicket(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject=null;
                for(int i=0;i<response.length();i++){
                    try{
                       jsonObject=response.getJSONObject(i);
                       txtfecha.setText(jsonObject.getString("fecha"));
                        txtreferencia.setText(jsonObject.getString("referencia"));
                        txtdescripcion.setText(jsonObject.getString("descripcion"));
                        txtestado.setText(jsonObject.getString("estado"));
                        txtsolicitadopor.setText(jsonObject.getString("solicitadopor"));

                    }catch (JSONException e){
                        Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener(){
          @Override
          public void onErrorResponse(VolleyError error){
              Toast.makeText(getApplicationContext(),"Error Conexion",Toast.LENGTH_SHORT).show();
          }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

}