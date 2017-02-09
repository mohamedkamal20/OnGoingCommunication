package com.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.catalina.User;
import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONObject;

import com.models.ByNearbyPlaces;
import com.models.ByNumOfCheckins;
import com.models.ByRate;
import com.models.CheckIn;
import com.models.Comment;
import com.models.CommentNotifications;
import com.models.DBConnection;
import com.models.Like;
import com.models.LikeNotifications;
import com.models.Place;
import com.models.SortingMethod;
import com.models.UserModel;

/**
 * @author Mohamed
 * CheckIncontroller 
 * contains all services that are responsible for check in actions
 * such adding check ins , actions on checkins .. 
 */
@Path("/")
public class Ckeck_in_Controller 
{

	private SortingMethod sort = null ;
		
	
	@POST
	@Path("/sort")
	@Produces(MediaType.TEXT_PLAIN)
	public String sort(@FormParam("method") String method , @FormParam("UserId") String userid){
		UserModel u = new UserModel();
		u.setId(Integer.parseInt(userid));
		
		if (method.contains("rate")) u.setSortMethod("r");
		else if (method.contains("check ins")) u.setSortMethod("numCheckIns");
		else if (method.contains("places")) u.setSortMethod("nearby");
		
		JSONObject json = new JSONObject();
		if (sort!=null) json.put("status", "done");
		else json.put("status", "notDone");
		
		return json.toJSONString();
	}
	
	@POST
	@Path("/checkin")
	@Produces(MediaType.TEXT_PLAIN)
	public String addCheckIn(@FormParam("placename") String placeName,@FormParam("userid") String userID, @FormParam("text") String text)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String date  = dateFormat.format(cal.getTime()); 
		CheckIn ch = new CheckIn();
		Place p = new Place();
		UserModel u = new UserModel();
		p.setName(placeName);
		u.setId(Integer.parseInt(userID));
		ch.setPlace(p);
		ch.setUser(u);
		ch.setText(text);
		ch.setDate(date);
		
		ch = ch.checkIn();
		JSONObject json = new JSONObject();
		if(ch == null) 
		{
			json.put("status", 0);
		}
		else 
		{
			json.put("status", 1);
			//json.put("ID", ch.getId());
			//json.put("UserID", ch.getUser().getId());
			//json.put("Place Name", ch.getPlace().getName());
			//json.put("Text", ch.getText());
		    //json.put("Date", ch.getDate());
		}
		return json.toJSONString();
	}
	
	@POST
	@Path("/retrivecheckin")
	@Produces(MediaType.TEXT_PLAIN)
	public String retriveCheckIn(@FormParam("checkinID") String checkinID)
	{
		CheckIn ch = new CheckIn();
		ch = ch.retrieveCheckIn(Integer.parseInt(checkinID));
		JSONObject json = new JSONObject();
		if(ch == null)
		{
			return json.toJSONString();
		}
		json.put("postID", ch.getId());
		json.put("postText", ch.getText());
		json.put("postDateTime", ch.getDate());
		json.put("userID", ch.getUser().getId());
		json.put("placeID", ch.getPlace().getId());
		return json.toJSONString();
	}
	
	@POST
	@Path("/comment")
	@Produces(MediaType.TEXT_PLAIN)
	public String comment(@FormParam("user_id") String userID,@FormParam("post_id") String postID, @FormParam("text") String text) 
	{
		Comment obj = new Comment ();
		int id = obj.performAction(text,Integer.parseInt(userID),Integer.parseInt(postID));
		JSONObject json = new JSONObject();
		json.put("id", id);
		return json.toJSONString();
	}
	
	@POST
	@Path("/retrievecomments")
	@Produces(MediaType.TEXT_PLAIN)
	public String retrieveComments(@FormParam("post_id") String postID)
	{
		Comment obj = new Comment ();
		ArrayList<Comment>comments = new ArrayList<Comment>();
		comments = obj.retrieveComments(Integer.parseInt(postID));
		JSONObject json = new JSONObject();
		if(comments == null)return json.toJSONString();
		json.put("count", comments.size());
		for(int i =0 ; i<comments.size() ; i++)
		{
			json.put("id"+i, comments.get(i).getId());
			json.put("text"+i, comments.get(i).getText());
			json.put("date"+i, comments.get(i).getDate());
			json.put("usermail"+i, comments.get(i).getUser().getName());
		}
		return json.toJSONString();
	}
	
	@POST
	@Path("/deletecomment")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteComment(@FormParam("comment_id") String commentID)
	{
		Comment obj = new Comment ();
		boolean status = obj.deleteComment(Integer.parseInt(commentID));
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return json.toJSONString();
	}
	
	@POST
	@Path("/like")
	@Produces(MediaType.TEXT_PLAIN)
	public String like(@FormParam("user_id") String userID,@FormParam("post_id") String postID) 
	{
		Like obj = new Like ();
		int id = obj.performAction(Integer.parseInt(userID),Integer.parseInt(postID));
		JSONObject json = new JSONObject();
		json.put("id", id);
	    return json.toJSONString();
	}
	
	@POST
	@Path("/unlike")
	@Produces(MediaType.TEXT_PLAIN)
	public String unlike(@FormParam("user_id") String userID,@FormParam("post_id") String postID) 
	{
		Like obj = new Like ();
		boolean check = obj.unlike(Integer.parseInt(userID), Integer.parseInt(postID));
		JSONObject json = new JSONObject();
		json.put("status", check ? 1 : 0);
	    return json.toJSONString();
	}
	
	@POST
	@Path("/retrievelikes")
	@Produces(MediaType.TEXT_PLAIN)
	public String retrieveLikes(@FormParam("post_id") String postID)
	{
		Like obj = new Like ();
		ArrayList<Like>likes = new ArrayList<Like>();
		likes = obj.retrieveLikes(Integer.parseInt(postID));
		JSONObject json = new JSONObject();
		if(likes == null) return json.toJSONString();
		json.put("count", likes.size());
		for(int i =0 ; i<likes.size() ; i++)
		{
			json.put("id"+i, likes.get(i).getId());
			json.put("username"+i, likes.get(i).getUser().getName());
			json.put("userMail"+i, likes.get(i).getUser().getEmail());
		}
		return json.toJSONString();
	
	}
	
	@POST
	@Path("/showHomePage")
	@Produces(MediaType.TEXT_PLAIN)
	public String showHomePage(@FormParam("userID") String userID)
	{
		ArrayList<CheckIn> ch = new ArrayList<CheckIn>();
		CheckIn temp = new CheckIn();
		ch = temp.showHomePage(Integer.parseInt(userID));
		//System.out.println(ch.size());
		JSONObject json = new JSONObject();
		if(ch == null)return json.toJSONString();
		
		UserModel u = new UserModel();
			u.setId(Integer.parseInt(userID));
			String sortMethod = u.getSortMethod(Integer.parseInt(userID));
			if (sortMethod.contains("r")) sort = new ByRate() ;
			else if (sortMethod.contains("numCheckIns")) sort = new ByNumOfCheckins() ;
			else if (sortMethod.contains("nearby")) sort = new ByNearbyPlaces() ;
			
			ch = sort.sortBubble(ch);
			
		
		
		json.put("count", ch.size());
		for(int i = 0 ; i < ch.size() ; ++i)
		{
			json.put("postID"+i, ch.get(i).getId());
			json.put("postText"+i, ch.get(i).getText());
			json.put("postDateTime"+i, ch.get(i).getDate());
			json.put("userID"+i, ch.get(i).getUser().getId());
			json.put("placeID"+i, ch.get(i).getPlace().getId());
			ArrayList<Like> l = new ArrayList<Like>();
			Like temp1 = new Like();
			l = temp1.retrieveLikes(ch.get(i).getId());
			if(l == null)
				json.put("numOfLikes"+i, 0);
			else json.put("numOfLikes"+i, l.size());
			
		}
		return json.toJSONString();
	}

}
