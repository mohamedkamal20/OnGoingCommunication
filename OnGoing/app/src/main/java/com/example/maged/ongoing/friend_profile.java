package com.example.maged.ongoing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
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



// this view is resposible for viewing the friends profile once searching for them
public class friend_profile extends ActionBarActivity {
    String url_follow = "http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/follow";
    String url_unfollow = "http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/unfollow";
    String follow_result , useremail , username,friend_name,id,friend_email;
    String unfollow_result;
    TextView followresult=(TextView)findViewById(R.id.result);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        //------------------------------------------------
        final TextView userName=(TextView)findViewById(R.id.userName);
        final TextView userEmail=(TextView)findViewById(R.id.email);
        Button follow =(Button)findViewById(R.id.follow);
        Button unfollow=(Button)findViewById(R.id.unfollow);


        Button home_back , profile_back , other_back ,logout;
        home_back = (Button) findViewById(R.id.homeback);
        profile_back = (Button) findViewById(R.id.Profileback);
        other_back = (Button) findViewById(R.id.optionsback);
        logout=(Button) findViewById(R.id.logout);

        //-----------------------------------------------
        Bundle EmailBundle = getIntent().getExtras() ;
         username=EmailBundle.getString("Name");
         useremail=EmailBundle.getString("email");
        friend_email=EmailBundle.getString("email1");
        friend_name=EmailBundle.getString("name1");
        id=EmailBundle.getString("id");
        userName.setText(friend_name);
        userEmail.setText(friend_email);
        profile_back.setText(username);
        //--------------------------------------------
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Follow();
            }
        });


        //----------------------------------------------
        unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Unfollow();
            }
        });
        //-----------------------------------------------------------------------------------
        profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  }
        });
        home_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePage();
            }
        });
        other_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsersOptions();
            }
        });
        //-----------------------------------------------------------------------------------------------------------------
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

    }




    void Follow(){
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest request=new StringRequest(Request.Method.POST,url_follow,new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {

                try {

                    // Problem

                    JSONObject obj = new JSONObject(response);
                    follow_result = obj.getString("status");

                    if (follow_result.equals("1")) {
                        followresult.setText("following...");
                    } else {
                        followresult.setText("you already following him");
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
                params.put("email1", useremail);
                params.put("email2", friend_email);
                return params;
            }
        };
        queue.add(request);

    }

void Unfollow(){
    final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

    final StringRequest request=new StringRequest(Request.Method.POST,url_unfollow,new Response.Listener<String>(){

        @Override
        public void onResponse(String response) {

            try {
                // Problem

                JSONObject obj = new JSONObject(response);
                unfollow_result = obj.getString("status");


                if (unfollow_result.equals("1")) {

                    followresult.setText("Unfollow done");

                } else {

                    followresult.setText("you already unfollow him");
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
            params.put("email1", useremail);
            params.put("email2", friend_email);
            return params;
        }
    };
    queue.add(request);
}




    void Profile(){
        Intent goProfile = new Intent(friend_profile.this,profile.class);
        startActivity(goProfile);
        goProfile.putExtra("Name",username);
        goProfile.putExtra("email",useremail);
        goProfile.putExtra("id",id);
        startActivity(goProfile);
    }




    void UsersOptions(){
        Intent goMoreOption = new Intent(friend_profile.this,MoreOption.class);
        startActivity(goMoreOption);
        goMoreOption.putExtra("Name",username);
        goMoreOption.putExtra("email",useremail);
        goMoreOption.putExtra("id",id);
        startActivity(goMoreOption);
    }
    void Logout(){

        username = useremail = id = "";
        Intent intent=new Intent(friend_profile.this,signin.class);
        startActivity(intent);
    }

void HomePage(){
    Intent goHome = new Intent(friend_profile.this,Home.class);
    startActivity(goHome);
    goHome.putExtra("Name",username);
    goHome.putExtra("email",useremail);
    goHome.putExtra("id",id);
    startActivity(goHome);
}

}
