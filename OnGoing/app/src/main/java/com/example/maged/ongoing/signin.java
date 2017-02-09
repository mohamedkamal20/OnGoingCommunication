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

    // signIn page allows the user to enter his username , and password and login to his account
public class signin extends ActionBarActivity {

    RequestQueue queue  ;
    final String url_login = "http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/login";
    String user,pass;
    TextView login_statues=(TextView) findViewById(R.id.text);
    String UserName,UserEmail,UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        final EditText UserName=(EditText)findViewById(R.id.userName);
        final EditText Pass= (EditText) findViewById (R.id.password);
        final Button signin=(Button) findViewById(R.id.login);
        Button signUp=(Button)findViewById(R.id.signup);

// takes username and the password and start call the sign-in service
        // if there's such user it redirect automatically to his profile else display no such user to the screen
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user =UserName.getText().toString();
                pass =Pass.getText().toString();
                SignIn();
            }
        });

            // calling the signUp activity
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
             }
        });
    }



        void SignIn(){
            queue = Volley.newRequestQueue(getApplicationContext());
            final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            final StringRequest request=new StringRequest(Request.Method.POST,url_login,new Response.Listener<String>(){

                @Override
                public void onResponse(String response) {

                    try {

                        // Problem

                        JSONObject obj = new JSONObject(response);
                        UserEmail = obj.getString("email").toString();
                        UserName=obj.getString("name").toString();
                        UserId = obj.getString("id").toString();

                        if (UserEmail.equals(""))
                        {
                            login_statues.setText("No Such User");
                        }
                        else
                        {

                            login_statues.setText(UserName);
                            Intent intent = new Intent(signin.this, Home.class);
                            intent.putExtra("Name",UserName) ;
                            intent.putExtra("email",UserEmail);
                            intent.putExtra("id",UserId);
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
                    params.put("email", user);
                    params.put("pass", pass);
                    return params;
                }
            };
            queue.add(request);
        }

        void SignUp(){
            Intent intent = new Intent(signin.this, signup.class);
            startActivity(intent);
        }


}
