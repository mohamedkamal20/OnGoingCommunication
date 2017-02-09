package com.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

import com.models.CheckIn;
import com.models.CommentNotifications;
import com.models.DBConnection;
import com.models.Like;
import com.models.LikeNotifications;
import com.models.UserModel;
import com.models.Notifications;

/**
 * @author Mohamed
 * NotificationController 
 * contains all services that are responsible for all notifications actions 
 * such adding notifications or retrieving ...
 */

@Path("/")
public class NotificationController 
{
	public Notifications notification = null ;
	
	/*
	public NotificationController (Notifications n)
	{
		notification = n ;
	}
	*/
	/*
	public void add(CheckIn ch , UserModel u){
		notification.add(ch , u);
	}
	*/
	
	///////
	
	@POST
	@Path("/getAllNotifications")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllNotifications(@FormParam("userID") String userID)
	{
		UserModel u = new UserModel();
		u.setId(Integer.parseInt(userID));
		return getAllNotifications(u);
	
	}
	
	
	String getAllNotifications(UserModel u){ // likes 5as b table notifications likes fe le database w comments 5as b table comments notifications
		ArrayList<Notifications> likes = new ArrayList<Notifications>();
		ArrayList<Notifications> comments = new ArrayList<Notifications>();
		
		notification = new LikeNotifications();
		likes = notification.getNotifications(u);
		
		notification = new CommentNotifications();
		comments = notification.getNotifications(u);
		
		JSONObject json = new JSONObject();

		json.put("count", comments.size() + likes.size());
		for(int i =0 ; i<likes.size() ; i++)
		{
			
			json.put("notification_id"+i, likes.get(i).getId());
			json.put("description"+i, likes.get(i).getDescription());
			json.put("date"+i, likes.get(i).getDate());
			json.put("postid"+i, likes.get(i).getPost().getId());
		}
		
		for(int i =0 ; i<comments.size() ; i++)
		{
			json.put("notification_id"+i, comments.get(i).getId());
			json.put("description"+i, comments.get(i).getDescription());
			json.put("date"+i, comments.get(i).getDate());
			json.put("postid"+i, comments.get(i).getPost().getId());
		}
		// hna t3red el likes w el comments fe JSON b2a 
		return json.toJSONString();
	}
	
	
	CheckIn respond(int notificationID )
	{
		
		return null;	
	}

}
