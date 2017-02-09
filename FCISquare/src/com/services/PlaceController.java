
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
import com.models.Place;
import com.models.UserModel;

/**
 * @author Mohamed
 * PlaceController 
 * contains all services that are responsible for all place actions
 * such adding places , saving places ..
 */

@Path("/")
public class PlaceController 
{
	
	
	@POST
	@Path("/addPlace")
	@Produces(MediaType.TEXT_PLAIN)
	public String addPlace(@FormParam("name") String name,@FormParam("lat") String lat, @FormParam("long") String long_,@FormParam("description") String description) 
	{
		
		Place place = new Place();
		place.setName(name);
		place.setDescription(description);
		place.setLatitude(Double.parseDouble(lat));
		place.setLongtude(Double.parseDouble(long_));
		place = place.addPlace();
		JSONObject json = new JSONObject();
		if(place == null) 
		{
			json.put("status", "not done");
		}
		else json.put("status", "done");
		return json.toJSONString();
	}
	
	@POST
	@Path("/savePlace")
	@Produces(MediaType.TEXT_PLAIN)
	public String savePlace(@FormParam("name") String name , @FormParam("userID") int userID) 
	{
		Place place = new Place();
		place.setName(name);
		UserModel user = new UserModel();
		user.setId(userID);
		boolean check = place.savePlace(user);
		JSONObject json = new JSONObject();
		if(check == false) 
		{
			json.put("status", "not done");
		}
		else
		{
			json.put("status", "done");
		}
		return json.toJSONString();
	}
	
	
	
	

}
