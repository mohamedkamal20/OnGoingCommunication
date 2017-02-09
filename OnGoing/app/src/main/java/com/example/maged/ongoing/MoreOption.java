package com.example.maged.ongoing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
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


// more options
// this is the view responsible for adding new place for the database
// and adding place to the faviourite places list

public class MoreOption extends ActionBarActivity {

    Button add_place,save_place , home_back , other_back , profile_back,logout;
    EditText save_place_text;
    TextView save_place_result;

    String url_save_place="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/savePlace";
    String statues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_option);
    // ------------------------------------------------------------------------
        add_place = (Button) findViewById(R.id.add_new_place);
        save_place=(Button) findViewById(R.id.favplace);
        save_place_text=(EditText)findViewById(R.id.favplacetext);
        save_place_result=(TextView) findViewById(R.id.save_place_result);
        home_back = (Button) findViewById(R.id.homeback);
        profile_back = (Button) findViewById(R.id.Profileback);
        other_back = (Button) findViewById(R.id.optionsback);
        logout=(Button) findViewById(R.id.logout);

    //---------------------------------------------------------------------------

   //-----------------------------------------------------------------------------
        Bundle nameBundle = getIntent().getExtras() ;
        final String username=nameBundle.getString("Name");
        final String useremail=nameBundle.getString("email");
        final String id=nameBundle.getString("id");
        profile_back.setText(username);
    //---------------------------------------------------------------------------
        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MoreOption.this,AddPlace.class);
                startActivity(intent);

            }
        });
     //------------------------------------------------------
        save_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final String save_place_str=save_place_text.getText().toString();
               final String id_str=id;
                final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                final StringRequest request=new StringRequest(Request.Method.POST,url_save_place,new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {

                        try {

                            // Problem

                            JSONObject obj = new JSONObject(response);
                            statues = obj.getString("status").toString();

                            if (statues.equals("done"))
                            {

                                save_place_result.setText("This place added successfuly to your favourit palces");
                            }
                            else
                            {
                                save_place_result.setText("This place doesn't exist");

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
                        params.put("name",save_place_str);
                        params.put("userID",id_str);
                        return params;
                    }
                };
                queue.add(request);


            }
        });


        profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MoreOption.this, profile.class);
                startActivity(intent);
                intent.putExtra("Name",username);
                intent.putExtra("email",useremail);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        home_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MoreOption.this, Home.class);
                startActivity(intent);
                intent.putExtra("Name",username);
                intent.putExtra("email",useremail);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        other_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MoreOption.this, MoreOption.class);
                startActivity(intent);
                intent.putExtra("Name",username);
                intent.putExtra("email",useremail);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        //-----------------------------------------------------------------------------------------------------------------
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MoreOption.this,signin.class);
                startActivity(intent);
            }
        });


    }

}
