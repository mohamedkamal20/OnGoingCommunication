package com.models;

import java.util.ArrayList;

public class ByNumOfCheckins implements SortingMethod{
	
	@Override
	public ArrayList<CheckIn> sortBubble(ArrayList<CheckIn> arr) {
		
		for(int i=0;i<arr.size();i++)
	    {
	        for(int j=0;j<arr.size()-1;j++)
	        {
	        	if ( i == 0){
	        	arr.get(j).place.searchPlace(arr.get(j).place.id);
	        	arr.get(j+1).place.searchPlace(arr.get(j+1).place.id);
	        	}
	        	
	            if(arr.get(j).place.numOfCheckins > arr.get(j+1).place.numOfCheckins )
	            { // m7tag a3mel search place b el id 34an el place object ykon mlyan 
	            	CheckIn ch = arr.get(j);
	            	arr.set(j, arr.get(j+1));
	            	arr.set(j+1,ch);
	            }
	        }
	    }
		return arr;
	}
}
