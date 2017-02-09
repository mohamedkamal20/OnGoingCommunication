package com.example.maged.ongoing;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

class CustomAdapter extends BaseAdapter {
    String[] ownersList;
    String[] posstBodies;
    String[] likesCounter;
    String[]  postIds;

    String url_retrievecomments="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/retrievecomments";
    String url_retrievelikes="http://ongoingcommunication-software2project.rhcloud.com/FCISquare/rest/retrievelikes";
    String [] PostsIds ;
    String [] postsBodies ;
    String [] LikesCounter ;
    int comment_count;
    String []commentText;
    String []commentDate;
    String []commentOwner;
    int like_count;
    String [] likesownersName;
    String userid;

    Context context;

    private static LayoutInflater inflater = null;

    public CustomAdapter(Home mainActivity, String[] owner, String[] posts, String[] likes,String[] postsIDs, String userid) {
        // TODO Auto-generated constructor stub
        this.ownersList = owner;
        this.posstBodies = posts;
        this.likesCounter = likes;
        this.postIds=postsIDs;
        this.userid=userid;

        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ownersList.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView owner;
        TextView post;
        Button like_counter;
        Button comment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.program_list, null);
        holder.owner = (TextView) rowView.findViewById(R.id.postOwnerName);
        holder.owner.setText(ownersList[position]);

        holder.post = (TextView) rowView.findViewById(R.id.PostBody);
        holder.post.setText(posstBodies[position]);

        holder.like_counter = (Button) rowView.findViewById(R.id.Comment_Like);
        String like_count = likesCounter[position];
        holder.like_counter.setText(like_count);

        holder.comment=(Button) rowView.findViewById(R.id.comment);
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //-------------------------------------------------------------------

                //-------------------------------------------------------------------


                Intent intent = new Intent(context, Fortest.class);
                context.startActivity(intent);
                intent.putExtra("posttext", posstBodies[position]);
                intent.putExtra("postsOwners", ownersList[position]);
                intent.putExtra("likeCounter", likesCounter[position]);
                intent.putExtra("postID",postIds[position]);
                intent.putExtra("userid",userid);
                context.startActivity(intent);
            }
        });
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + ownersList[position], Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }
}

