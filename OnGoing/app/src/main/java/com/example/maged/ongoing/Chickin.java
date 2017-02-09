package com.example.maged.ongoing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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


// responsible for making check-in to specific place and writing post about it
// the user enter the place name , and the place rate and the post text and then press add button to call the service
public class Chickin extends ActionBarActivity {

    EditText place_name,post_text;
    Button profile_back,other_back,home_back,logout ,confirmPost;
    String username , id , useremail ,result1;
    final String url="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/checkin";
    TextView checkIn_result  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chickin);


        //---------------------------------------------
        place_name=(EditText)findViewById(R.id.place_chickin);
        post_text=(EditText)findViewById(R.id.text_chickin);
        checkIn_result=(TextView)findViewById(R.id.result_chickin);
        confirmPost =(Button)findViewById(R.id.confirm_chickin);
        profile_back=(Button)findViewById(R.id.Profileback);
        home_back=(Button) findViewById(R.id.homeback);
        other_back=(Button)findViewById(R.id.optionsback);
        logout=(Button) findViewById(R.id.logout);
        //----------------------------------------------
        Bundle bundle = getIntent().getExtras() ;
        id=bundle.getString("id");
        username=bundle.getString("Name");
        useremail=bundle.getString("email");
        profile_back.setText(username);
        confirmPost.setText("Chick-in");



        // this function initaite the volly needed to call the service responsible for making the check-in
        //----------------------------------------------
        confirmPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInService();
            }
        });



        //allow the user to go to his profile
        //----------------------------------------------
        profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileButton();

            }
        });
        // allow the user to go his home page
        home_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeBotton();
            }
        });
        // allow the user to go to user options page
        // ------------------------------------------------------------------------------------------------------------
        other_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackButton();
            }
        });
            // allow the user to go logout his account
        //-----------------------------------------------------------------------------------------------------------------
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

    }


    void BackButton(){
        Intent goMoreOption = new Intent(Chickin.this,MoreOption.class);
        startActivity(goMoreOption);
        goMoreOption.putExtra("Name",username);
        goMoreOption.putExtra("email",useremail);
        goMoreOption.putExtra("id",id);
        startActivity(goMoreOption);
    }
    void Logout(){
        username = useremail = id = "";
        Intent intent=new Intent(Chickin.this,signin.class);
        startActivity(intent);
    }

    void HomeBotton(){
        Intent goHome = new Intent(Chickin.this,Home.class);
        startActivity(goHome);
        goHome.putExtra("Name",username);
        goHome.putExtra("email",useremail);
        goHome.putExtra("id",id);
        startActivity(goHome);
    }

    void ProfileButton(){
        Intent goProfile = new Intent(Chickin.this,profile.class);
        startActivity(goProfile);
        goProfile.putExtra("Name",username);
        goProfile.putExtra("email",useremail);
        goProfile.putExtra("id",id);
        startActivity(goProfile);
    }


    void CheckInService(){
        final String ThePost = post_text.getText().toString();
        final String placeName = place_name.getText().toString();
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest request=new StringRequest(Request.Method.POST,url,new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject obj = new JSONObject(response);
                    result1 = obj.getString("status").toString();

                    if (result1.equals("0"))
                    {
                        checkIn_result.setText("Invalid");
                    }
                    else
                    {
                        checkIn_result.setText("done");
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
                params.put("placename", placeName);
                params.put("userid", id);
                params.put("text",ThePost);

                return params;
            }
        };
        queue.add(request);
    }


}
