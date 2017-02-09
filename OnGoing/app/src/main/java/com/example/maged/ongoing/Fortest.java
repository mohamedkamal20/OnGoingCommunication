package com.example.maged.ongoing;

import android.content.Intent;
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

 // responsible for showing the posts in separet view , and allow the user to interact with the post , like , comment , view likers

public class Fortest extends ActionBarActivity {
    final String url_like="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/like";
    final String url_comment="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/comment";
    final String url_retrievecomments="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/retrievecomments";
    final String url_retrievelikes="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/retrievelikes";
    String count_comment ,postText,likes_count,postowner,postID,userid , like_statues , comment_statues;
    String []commentText;
    String []commentOwner ;
    String []commentmerge;
    int like_count;
    EditText newComment;
    String [] likesownersName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fortest);
        final TextView post_ownerName=(TextView)findViewById(R.id.post_ownerName);
        TextView main_Post_body=(TextView)findViewById(R.id.main_Post_body);
        Button like_Button=(Button) findViewById(R.id.like_Button);
        Button AddCommentbutton=(Button)findViewById(R.id.AddCommentbutton);
         newComment=(EditText)findViewById(R.id.newComment);
        Button showlikes=(Button)findViewById(R.id.showlikes);
        //-----------------------------------------------
        Bundle bundle = getIntent().getExtras();
         postText=bundle.getString("posttext");
         likes_count=bundle.getString("likeCounter");
         postowner=bundle.getString("postsOwners");
         postID=bundle.getString("postID");
         userid=bundle.getString("userid");
        //-------------------------------------------------
        main_Post_body.setText(postText);
        post_ownerName.setText(postowner);
        like_Button.setText(likes_count);
        //----------------------------------------------------------------------------------------
        // responsible for starting new activity to show how liked this post
        showlikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewLikers();
            }
        });
        RetrievePostComments();


        // responsible for liking the post
    //---------------------------------------------------------------------------------------------
      like_Button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            LikePost();
          }
      });
    //----------------------------------------------------------------------------------------
        // responsible for calling the service to add new comment
        AddCommentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Comment();
            }
        });

    }


    void ViewLikers(){
        Intent intent1=new Intent(Fortest.this,ShowLikers.class);
        intent1.putExtra("PostId",postID);
        startActivity(intent1);
    }
    void RetrievePostComments(){

        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest request = new StringRequest(Request.Method.POST, url_retrievecomments, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    // Problem

                    JSONObject obj = new JSONObject(response);
                    count_comment = obj.getString("count").toString();
                    int count =Integer.parseInt(count_comment);
                    if(count!=0)
                    {
                        commentText=new String[count];
                        commentOwner=new String[count];
                        commentmerge=new String[count];
                        for(int i=0;i<count;i++)
                        {
                            commentText[i]=obj.getString("text"+i).toString();
                            commentOwner[i]=obj.getString("username"+i).toString();
                            commentmerge[i]=commentOwner[i]+" comment : "+commentText[i];
                            // commentmerge[i]=commentText[i];
                            Log.e("COMMENT",commentText[i]);

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Fortest.this ,android.R.layout.simple_list_item_1,commentmerge );
                        ListView listView=(ListView)findViewById(R.id.listViewComment);
                        listView.setAdapter(adapter);
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
                params.put("post_id", postID);
                return params;
            }
        };
        queue.add(request);
    }



    void LikePost(){
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest request = new StringRequest(Request.Method.POST, url_like, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    // Problem

                    JSONObject obj = new JSONObject(response);
                    like_statues = obj.getString("id").toString();
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
                params.put("user_id",userid);
                params.put("post_id", postID);
                return params;
            }
        };
        queue.add(request);
    }


    void Comment(){
        final String TheText = newComment.getText().toString();
        //------------------------------------------------

        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest request=new StringRequest(Request.Method.POST,url_comment,new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {

                try {
                    // Problem
                    JSONObject obj = new JSONObject(response);
                    comment_statues = obj.getString("id").toString();
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
                params.put("user_id",userid);
                params.put("post_id",postID);
                params.put("text",TheText);
                return params;
            }
        };
        queue.add(request);
    }





}
