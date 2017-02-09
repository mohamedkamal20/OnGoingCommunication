package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class UserModel {

	static public Connection conn = DBConnection.getActiveConnection();
	
	private String name;
	private String email;
	private String pass;
	private Integer id;
	private Double lat;
	private Double lon;

	public UserModel()
	{
		name = new String();
		email = new String();
		pass = new String();
		id = 0;
		lat = 0.0 ;
		lon = 0.0;
	}
	public String getPass() 
	{
		return pass;
	}

	public void setPass(String pass) 
	{
		this.pass = pass;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getEmail() 
	{
		return email;
	}

	public void setEmail(String email) 
	{
		this.email = email;
	}

	public Integer getId() 
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) 
	{
		this.lat = lat;
	}

	public Double getLon() 
	{
		return lon;
	}

	public void setLon(Double lon) 
	{
		this.lon = lon;
	}
	/**
	 * add new user the system , used when a user want to register 
	 * @param name
	 * @param email
	 * @param pass
	 * @return
	 */

	public static UserModel addNewUser(String name, String email, String pass) 
	{
		UserModel user = new UserModel();
		try 
		{
			String sql = "Insert into user (`USERNAME`,`USER_MAIL`,`USER_PASSWORD`) VALUES  (?,?,?)";		
			int id = Search_User(email);
			if(id != -1)
			{
				return user;
			}
			
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.setString(2, email);
			stmt.setString(3, pass);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) 
			{
				
				user.id = rs.getInt(1);
				user.email = email;
				user.pass = pass;
				user.name = name;
				user.lat = 0.0;
				user.lon = 0.0;
				return user;
			}
			return user;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * get access to your account if you have one 
	 * @param email
	 * @param pass
	 * @return
	 */

	public static UserModel login(String email, String pass) 
	{
		UserModel user = new UserModel();
		try 
		{
			String sql = "Select * from user where `USER_MAIL` = ? and `USER_PASSWORD` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, pass);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) 
			{
				user.id = rs.getInt(2);
				user.email = rs.getString("USER_MAIL");
				user.pass = rs.getString("USER_PASSWORD");
				user.name = rs.getString("USERNAME");
				user.lat = rs.getDouble("USER_LATITUDE");
				user.lon = rs.getDouble("USER_LANGITUDE");
				return user;
			}
			return user;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * update the longitude and the latitude of the user
	 * @param id
	 * @param lat
	 * @param lon
	 * @return
	 */

	public static boolean updateUserPosition(Integer id, Double lat, Double lon) 
	{
		try 
		{
			String sql = "Update user set `USER_LATITUDE` = ? , `USER_LANGITUDE` = ? where `USER_ID` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, lat);
			stmt.setDouble(2, lon);
			stmt.setInt(3, id);
			stmt.executeUpdate();
			return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * follow a user on the system to get their check-ins and interact with it 
	 * @param emails
	 * @param emaild
	 * @return
	 */
	public static boolean follow(String emails, String emaild) 
	{	
		int follower_id = Search_User(emails);
		int followed_id = Search_User(emaild);
		
		
		String follow_quary = "insert into follow values ('" + follower_id + "','"
				+ followed_id + "')";
		String check_already_following = "select s_id from follow where s_id = '"
				+ follower_id + "' and d_id = '" + followed_id + "'";
		
		java.sql.Statement stm = null;
		try 
		{
			stm = conn.createStatement(); 
		} 
		catch (SQLException e) {
			System.out.println("Statement Creation Error ! ");
		}

		int counter = 0; 
		ResultSet quarry_result;
		try {
			
				
			quarry_result = stm.executeQuery(check_already_following);
			
			while (quarry_result.next())
				counter++; 
		} catch (SQLException e) 
		{
			System.out.println("Problem finding if already following  !! ");
		}
		if (counter > 0) 
				return false;
		else 
		{
			try 
			{
				stm.executeUpdate(follow_quary); // add them to follow table
				return true;
			} 
			catch (SQLException e) 
			{
				System.out
						.println("Error Adding The Mails to follow table !! ");
			}
		}
		return false; 
						 
	}

	/**
	 * delete a user from your following list 
	 * @param emails
	 * @param emaild
	 * @return
	 */
	public static boolean unfollow(String emails, String emaild) 
	{
		int follower_id = Search_User(emails);
		int followed_id = Search_User(emaild);
		
		
		String unfollow_quary = "delete from follow where s_id = '"+follower_id+"' and d_id = '" +followed_id + "'";
		
		String check_already_following = "select d_id from follow where s_id = '"
				+ follower_id + "' and d_id = '" + followed_id + "'";
		
		
		java.sql.Statement stm = null;
		try 
		{
			stm = conn.createStatement(); // create the execution way in java
		} catch (SQLException e) {
			System.out.println("Statement Creation Error ! ");
		}

		int counter = 0; // counter for the result for the already following quarry
		ResultSet quarry_result;
		try 
		{
			// execute check if the	mails are already following
			quarry_result = stm.executeQuery(check_already_following); 
			while (quarry_result.next())
				counter++; // count for the quarry results
		} 
		catch (SQLException e) 
		{
			System.out.println("Problem finding if already following  or already following !! ");
		}

		if (counter == 0) // if counter == 0 then not  following return false now
			return false;
		else 
		{
			try 
			{
				stm.executeUpdate(unfollow_quary); // delete them from follow table
				return true;
			} 
			catch (SQLException e) 
			{
				System.out.println("Error Removing The Mails from follow table !! ");
			}
		}

		return false;

	}
	/**
	 * return all the users that you follow
	 * @param email
	 * @return
	 */

	public static ArrayList<UserModel> get_user_followers(String email) 
	{
		
		
		 System.out.println("Hello world ");

		ArrayList<UserModel> userFollowers = new ArrayList<UserModel>();

		int id = Search_User(email);
		if(id == -1) return null;
		
		String select_the_followers_list = "SELECT s_id FROM follow where d_id = '"+ id + "' ";
		
		int temp_id = -1;
		java.sql.Statement stm = null;

		try 
		{
			stm = conn.createStatement();
		} 
		catch (SQLException e) 
		{
			System.out.println("Error Creating Statement !! ");
		}

		ResultSet followers_mails = null;
		try 
		{
			followers_mails = stm.executeQuery(select_the_followers_list);
			      
		} 
		catch (SQLException e) 
		{
			System.out.println("Error Retrieveing The followers List !!!! ");
		}
		try 
		{
			while (followers_mails.next()) 
			{
				temp_id = followers_mails.getInt("s_id");
				String select_user_by_mail = "SELECT * FROM user WHERE USER_ID = '"	+ temp_id + "'";

				stm = conn.createStatement();
				ResultSet Follower_Object = null;
				Follower_Object = stm.executeQuery(select_user_by_mail);
				Follower_Object.first();

				UserModel object = new UserModel();
				object.id = temp_id;
				object.email = Follower_Object.getString("USER_MAIL");
				object.lat = Follower_Object.getDouble("USER_LATITUDE");
				object.lon = Follower_Object.getDouble("USER_LANGITUDE");
				object.pass = Follower_Object.getString("USER_PASSWORD");
				object.name = Follower_Object.getString("USERNAME");
				userFollowers.add(object);
			}
		} 
		catch (SQLException e) 
		{
			System.out.println(e.toString());
		}
		return userFollowers;
	}
	/**
	 * get the logitude and the latitude of a user 
	 * @param email
	 * @return
	 */
	
	public static UserModel get_user_last_position(String email) 
	{
		UserModel user = new UserModel();
		
		Connection conn = DBConnection.getActiveConnection();
		
		String select_user = "select* from user where USER_MAIL = '"+email+"'";
		
		try 
		{
			java.sql.Statement stm = conn.createStatement() ;
			ResultSet result = stm.executeQuery(select_user);
			result.first();
			user.id = result.getInt(2);
			user.name = result.getString("USERNAME");
			user.email = email ;
			user.pass = result.getString("USER_PASSWORD");
			user.lat = result.getDouble("USER_LATITUDE");
			user.lon = result.getDouble("USER_LANGITUDE");
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	
	/**
	 * search for a user in the system , takes the email and return the id of this user if exists 
	 * @param mail
	 * @return
	 */
	
	public static int Search_User(String mail)
	{
		int id = -1;
		
		String quarry = "Select USER_ID from user where USER_MAIL = '"+mail+"'" ;
		try 
		{
			java.sql.Statement stm = conn.createStatement() ;
			ResultSet results = stm.executeQuery(quarry);
			results.first() ;
			if(results != null) 
			{
				id = results.getInt("USER_ID");
			}
			return id ;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return id ;
	}
	
	public static UserModel search(String email) 
	{
		UserModel user= new UserModel();
		int id = Search_User(email);
		if(id == -1)
		{
			return user;
		}
		try 
		{
			String sql = "Select * from user where `USER_MAIL` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) 
			{
				user.id = rs.getInt(2);
				user.email = rs.getString("USER_MAIL");
				user.pass = rs.getString("USER_PASSWORD");
				user.name = rs.getString("USERNAME");
				user.lat = rs.getDouble("USER_LATITUDE");
				user.lon = rs.getDouble("USER_LANGITUDE");
				return user;
			}
			return user;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * takes the id of the user and returns all the infromation of this user
	 * @param id
	 * @return
	 */
	public static UserModel searchByID(int id)
	{
		UserModel user = new UserModel();
		try 
		{
			String sql = "Select * from user where `USER_ID` = ?";
			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) 
			{
				user.id = rs.getInt(2);
				user.email = rs.getString("USER_MAIL");
				user.pass = rs.getString("USER_PASSWORD");
				user.name = rs.getString("USERNAME");
				user.lat = rs.getDouble("USER_LATITUDE");
				user.lon = rs.getDouble("USER_LANGITUDE");
				return user;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void setSortMethod(String method) {
		
		String insert = "insert into user (sortMethod) "
				+ "values ('"+method+"')";
		Statement stm = null ;
		try{
			stm = (Statement) conn.createStatement();
			stm.executeUpdate(insert);
		}
		
		catch (SQLException e) 
		{
			System.out.println(e.toString());
			System.out.println("Error in insert methodSort");
		}
	}
	
	public String getSortMethod(int userid) {
		String sql = "select sortMethod from user where USER_ID = '"+userid+"' " ;
		String s = "" ;
		try {	
			Statement stm = (Statement) conn.createStatement();
			ResultSet res = stm.executeQuery(sql);
			CheckIn ch = new CheckIn();
		    if(res.next()){
		    	s = res.getString("sortMethod");
		    }
		    else {
		    	return "" ;
		    }
		}
			catch (SQLException e){
				System.out.println("Error in RETREING methodSort");
			}
		
		return null;
	}
	
}
