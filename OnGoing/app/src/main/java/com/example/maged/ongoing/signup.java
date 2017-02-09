package com.example.maged.ongoing;

import android.content.Intent;
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


public class signup extends ActionBarActivity {


    RequestQueue queue  ;
    final String url_signUp = "http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/signup";
    EditText name , email , passWord ;
    String  username , useremail , pass ;
    TextView showname ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

          name=(EditText) findViewById(R.id.name);
          email=(EditText) findViewById(R.id.email);
          passWord = (EditText) findViewById(R.id.pass);
        Button signup = (Button) findViewById(R.id.signUP);
            showname = (TextView) findViewById(R.id.text);

        //---------------------------------------



        // this function is responsible for getting the data from the edit texts and call the service
        // if the sign up completed successfully it goes directly to the user profile otherwise it displayes invalid to the user
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = name.getText().toString();
                useremail = email.getText().toString();
                pass = passWord.getText().toString();
                SignUp();
            }

        });

    }


    void SignUp(){



        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest request = new StringRequest(Request.Method.POST, url_signUp, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    final String userName , userEmail , userId ;
                    JSONObject obj = new JSONObject(response);
                    userName = obj.getString("email").toString();
                    userEmail = obj.getString("name").toString();
                    userId = obj.getString("id").toString();

                    if (userName.equals("")) {
                        showname.setText("Invalid");
                    } else {
                        showname.setText(userEmail);
                        Intent intent = new Intent(signup.this, Home.class);
                        startActivity(intent);
                        intent.putExtra("Name",userName) ;
                        intent.putExtra("email",userEmail);
                        intent.putExtra("id",userId);
                        startActivity(intent);
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
                params.put("name",useremail);
                params.put("email", username);
                params.put("pass", pass);
                return params;
            }
        };
        queue.add(request);

    }





}
