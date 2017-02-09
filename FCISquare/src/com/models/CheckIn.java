package com.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;
/**
*
* @author mohamed safaa
*/
public class CheckIn 
{
	
	static public Connection conn = DBConnection.getActiveConnection();


	int id ;
	String text ;
	String date ;
	Place place ;
	UserModel user ;
	ArrayList<Comment>comments = new ArrayList<Comment>();
	ArrayList<Like>likes = new ArrayList<Like>();
	
	/**
	 * @Owner mohamed safaa
	 * check-in constructor
	 */
	
	public CheckIn()
	{
		id = 0;
		user = new UserModel();
		place = new  Place();
		date = new String();
		text =new String();
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
	public Place getPlace() {
		return place;
	}
	public void setPlace(Place place) {
		this.place = place;
	}
	public UserModel getUser() {
		return user;
	}
	public void setUser(UserModel user) {
		this.user = user;
	}
	
	/**
	 * to check-in in a place in the system 
	 * @return
	 */
	public CheckIn checkIn()
	{
		if (place.searchPlace() == null) 
		{
			System.out.println("Not Found Place");
			return null ;
		}
		CheckIn ch = searchCheckIn(place, user) ;
		if (ch != null)
		{
			deleteCheckIn(ch);
		}
			
		int userid = user.getId();
		int placeid = place.getId();	
		String insert = "Insert into check_in  (USER_ID ,PLACE_ID , POST_tEXT,POST_DATE_TIME) VALUES ('"+userid+"','"+placeid+"','"+text+"','"+date+"') " ;
		try 
		{	
			Statement stm = (Statement) conn.createStatement();
			int id = stm.executeUpdate(insert);
			
			System.out.println(id);	
			String update = "Update place set place.PLACE_CHECK_IN = place.PLACE_CHECK_IN +1 where PLACE_ID = '"+placeid+"'";
			try
			{
				stm = (Statement) conn.createStatement();
				stm.executeUpdate(update);
				System.out.println(1);
				CheckIn ch1 = searchCheckIn(place, user) ;
				int flag = 0 ;
				int postid = ch1.id ;
				History h = new History();
				h.checkinHistory(postid, userid);
				String insertFollow = "insert into follow_post (USER_ID , POST_ID , comment_seen , like_seen) "
						+ "values ('"+userid+"' , '"+postid+"', '"+flag+"' , '"+flag+"')";
				 stm = null ;
				try
				{
					stm = (Statement) conn.createStatement();
					stm.executeUpdate(insertFollow);
				}
				
				catch (SQLException e) 
				{
					System.out.println(e.toString());
					System.out.println("Error in insert follow_post");
				}
				
				return this ;
			}
			catch (SQLException e)
			{
				System.out.println(e.toString());
				System.out.println("Error in update");
			}
		} 
		catch (SQLException e) 
		{
			System.out.println(e.toString());
			System.out.println("Error in insert");
		}
		
		return null;
	}
	/**
	 * to search for a checkin in the system by the user and the place 
	 * @param p
	 * @param u
	 * @return
	 */

	public CheckIn searchCheckIn(Place p , UserModel u){
		int userid = u.getId();
		int placeid = p.getId();
		
		String select = "select* from check_in where USER_ID = '"+userid+"' and PLACE_ID = '"+placeid+"' " ;
		Statement stm = null ;
		ResultSet res = null ;
		try 
		{	
			stm = (Statement) conn.createStatement();
			res = stm.executeQuery(select);
		    if(res.next())
		    {
		    	System.out.println("found result in select check in");
		    	 id = res.getInt("POST_ID");
		    	 text = res.getString("POST_TEXT");
		    	 date = res.getString("POST_DATE_TIME");
		    	 return this ;
		    }
		    
		    	return null ;
		    
		}
		catch (SQLException e)
		{
				System.out.println("Error in search");
		}
		
		return this ;
	}
	/**
	 * to delete a check-in that u made earlier
	 * @param ch
	 * @return
	 */


public boolean deleteCheckIn(CheckIn ch){
		String delete = "delete from Check_In where POST_ID = '"+ch.id+"'" ;
		Statement stm = null ;
		try 
		{	
			stm = (Statement) conn.createStatement();
		    stm.executeUpdate(delete);
		}
		catch (SQLException e){
			System.out.println("Error in Delete check in");
			return false ;
		}
		
		String update = "Update place set place.PLACE_CHECK_IN = place.PLACE_CHECK_IN -1 where PLACE_ID = '"+ch.place.getId()+"'";
		try
		{
			stm = (Statement) conn.createStatement();
			stm.executeUpdate(update);
			return true ;
		}
		catch (SQLException e)
		{
			System.out.println(e.toString());
			System.out.println("Error in update check on fe el delete");
			return false ;
		}
	}

/**
 * returns a particular post 
 * @param checkInId
 * @return
 */

public CheckIn retrieveCheckIn(int checkInId){
	String select = "select* from check_in where POST_ID = '"+checkInId+"' " ;
	try {	
		Statement stm = (Statement) conn.createStatement();
		ResultSet res = stm.executeQuery(select);
		CheckIn ch = new CheckIn();
	    if(res.next()){
	    	 ch.id = res.getInt("POST_ID");
	    	 ch.text = res.getString("POST_TEXT");
	    	 ch.date = res.getString("POST_DATE_TIME");
	    	 ch.user.setId(res.getInt("USER_ID"));
	    	 ch.place.id = res.getInt("PLACE_ID");
	    	 
	    	 return ch ;
	    }
	    else {
	    	return null ;
	    }
	}
		catch (SQLException e){
			System.out.println("Error in RETREING POST");
		}
	return null;	
	
}

/**
 * retrieve all the posts( check-in) of the users that you follow to show them in your homepage
 * @param userid
 * @return
 */
public ArrayList<CheckIn> showHomePage(int userid){
	
	ArrayList<CheckIn> checkins = new ArrayList<CheckIn>();

	String select_the_followers_list = "SELECT d_id FROM follow where s_id = '"+ userid + "' ";
	
	Statement stm = null;

	try 
	{
		stm = (Statement) conn.createStatement();
	} 
	catch (SQLException e) 
	{
		System.out.println("Error Creating Statement !! ");
	}

	ResultSet followered_ids = null;
	try 
	{
		followered_ids = stm.executeQuery(select_the_followers_list);
		      
	} 
	catch (SQLException e) 
	{
		System.out.println("Error Retrieveing The followered List !!!! ");
	}
	try 
	{
		int tempId ;
		ResultSet checkin = null; 
		while (followered_ids.next()) 
		{
			tempId = followered_ids.getInt("d_id");
			String ret = "select* from check_in where USER_ID = '"+tempId+"'" ;
			stm = (Statement) conn.createStatement();
			checkin = stm.executeQuery(ret);
			while(checkin.next())
			{
				CheckIn object = new CheckIn();
				object.id = checkin.getInt("POST_ID");
				object.text = checkin.getString("POST_TEXT");
				object.user = UserModel.searchByID(checkin.getInt("USER_ID"));
				object.date = checkin.getString("POST_DATE_TIME");
				object.place.id = checkin.getInt("PLACE_ID");
				
				checkins.add(object);
			}
		}
		
		if (checkins.size()!=0) return checkins ;
	}
	catch (SQLException e) 
	{
		System.out.println("Error Retrieving POSTS !!!! ");
	}

	return null ;

}

	
}
