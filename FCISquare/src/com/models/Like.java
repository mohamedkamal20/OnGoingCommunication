package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.mysql.jdbc.Statement;

/**
*
* @author yara mansour
*/
public class Like extends Actions{
	static public Connection conn = DBConnection.getActiveConnection();
	
	 /**
     * @owner yara mansour 
     * performs the action of like (to like a post )
     * takes 2 parameters the post id and the user id 
     * @param int userID 
     * @param int postID
     */

	public int performAction(int userID , int postID) 
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String d  = dateFormat.format(cal.getTime());
		LikeNotifications nc = new  LikeNotifications(); 
		nc.setDate(d);
		UserModel u = new UserModel();
		u.setId(userID);
		CheckIn ch = new CheckIn();
		ch.setId(postID);
		nc.add(ch, u);
		
		try 
		{
			String sql = "INSERT INTO likes (`USER_ID`,`POST_ID`,`LIKE_DATE_TIME`)values(?,?,?)";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, userID);
			stmt.setInt(2, postID);
			String date  = new String ();
			dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			cal = Calendar.getInstance();
			date = dateFormat.format(cal.getTime());
			stmt.setString(3, date);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next())
			{
				int id = rs.getInt(1);
				History h = new History();
				h.likeHistory(id, userID);
			
			    return id;
		     }
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return -1;
	}
	
	 /**
     * @owner yara mansour 
     * delete like of a certain post 
     * takes 2 parameters the post id and the user id 
     * @param int userID 
     * @param int postID
     */

	public boolean unlike(int userID , int postID)
	{
		String sql = "delete from likes where USER_ID = '" +userID + "' "
				+ " and '" + postID+ "' ";	
		PreparedStatement stmt;
		try 
		{
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next())
			{
			    return false;
		    }
			else return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false ;
		
	}
	
	 /**
     * @owner yara mansour 
     * returns the likes of a certain post
     * takes one parameter the post id 
     * @param int postID
     * @return likes 
     */
	public ArrayList<Like> retrieveLikes (int postID){
		ArrayList<Like> likes = new ArrayList<Like>();
		Like like = null;
		try 
		{
			
			String sql = "Select LIKE_ID,USER_MAIL from likes inner join user on likes.USER_ID = user.USER_ID  where POST_ID = '"+ postID +"' " ;
			
			java.sql.Statement stmt = null;
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				
				like = new Like();
				like.id = rs.getInt(1);	
				like.user = UserModel.search(rs.getString(2)) ;
				
				likes.add(like);
			}
			return likes;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
		
	}
	

}
