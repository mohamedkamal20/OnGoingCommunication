package com.example.maged.ongoing;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;


// responsible for adding new place to the database
// first takes the input from the edit texts and on cliliking the add buttton it starts the service responsible for adding it

public class AddPlace extends ActionBarActivity {


    String status="";
    String url_addplace="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/addPlace";
    EditText place_name,place_long,place_lat,place_description;

    Button add_place;
    TextView adding_result ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        //------------------------------------------
        place_name = (EditText) findViewById(R.id.place_name);
        place_lat = (EditText) findViewById(R.id.place_latitude);
        place_long = (EditText) findViewById(R.id.place_Longtude);
        place_description = (EditText) findViewById(R.id.place_description);
        add_place = (Button) findViewById(R.id.add_new_place);
        adding_result = (TextView) findViewById(R.id.statues_id);

        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPlaceBotton() ;
            }
        });
    }
        // responsible for starting the volly to call the service
        void AddPlaceBotton(){

                    final String place_nametxt = place_name.getText().toString();
                    final String place_descriptiontxt = place_description.getText().toString();
                    final String place_longtxt = place_long.getText().toString();
                    final String place_lattxt = place_lat.getText().toString();

                    // calling service add new place to the database
                    final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    final StringRequest request = new StringRequest(Request.Method.POST, url_addplace, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                // Problem
                                JSONObject obj = new JSONObject(response);
                                status = obj.getString("status");

                                if (status.equals("done")) {
                                    adding_result.setText("The place added successfuly");
                                } else {
                                    adding_result.setText("The place exist before");
                                }

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                        }
                    }) {
                        protected java.util.Map<String, String> getParams() throws AuthFailureError {
                            java.util.Map<String, String> params = new HashMap<String, String>();

                            params.put("name", place_nametxt);
                            params.put("lat", place_lattxt);
                            params.put("long", place_longtxt);
                            params.put("description", place_descriptiontxt);
                            return params;
                        }
                    };
                    queue.add(request);






        }



    }




