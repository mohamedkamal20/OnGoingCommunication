package com.example.maged.ongoing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

// this is the home page

public class Home extends ActionBarActivity {

    RequestQueue queue  ;
    String url = "http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/search-user";
    String url1 = "http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/get-user-last-position";
    String url2 = "http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/updatePosition";
    String url_postinfo="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/showHomePage";
    String search_email;
    String pass;
    String result1,result2;

    //--------------------------------------

    String url_retrievecomments="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/retrievecomments";
    String url_retrievelikes="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/retrievelikes";

    String postownername,posttext,numperofcomments;
    String [] postsOwnerName ;



    String [] PostsIds ;
    String [] postsBodies ;
    String [] LikesCounter ;

    int comment_count;
    String []commentText;
    String []commentDate;
    String []commentOwner;
    int like_count;
    String [] likesownersName;
    static  String [] postsbodies ;
    static String [] postsIDs ;
    static String []  postsOwners;
    static String [] likeCounter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //-------------------------------------------------------
        TextView UserName=(TextView) findViewById(R.id.userName);
        final EditText Search =(EditText) findViewById(R.id.ser);
        Button search_but=(Button) findViewById(R.id.search);
        final TextView search_res=(TextView)findViewById(R.id.searchRes);
        Button home_back , profile_back , other_back,logout ;
        home_back = (Button) findViewById(R.id.homeback);
        profile_back = (Button) findViewById(R.id.Profileback);
        other_back = (Button) findViewById(R.id.optionsback);
        logout=(Button) findViewById(R.id.logout);
        Button chickin=(Button)findViewById(R.id.chickin);




        //-------------------------------------------------------
        Bundle nameBundle = getIntent().getExtras() ;
        final String username=nameBundle.getString("Name");
        final String useremail=nameBundle.getString("email");
        final String id=nameBundle.getString("id");
        UserName.setText(username);
        profile_back.setText(username);
        //---------------------------------------------------------
        search_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                search_email = Search.getText().toString();


                final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            // Problem

                            JSONObject obj = new JSONObject(response);
                            result1 = obj.getString("email").toString();
                            result2 = obj.getString("name").toString();

                            if (result1.equals("")) {

                                search_res.setText("there is no one with this email please try again...");
                            } else {

                                search_res.setText("this exit go to his profile");
                                Intent intent = new Intent(Home.this, friend_profile.class);
                                startActivity(intent);
                                intent.putExtra("Name",username);
                                intent.putExtra("email", useremail);
                                intent.putExtra("email1",result1);
                                intent.putExtra("name1",result2);
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
                        params.put("email",  search_email);
                        return params;
                    }
                };
                queue.add(request);

            }

        });
        //--------------------------------------------------------------------------
        profile_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, profile.class);
                startActivity(intent);

                intent.putExtra("Name", username);
                intent.putExtra("id", id);
                intent.putExtra("email",useremail);
                startActivity(intent);
          }
        });
        //----------------------------------------------------
        other_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, MoreOption.class);
                startActivity(intent);

                intent.putExtra("Name", username);
                intent.putExtra("id", id);
                intent.putExtra("email", useremail);
                startActivity(intent);
            }
        });


       //---------------------------------------------------

        home_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,Home.class);
                startActivity(intent);
                intent.putExtra("Name", username);
                intent.putExtra("id", id);
                intent.putExtra("email", useremail);
                startActivity(intent);
            }
        });
    //-----------------------------------------------------------------------------------------------------------------
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,signin.class);
                startActivity(intent);
            }
        });
   //------------------------------------------------------------------------------------------------------------------
        chickin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,Chickin.class);
                startActivity(intent);
                intent.putExtra("id", id);
                intent.putExtra("Name", username);
                intent.putExtra("email", useremail);
                startActivity(intent);
            }
        });


        // ----------------------------------------------------------------------


        //-----------------------------------------------------------------------------

        final ListView listView = (ListView) findViewById(R.id.posts_listview);
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final StringRequest request=new StringRequest(Request.Method.POST,url_postinfo,new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {

                try {

                    // Problem

                    JSONObject obj = new JSONObject(response);
                    String count=obj.getString("count");
                    int numberOfPosts=Integer.parseInt(count);
                    if (numberOfPosts!=0)
                    {
                        postsOwners=new String[ numberOfPosts];
                        postsbodies=new String[ numberOfPosts];
                        likeCounter=new String[ numberOfPosts];
                        postsIDs=new String[ numberOfPosts];
                        for(int i=0;i<numberOfPosts;i++)
                        {
                            postsbodies[i]=obj.getString("postText"+i).toString();
                            postsOwners[i]=obj.getString("username"+i).toString();
                            likeCounter[i]=obj.getString("numOfLikes"+i).toString();
                            postsIDs[i]=obj.getString("postID"+i).toString();

                        }

                        //---------------------------------------------------------------------------------------

                        //---------------------------------------------------------------------------------------
                        listView.setAdapter(new CustomAdapter(Home.this,postsOwners,postsbodies,likeCounter,postsIDs,id));

                    }
                    else
                    {
                        numberOfPosts=1;
                        postsOwners=new String[ numberOfPosts];
                        postsbodies=new String[ numberOfPosts];
                        likeCounter=new String[ numberOfPosts];
                        postsIDs=new String[ numberOfPosts];
                        for(int i=0;i<numberOfPosts;i++)
                        {
                            postsbodies[i]="";
                            postsOwners[i]="";
                            likeCounter[i]="";
                            postsIDs[i]="";
                        }
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
                params.put("userID",id);
                return params;
            }
        };
        queue.add(request);
        // ----------------------------------------------------------------------


        //-----------------------------------------------------------------------

        /*ListView listView = (ListView) findViewById(R.id.posts_listview);
        String [] postsOwners = {"MO","Taliawy","Kamal"};
        String [] postsbodies = {"MOPOST","TaliawyPOST","KamalPOST"};
        String []lieks_counter = {"1","2","3"};
        String []postsids={"1","2","3"};
        listView.setAdapter(new CustomAdapter(this,postsOwners,postsbodies,lieks_counter,postsids));
        //listView.setAdapter(new CustomAdapter(Home.this,postsOwners,postsbodies,likeCounter,postsIDs));*/
        //-----------------------------------------------------------
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String postID = postsIDs[position];
                final RequestQueue queuecomment = Volley.newRequestQueue(getApplicationContext());

                final StringRequest requestcomment = new StringRequest(Request.Method.POST, url_retrievecomments, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            // Problem

                            JSONObject obj = new JSONObject(response);

                            String count = obj.getString("count");
                            comment_count = Integer.parseInt(count);

                            commentText = new String[comment_count];
                            commentDate = new String[comment_count];
                            commentOwner = new String[comment_count];

                            for (int i = 0; i < comment_count; i++) {
                                commentText[i] = obj.getString("text" + i).toString();
                                commentDate[i] = obj.getString("date" + i).toString();
                                commentOwner[i] = obj.get("usermail" + i).toString();

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
                queuecomment.add(requestcomment);

                //-----------------------------------------------------------------------------------------------

                final RequestQueue queuelikes = Volley.newRequestQueue(getApplicationContext());

                final StringRequest requestlikes = new StringRequest(Request.Method.POST, url_retrievelikes, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            // Problem

                            JSONObject obj = new JSONObject(response);
                            String count = obj.getString("count");
                            like_count = Integer.parseInt(count);

                            String[] likesownersName = new String[like_count];


                            for (int i = 0; i < like_count; i++) {
                                likesownersName[i] = obj.getString("userMail" + i).toString();

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
                        params.put("post_id", "1");

                        return params;
                    }
                };
                queuelikes.add(requestlikes);

                Intent showPost = new Intent(Home.this, ShowPost.class);
                showPost.putExtra("comments", commentText);
                showPost.putExtra("likes", likesownersName);
                showPost.putExtra("commentOwner", commentOwner);
                showPost.putExtra("postText", postsbodies[position]);
                showPost.putExtra("postowner", postsOwners[position]);
                showPost.putExtra("post_id", postID);
                showPost.putExtra("id", id);
                startActivity(showPost);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
