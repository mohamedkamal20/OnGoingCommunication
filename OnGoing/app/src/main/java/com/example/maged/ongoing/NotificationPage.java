package com.example.maged.ongoing;

import android.content.Intent;
import android.sax.StartElementListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class NotificationPage extends ActionBarActivity {
    final String userId  = getIntent().getStringExtra("userId");
    final String urlgetNotification = "http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/getAllNotifications/";
    final String urlgetPost = "http://ongoingcommunication- software2project.rhcloud.com/FCISquare/rest/retrivecheckin/";
    final String urlGetLike = "http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/retrievelikes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);

                     //Nested vollies in brief
        // the first volly is responsible for getting the notificaitons list to display it
        // on clikcing any item the second volly starts to get the post text , owner and the like counter
        // and then call the third volly to get the likers names and then start the activity to display all the data about the post


        // this volly is responsible for getting retrieving the Notifications from the database
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest request = new StringRequest(Request.Method.POST, urlgetNotification, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    // Problem

                    JSONObject obj = new JSONObject(response);
                   int Counter = obj.getInt("count");

                            // on getting the response from the service the list of Notification appears in list View
                    String []Notifications = new String[Counter];
                   final int [] PostIds = new int[Counter];
                    for (int i = 0; i < Counter; i++) {
                        Notifications[i] = obj.getString("description" + i);
                    }
                    for (int i = 0; i < Counter; i++) {
                        PostIds[i] = Integer.parseInt(obj.getString("postid" + i));
                    }
                    final ListView listView  = (ListView) findViewById(R.id.NotificationList);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(NotificationPage.this,android.R.layout.simple_list_item_1,Notifications);
                    listView.setAdapter(adapter);

                     // on clikcing any Item in the listView the listView starts to retrive the data of the post responsible of the
                    // notification and start showing the post
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final int index = listView.getSelectedItemPosition();

                            // volly resposible for getting the post itself , owner and text and likes number
                            final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            final StringRequest request = new StringRequest(Request.Method.POST,urlgetPost, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {

                                    try {

                                        // Problem

                                        JSONObject obj = new JSONObject(response);
                                        String PostText = obj.getString("postText");
                                        String PostOwner = obj.getString("username");
                                        String ThePostId = String.valueOf(PostIds[index]);

                                        final Intent goShow = new Intent(NotificationPage.this ,Fortest.class);
                                        goShow.putExtra("posttext" , PostText);
                                        goShow.putExtra("posttext" , PostText);

                                            // on getting the post body this volly start to get the Likers of the post
                                        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                        final StringRequest request = new StringRequest(Request.Method.POST, urlGetLike, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    // Problem
                                                    JSONObject obj = new JSONObject(response);
                                                    String count = obj.getString("count");
                                                    int  Like_count = Integer.parseInt(count);
                                                    goShow.putExtra("likeCounter",String.valueOf(Like_count));
                                                    goShow.putExtra("postID",String.valueOf(PostIds[index]));



                                                    // finally after getting the post and the likers list intent show Post post
                                                    goShow.putExtra("userid",userId);
                                                    startActivity(goShow);
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
                                                params.put("post_id", String.valueOf(PostIds[index]));
                                                return params;
                                            }
                                        };
                                        queue.add(request);













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
                                    params.put("post_id",String.valueOf(PostIds[index]));
                                    return params;
                                }
                            };
                            queue.add(request);






                        }
                    });










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
                params.put("userID", userId);
                return params;
            }
        };
        queue.add(request);









    }
}
