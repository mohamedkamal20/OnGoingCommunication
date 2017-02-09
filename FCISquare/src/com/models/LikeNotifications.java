package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;


public class LikeNotifications implements Notifications{
	int id ;
	CheckIn post ;
	String description ;
	String date ;
	
	
	
	public LikeNotifications()
	{
		id = 0 ;
		description = "there is a like on "; 
		post = new CheckIn() ; 
	}
	
	
	public int getId() {
		return id;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setId(int id) {
		this.id = id;
	}
	public CheckIn getPost() {
		return post;
	}
	public void setPost(CheckIn post) {
		this.post = post;
	}
	
	public void setDescription(String des) {
		this.description = des;
	}
	public String getDescription(){
		return description ;
	}
	
	public String getDate(){
		return date ;
	}
	
	@Override
	/**
	 * takes the like a user made on a certain post and add it to the notification to be recieved by the user who made the check-in
	 * @param ch 
	 * @param u
	 */
	public void add(CheckIn ch, UserModel u ) {
		int userid = u.getId();
		int postid = ch.getId();
		int flag = 1 ;
		
		ch = ch.retrieveCheckIn(ch.id);
		ch.place.searchPlace(ch.place.id);
		description = description + ch.getPlace().getName() + "'s check in" ;

		String insert = "insert into Notification(USER_ID ,NOTIFICATION_DESCRIPTION,NOTIFICATION_DATE_TIME ,POST_ID, flag )"
				+ "VALUES ('"+userid+"','"+description+"','"+this.date + "' , '" + postid +"','"+flag+"')" ;
		try 
		{	
			Statement stm = (Statement) conn.createStatement();
			stm.executeUpdate(insert);
			
			String updateSeen = "update follow_post set like_seen = '"+flag+"' where POST_ID = '"+postid+"'"; 
			try{
			stm = (Statement) conn.createStatement();
			stm.executeUpdate(updateSeen);
			}
			catch (SQLException e) 
			{
				System.out.println("Error in update fe like notification");
			}
		} 
		catch (SQLException e) 
		{
			System.out.println(e.toString());
			System.out.println("Error in insert like notification");
		}
		
	}

	@Override
	/**
	 * retrieve all the comment notifications for a user 
	 * @param u
	 */
	public ArrayList<Notifications> getNotifications(UserModel u) {
		int userid = u.getId() ;
		int seen = 1 ;
		int type = 1 ;
		
		String query = "select NOTIFICATION_ID , notification.USER_ID , NOTIFICATION_DESCRIPTION , NOTIFICATION_DATE_TIME , notification.POST_ID "
				+ "from follow_post inner join notification on follow_post.POST_ID = notification.POST_ID "
				+ "where follow_post.USER_ID = '"+userid+"' and like_seen = '"+seen+"' and flag = '"+type+"'";
		
		try{
			
			Statement stm = (Statement) conn.createStatement();
			ResultSet res = stm.executeQuery(query);
			ArrayList <Notifications> arr = new ArrayList<Notifications>();
			while (res.next())
			{
				LikeNotifications comm = new LikeNotifications();
				comm.id = res.getInt("NOTIFICATION_ID");
				comm.description = res.getString("NOTIFICATION_DESCRIPTION");
				comm.date = res.getString("NOTIFICATION_DATE_TIME");
				comm.post.id = res.getInt("POST_ID");
				
				arr.add(comm);
			}
			
			seen = 0 ;
			String update = "updat e follow_post set like_seen = '"+seen+"' where follow_post.USER_ID = '"+userid+"'";
			try {
				stm = (Statement) conn.createStatement();
				stm.executeUpdate(update);
				
			}
			catch (SQLException e) 
			{
				System.out.println("Error in Update el SEEN ");
			}
			
			return arr ;
		}
		
		catch (SQLException e) 
		{
			System.out.println(e.toString());
			System.out.println("Error in GET LIKE notification");
		}
		
		return null;
	}
		
	
}
