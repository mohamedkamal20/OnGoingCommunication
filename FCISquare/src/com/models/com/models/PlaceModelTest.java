package com.models.com.models;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.models.Place;
import com.models.UserModel;

public class PlaceModelTest 
{
  @Test
  public void addPlace() 
  {
	  	Place place = new Place();
	  	Place result = new Place();
		place.setName("6 october");
		place.setDescription("pretty city");
		place.setLatitude(12);
		place.setLongtude(23);
		result = place.addPlace();
		
		Assert.assertEquals(null , result );
  }
  
  @Test
  public void savePlace() 
  {
	  	Place place = new Place();
		place.setName("dahab");
		UserModel user = new UserModel();
		user.setId(2);
		boolean check = place.savePlace(user);
		
		Assert.assertEquals(false, check);
  }
  
  @Test
  public void searchPlace() 
  {
	  	Place place = new Place();
		Place result = new Place();
	  	place.setName("dahab");
		result = place.searchPlace();
		
		Assert.assertEquals(null, result);
  }
  
}
