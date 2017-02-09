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

public class Comment extends Actions{
	static public Connection conn = DBConnection.getActiveConnection();
	String text ;
	String date ;
	CheckIn check ;
	
	/**
     * @owner yara mansour 
     * comment constructor 
     */
	
	public Comment()
	{
		id = 0;
		text = new String();
		date= new String();
		user = new UserModel();
		check = new CheckIn();
	}
	public String getText() 
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	public String getDate() 
	{
		return date;
	}
	public void setDate(String date) 
	{
		this.date = date;
	}
	
	/**
	 * delete a comment on a post 
	 * @param id
	 * @return
	 */
	public boolean deleteComment(int id)
	{
		try 
		{
			Connection conn = DBConnection.getActiveConnection();
			String sql = "delete from comment where `COMMENT_ID`= ? ";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.executeUpdate();
			return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false ;
	}
	
	/**
	 * retrieve all the comments of a certain post
	 * @param postID
	 * @return
	 */
	public ArrayList<Comment> retrieveComments (int postID)
	{
		ArrayList<Comment> comments = new ArrayList<Comment>();
		Comment comment = null;
		try 
		{
			String sql = "Select COMMENT_ID,COMMENT_TEXT,COMMENT_TIME_DATE,USER_MAIL from comment , user "
					+ "where `POST_ID` = '" + postID + "'  AND comment.USER_ID =user.USER_ID" ;
			
				java.sql.Statement stmt = null;
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				
				while (rs.next()) 
				{
					comment = new Comment();
					comment.id = rs.getInt(1);
					comment.text = rs.getString("COMMENT_TEXT");
					comment.date = rs.getString("COMMENT_TIME_DATE");	
					comment.user = UserModel.search(rs.getString("USER_MAIL")) ;
					
					comments.add(comment);
				}
				return comments;
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * to comment on a post of a friend 
	 * @param text
	 * @param userID
	 * @param postID
	 * @return
	 */
	public int performAction(String text, int userID, int postID) 
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String d  = dateFormat.format(cal.getTime());
		CommentNotifications nc = new CommentNotifications();
		nc.setDate(d);
		UserModel u = new UserModel();
		u.setId(userID);
		CheckIn ch = new CheckIn();
		ch.setId(postID);
		nc.add(ch, u);

		try 
		{
			
			String sql = "INSERT INTO comment (`USER_ID`,`POST_ID`,`COMMENT_TEXT`,`COMMENT_TIME_DATE`) values(?,?,?,?)";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, userID);
			stmt.setInt(2, postID);
			stmt.setString(3, text);
			String date  = new String ();
			dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			cal = Calendar.getInstance();
			date = dateFormat.format(cal.getTime());
			stmt.setString(4, date);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next())
			{
				int id = rs.getInt(1);
				History h = new History();
				h.commentHistory(id,text, userID);
				
			    return id;
		     }
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return -1;	
	}
	
	

}
