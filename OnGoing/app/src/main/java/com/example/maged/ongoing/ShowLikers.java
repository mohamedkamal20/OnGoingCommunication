package com.example.maged.ongoing;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

// shows simple list view with the Likers names to spicific post
// calls get Likers services and display the result in listView

public class ShowLikers extends ActionBarActivity {

   String []LikersList;
   int Like_count ;
   final String PostId = getIntent().getStringExtra("PostId");
   String urlGetLike = "http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/retrievelikes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_likers);
        //-----------------------------------------------------------------------------

            // service get Likers volly
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest request = new StringRequest(Request.Method.POST, urlGetLike, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject obj = new JSONObject(response);
                    String count = obj.getString("count");
                    Like_count = Integer.parseInt(count);
                    LikersList = new String[Like_count];
                    for (int i = 0; i < Like_count; i++) {
                        LikersList[i] = obj.getString("username" + i).toString();
                    }

                    ListView listView = (ListView) findViewById(R.id.TheLikers);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShowLikers.this, android.R.layout.simple_list_item_1, LikersList);
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
                params.put("post_id", PostId);
                return params;
            }
        };
        queue.add(request);

    }
}
