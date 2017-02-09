package com.services.com.services;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.services.PlaceController;

public class PlaceControllerTest 
{
  @Test
  public void addPlace() 
  {
	  PlaceController place = new PlaceController();
	  String result = place.addPlace("6 october", "12", "43", "calm city");
	  JSONObject json = new JSONObject();
	  json.put("status", "not done");
			
	  Assert.assertEquals(json.toJSONString(), result);
	  
  }
  
  @Test
  public void savePlace() 
  {
	  PlaceController place = new PlaceController();
	  String result = place.savePlace("6 october", 2);
	  JSONObject json = new JSONObject();
	  json.put("status", "not done");
			
	  Assert.assertEquals(json.toJSONString(), result);
  }
}
