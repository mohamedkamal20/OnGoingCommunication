package com.example.maged.ongoing;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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


public class ShowPost extends ActionBarActivity {
    String PostOwner , PostBody ;
    String [] LikersNames ;
    String [] TheComments ;
    String url_like="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/like";
    String url_comment="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/comment";
    String   result1;
    Button Likebtn , commentbtn;
    ListView commentslist;
    EditText ThenewComment ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);
        //----------------------------------------------------

        //----------------------------------------------------------

        Bundle bundle = getIntent().getExtras() ;
       // String [] comments=bundle.getStringArray("comments");
        //String [] commentOwner=bundle.getStringArray("commentOwner");

       // String [] likesownersName=bundle.getStringArray("likes");

        String likes_count=bundle.getString("likeCounter");
        final String postowner=bundle.getString("postsOwners");
        String postText=bundle.getString("posttext");
        final String post_id=bundle.getString("post_id");
        final String id=bundle.getString("id");

        // ------------------------------------------------------------------------------------


        Likebtn = (Button)findViewById(R.id.like_Button);
        Likebtn.setText(Integer.toString(LikersNames.length));
        commentbtn = (Button) findViewById(R.id.AddCommentbutton);
        ThenewComment = (EditText) findViewById(R.id.newComment);
        commentslist = (ListView) findViewById(R.id.listViewComment);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShowPost.this,android.R.layout.simple_list_item_1,TheComments);
        commentslist.setAdapter(adapter);
        Likebtn.setText(likes_count);
        TextView post_ownerName=(TextView)findViewById(R.id.post_ownerName);
        TextView main_Post_body=(TextView)findViewById(R.id.main_Post_body);
        main_Post_body.setText(postText);
        post_ownerName.setText(postowner);

        // -------------------------------------------------------------------------------------------

        Likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //----------------------------------------
                final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                final StringRequest request = new StringRequest(Request.Method.POST, url_like, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            // Problem

                            JSONObject obj = new JSONObject(response);
                            result1 = obj.getString("id").toString();
                            if (result1.equals("-1")) {
                                Log.e("ID", "NOT DONE");
                            } else {
                                Log.e("ID", "DONE");
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
                        params.put("user_id", id);
                        params.put("post_id", post_id);
                        return params;
                    }
                };
                queue.add(request);


            }
        });


        commentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String TheText = ThenewComment.getText().toString();
                //------------------------------------------------
                //----------------------------------------
                final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                final StringRequest request=new StringRequest(Request.Method.POST,url_comment,new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {

                        try {

                            // Problem

                            JSONObject obj = new JSONObject(response);
                            result1 = obj.getString("id").toString();
                            if(result1.equals("-1"))
                            {
                                Log.e("ID","NOT DONE");
                            }
                            else
                            {
                                Log.e("ID","DONE");
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
                        params.put("user_id",id);
                        params.put("post_id",post_id);
                        params.put("text",TheText);
                        return params;
                    }
                };
                queue.add(request);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
