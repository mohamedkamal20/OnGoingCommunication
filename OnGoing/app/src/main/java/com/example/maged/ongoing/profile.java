package com.example.maged.ongoing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;

// display the personal Profile to spicific user
public class profile extends ActionBarActivity {
    RequestQueue queue  ;
   final String url_updatePosition = "http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/updatePosition";
   final String url_GetLastPosition = "http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/get-user-last-position";

    TextView User_latitude , User_longitude , updatePostionResult ;
    EditText new_latitude , new_logitude ;
    String UpdateResult;


    String UserName , UserEmail ,UserId  ;
    ArrayList <String> followrName=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //--------------------------------------------
        final TextView userName=(TextView)findViewById(R.id.userName);
        final TextView userEmail=(TextView)findViewById(R.id.email);


        final Button getLastPos=(Button)findViewById(R.id.lastPosition);
        Button notifications = (Button) findViewById(R.id.NotificationBtn);
        User_latitude=(TextView)findViewById(R.id.lat);
        User_longitude=(TextView)findViewById(R.id.lon);

        Button updatepos=(Button)findViewById(R.id.Update);
        updatePostionResult=(TextView)findViewById(R.id.updateresult);
        new_latitude =(EditText) findViewById(R.id.newlat);
        new_logitude =(EditText) findViewById(R.id.newlon);

        Button getfollower=(Button)findViewById(R.id.getfollower);
        Button home_back , profile_back , other_back ,logout;
        home_back = (Button) findViewById(R.id.homeback);
        profile_back = (Button) findViewById(R.id.Profileback);
        other_back = (Button) findViewById(R.id.optionsback);
        logout=(Button) findViewById(R.id.logout);



        //--------------------------------------------
        Bundle EmailBundle = getIntent().getExtras() ;
        UserName=EmailBundle.getString("Name");
        UserEmail=EmailBundle.getString("email");
        UserId=EmailBundle.getString("id");
        userName.setText(UserName);
        userEmail.setText(UserEmail);
        profile_back.setText(UserName);
        //--------------------------------------------

        getLastPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastPosition();
            }

        });
//------------------------------------------------------------------------------------------

        updatepos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdatePostion() ;
            }

        });
        //--------------------------------------------------------

        getfollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFollowers();
            }
        });
  // -------------------------------------------------------------------------------
        profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile();
            }
        });
        home_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Home();
            }
        });
        other_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               UserOptions();
            }
        });
        //-----------------------------------------------------------------------------------------------------------------
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logtout();
            }
        });


        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetNotification();
            }
        });

    }

void Profile(){


    Intent goProfile = new Intent(profile.this,profile.class);
    startActivity(goProfile);
    goProfile.putExtra("Name",UserName);
    goProfile.putExtra("email",UserEmail);
    goProfile.putExtra("id",UserId);
    startActivity(goProfile);
}

void Logtout(){
    UserName = UserId = UserEmail = "";
    Intent intent=new Intent(profile.this,signin.class);
    startActivity(intent);
}


void GetNotification(){
    Intent goNotifications = new Intent(profile.this,NotificationPage.class);
    goNotifications.putExtra("userId",String.valueOf(UserId));
    startActivity(goNotifications);
}

void UserOptions(){
    Intent goMoreOption = new Intent(profile.this,MoreOption.class);
    startActivity(goMoreOption);
    goMoreOption.putExtra("Name",UserName);
    goMoreOption.putExtra("email",UserEmail);
    goMoreOption.putExtra("id",UserId);
    startActivity(goMoreOption);
}


    void Home(){
        Intent goHome = new Intent(profile.this,Home.class);
        startActivity(goHome);
        goHome.putExtra("Name",UserName);
        goHome.putExtra("email",UserEmail);
        goHome.putExtra("id",UserId);
        startActivity(goHome);
    }



    void getLastPosition(){
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest request = new StringRequest(Request.Method.POST, url_GetLastPosition, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    // Problem
                    String Latitude , Longitude ;
                    JSONObject obj = new JSONObject(response);
                    Latitude = obj.getString("lat").toString();
                    Longitude = obj.getString("long").toString();
                    User_latitude.setText(Latitude);
                    User_longitude.setText(Longitude);

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
                params.put("email", UserEmail);

                return params;
            }
        };
        queue.add(request);

    }




void UpdatePostion(){
    final String Newlat = new_latitude.getText().toString();
    final String Newlon = new_logitude.getText().toString();

    final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

    final StringRequest request = new StringRequest(Request.Method.POST, url_updatePosition, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            try {

                // Problem

                JSONObject obj = new JSONObject(response);
                UpdateResult = obj.getString("status").toString();


                if (UpdateResult.equals("1")) {
                    updatePostionResult.setText("Position Updeated successfully");
                } else {

                    updatePostionResult.setText("There is a problem in the update ");

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
            params.put("id", UserId);
            params.put("lat", Newlat);
            params.put("long", Newlon);
            return params;
        }
    };
    queue.add(request);
}





    void getFollowers(){
        Intent intent=new Intent(profile.this,Follower.class);
        intent.putExtra("email",UserEmail);
        startActivity(intent);
    }

}
