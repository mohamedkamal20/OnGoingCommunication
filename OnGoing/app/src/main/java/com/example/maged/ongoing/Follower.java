package com.example.maged.ongoing;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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


public class Follower extends ActionBarActivity {

    // designed to list the followers of the user
    // calling the service to retrieve the followers and set simple list view to thier names
    String getFollowersLink = "http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/get-followers";
    final String UserMail = getIntent().getStringExtra("email");
    final ListView listView = (ListView) findViewById(R.id.listView);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        // calling the service to retrive list of the followers
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final StringRequest request=new StringRequest(Request.Method.POST,getFollowersLink,new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    int count = obj.getInt("count");
                    String []Followers = new String[count];
                    for(int i= 0 ; i < count;i++){
                        Followers[i] = obj.getString("name" + (i + 1));
                    }

                    // setting the content of listView to the Names of followers to the user
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, Followers) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setTextColor(Color.WHITE);
                            text.setTextSize(25);
                            return view;
                        }
                    };
                    listView.setAdapter(adapter);
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
                params.put("email", UserMail);
                return params;
            }
        };
        queue.add(request);
    }

}
