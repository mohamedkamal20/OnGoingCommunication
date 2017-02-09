package com.models;

import java.sql.Connection;
import java.util.ArrayList;

public interface Notifications 
{
	static public Connection conn = DBConnection.getActiveConnection();
	
	void add(CheckIn ch, UserModel u);
	ArrayList<Notifications> getNotifications(UserModel u);
	
	public int getId() ;
	public void setId(int id) ;
	public CheckIn getPost() ;
	public void setPost(CheckIn post) ;
	public String getDescription();
	public String getDate();

}
