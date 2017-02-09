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
import com.models.DBConnection;
import com.models.UserModel;

/**
 * @author Mohamed
 * @check In controller 
 * contains all services that are responsible for User actions
 * such signing up , signing in , following ...
 */

@Path("/")
public class UserController {


	@POST
	@Path("/signup")
	@Produces(MediaType.TEXT_PLAIN)
	public String signUp(@FormParam("name") String name,@FormParam("email") String email, @FormParam("pass") String pass) 
	{
		
		UserModel user = UserModel.addNewUser(name, email, pass);
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("pass", user.getPass());
		json.put("lat", user.getLat());
		json.put("long", user.getLon());
		return json.toJSONString();
	}

	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@FormParam("email") String email,@FormParam("pass") String pass) 
	{
		UserModel user = UserModel.login(email, pass);
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("pass", user.getPass());
		json.put("lat", user.getLat());
		json.put("long", user.getLon());
		return json.toJSONString();
	}
	
	@POST
	@Path("/updatePosition")
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePosition(@FormParam("id") String id,
			@FormParam("lat") String lat, @FormParam("long") String lon) {
		Boolean status = UserModel.updateUserPosition(Integer.parseInt(id), Double.parseDouble(lat), Double.parseDouble(lon));
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return json.toJSONString();
	}
	
	@POST
	@Path("/follow")
	@Produces(MediaType.TEXT_PLAIN)
	public String follow(@FormParam("email1") String emails ,@FormParam("email2") String emaild) 
	{
		Boolean status = UserModel.follow(emails, emaild);
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return  json.toJSONString();
	}
	
	@POST
	@Path("/unfollow")
	@Produces(MediaType.TEXT_PLAIN)
	public String unfollow(@FormParam("email1") String emails ,@FormParam("email2") String emaild) 
	{
		Boolean status = UserModel.unfollow(emails, emaild);
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return json.toJSONString();
	}
	
	@POST
	@Path("/get-followers")
	@Produces(MediaType.TEXT_PLAIN)
	public String get_followers(@FormParam("email") String email) 
	{
		ArrayList<UserModel> userFollowers = UserModel.get_user_followers(email);
		JSONObject json = new JSONObject();
		if(userFollowers == null)return json.toJSONString();
		json.put("count", userFollowers.size());
		for(int i = 0 ; i < userFollowers.size() ; ++i)
		{
			json.put("id"+(i+1), userFollowers.get(i).getId());
			json.put("name"+(i+1), userFollowers.get(i).getName());
			json.put("email"+(i+1), userFollowers.get(i).getEmail());
			json.put("pass"+(i+1), userFollowers.get(i).getPass());
			json.put("lat"+(i+1), userFollowers.get(i).getLat());
			json.put("long"+(i+1), userFollowers.get(i).getLon());
		}
		
		return json.toJSONString();
	}
	
	@POST
	@Path("/get-user-last-position")
	@Produces(MediaType.TEXT_PLAIN)
	public String get_user_last_position(@FormParam("email") String email) 
	{
		UserModel user = UserModel.get_user_last_position(email);
		JSONObject json = new JSONObject();
		json.put("lat", user.getLat());
		json.put("long", user.getLon());
		
		return json.toJSONString();
	}
	
	@POST
	@Path("/search-user")
	@Produces(MediaType.TEXT_PLAIN)
	public String search_user (@FormParam("email") String email) 
	{
		UserModel user = UserModel.search(email);
		JSONObject json = new JSONObject();
		json.put("id", user.getId());
		json.put("name", user.getName());
		json.put("email", user.getEmail());
		json.put("pass", user.getPass());
		json.put("lat", user.getLat());
		json.put("long", user.getLon());
		
		return json.toJSONString();
	}
	
	@POST
	@Path("/check-follow")
	@Produces(MediaType.TEXT_PLAIN)
	public String check_follow (@FormParam("email1") String emails ,@FormParam("email2") String emaild) 
	{
		boolean status = /*UserModel.check_follow(emails, emaild)*/ true;
		JSONObject json = new JSONObject();
		json.put("status", status ? 1 : 0);
		return  json.toJSONString();
		
	}
	

	
	@GET
	@Path("/")
	@Produces(MediaType.TEXT_PLAIN)
	public String getJson() 
	{
		return "Hello after editing ^_^ !!";
		// Connection URL:
		// mysql://$OPENSHIFT_MYSQL_DB_HOST:$OPENSHIFT_MYSQL_DB_PORT/
	}
	
}
