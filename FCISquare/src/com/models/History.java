package com.models;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
*
* @author yara mansour
*/
public class History {
	String text ;
	String date;
	int id ;
	
	 /**
     * @owner yara mansour 
     * history constructor
     */
	public History ()
	{
		text = new String();
		date = new String();
		id = 0;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	static public Connection conn = DBConnection.getActiveConnection();
	
	 /**
     * @owner yara mansour 
     * add to the history of a user that he likes a certain post 
     * takes 2 parameters the id of the like and the user id 
     * @param int id
     * @param int userID
     */
	
	public void likeHistory(int id , int userID){
		String text ="you liked a check-in " ;
		try 
		{
			String sql = "INSERT INTO history (`ACTION_ID`,`USER_ID`,`TYPE`,`TEXT`,`DATE_TIME`) values(?,?,?,?,current_timestamp())";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.setInt(2, userID);
			stmt.setString(3, "like");
			stmt.setString(4, text);
			stmt.executeUpdate();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}
	 /**
     * @owner yara mansour 
     * add to the history of a user that he commented on a certain post 
     * takes 3 parameters the id of the comment and the text of the comment and the user id 
     * @param Strig commentText
     * @param int id
     * @param int userID
     */
    public void commentHistory(int id ,String commentText, int userID){
    	String text ="you commented : "+commentText+" on  a check-in " ;
		try 
		{
			String sql = "INSERT INTO history (`ACTION_ID`,`USER_ID`,`TYPE`,`TEXT`,`DATE_TIME`) values(?,?,?,?,current_timestamp())";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.setInt(2, userID);
			stmt.setString(3, "comment");
			stmt.setString(4, text);
			stmt.executeUpdate();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}
    /**
     * @owner yara mansour 
     * add to the history of a user that he made a check-in
     * takes 2 parameters the id of the check-in and the user id 
     * @param int id
     * @param int userID
     */
    public void checkinHistory(int id, int userID){
    	String text ="you checked in " ;
		try 
		{
			String sql = "INSERT INTO history (`ACTION_ID`,`USER_ID`,`TYPE`,`TEXT`,`DATE_TIME`) values(?,?,?,?,current_timestamp())";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.setInt(2, userID);
			stmt.setString(3, "check-in");
			stmt.setString(4, text);
			stmt.executeUpdate();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}
    /**
     * @owner yara mansour 
     * add to the history of a user that he add a place to his list of favourites 
     * takes 2 parameters the id of the place and the user id 
     * @param int id
     * @param int userID
     */
    public void savePlaceHistory(int id, int userID){
    	String text ="you saved a place to your favourites " ;
		try 
		{
			String sql = "INSERT INTO history (`ACTION_ID`,`USER_ID`,`TYPE`,`TEXT`,`DATE_TIME`) values(?,?,?,?,current_timestamp())";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.setInt(2, userID);
			stmt.setString(3, "savePlace");
			stmt.setString(4, text);
			stmt.executeUpdate();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}
    /**
     * @owner yara mansour 
     * return all the activities of the user 
     * takes one parameter  user id 
     * @param int usreID
     * @return history 
     */
    public ArrayList<History> retrieveHistory ( int userID){
    	ArrayList<History> history = new ArrayList<History>();
		History h = null;
		try 
		{
			String sql = "Select * from history where `USER_ID` = ?" ;
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) 
			{
				h = new History();
				h.id = rs.getInt(1);
				h.text = rs.getString("TEXT");
				h.date = rs.getString("DATE_TIME");	
				
				history.add(h);
			}
			return history;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
    	
    }
    /**
     * @owner yara mansour 
     * add to the history of a user that he likes a certain post 
     * takes one parameter the id of the history 
     * @param int id
     */
   
    public boolean deleteHistory(int id){
    	try {
    		Connection conn = DBConnection.getActiveConnection();
    		String sql = "delete from history where `HISTORY_ID`= ? ";
    		PreparedStatement stmt;
    		stmt = conn.prepareStatement(sql);
    		stmt.setInt(1, id);
    		stmt.executeUpdate();
    		return true;
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return false ;
    }	


}
