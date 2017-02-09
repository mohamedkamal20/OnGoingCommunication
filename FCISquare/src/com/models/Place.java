package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

/**
 *
 * @author mohamed kamal
 */

public class Place {

	static public Connection conn = DBConnection.getActiveConnection();

	String name;
	String description;
	int id;
	double longitude;
	double latitude;
	int numOfCheckins;
	int rate;
	int numOfSaves;

	/**
	 * @owner mohamed kamal place constructor
	 */
	public Place() {
		id = rate = numOfCheckins = numOfSaves = 0;
		longitude = latitude = 0.0;
		name = new String("dummy");
		description = new String("I love this place :D ..");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLongtude() {
		return longitude;
	}

	public void setLongtude(double longtude) {
		this.longitude = longtude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getNumOfCheckins() {
		return numOfCheckins;
	}

	public void setNumOfCheckins(int numOfCheckins) {
		this.numOfCheckins = numOfCheckins;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	/**
	 * @owner mohamed kamal add a new place to the system takes no parameters
	 */

	public Place addPlace() {
		try {
			if (searchPlace() != null)
				return null;
			String sql = "Insert into place (`place_name`,`place_rate`,`place_check_in` ,`place_description` , `place_number_of_saves`,`latitude` ,`longitude`  ) VALUES  (?,?,?,?,?,?,?)";

			PreparedStatement stmt;
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.setInt(2, rate);
			stmt.setInt(3, numOfCheckins);
			stmt.setString(4, description);
			stmt.setInt(5, numOfSaves);

			stmt.setDouble(6, latitude);
			stmt.setDouble(7, longitude);

			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
				return this;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public int getNumOfSaves() {
		return numOfSaves;
	}

	public void setNumOfSaves(int numOfSaves) {
		this.numOfSaves = numOfSaves;
	}

	/**
	 * @owner mohamed kamal search for a place in the system
	 */

	public Place searchPlace() {

		try {

			String sql = "select * from place where PLACE_NAME = '" + name
					+ "' ";

			int count = 0;
			java.sql.Statement stm = conn.createStatement();
			ResultSet results = stm.executeQuery(sql);
			results.first();

			if (results != null) {
				id = results.getInt(2);
				description = results.getString("PLACE_DESCRIPTION");
				rate = results.getInt("PLACE_RATE");
				numOfCheckins = results.getInt("place_check_in");
				numOfSaves = results.getInt("place_number_of_saves");
				longitude = results.getDouble("longitude");
				latitude = results.getDouble("latitude");
				count++;
				return this;
			}
			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Statement creation failed ... ");
		}
		return null;
	}

	/**
	 * @owner mohamed kamal add place to the list of favourites of a certain
	 *        user
	 * @param UserModel
	 *            user
	 */

	public boolean savePlace(UserModel u) {

		int user_id = u.getId();
		String getPlaceId = "select PLACE_ID from place where place.PLACE_NAME = '"
				+ this.name + "'";
		try {
			Statement stm = (Statement) conn.createStatement();
			ResultSet res = stm.executeQuery(getPlaceId);
			if (res != null) {
				res.first();
				int PlaceId = res.getInt("PLACE_ID");
				History h = new History();
				h.savePlaceHistory(PlaceId, user_id);

				String add_fav_place = "Insert into faviourite_places values ('"
						+ user_id + "','" + PlaceId + "')";
				stm = (Statement) conn.createStatement();
				stm.executeUpdate(add_fav_place);

				String updateCounter = "Update place set place.PLACE_NUMBER_OF_SAVES  = place.PLACE_NUMBER_OF_SAVES+1 where PLACE_ID = '"
						+ PlaceId + "'";
				stm = (Statement) conn.createStatement();
				stm.executeUpdate(updateCounter);
				return true;
			} else {
				System.out.println("Null Found !!!");
				return false;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;

	}

	void searchPlace(int placeid) {
		Statement stm = null;
		ResultSet results = null;
		String select = "select * from place where PLACE_ID = '" + placeid
				+ "' ";

		try {


			stm = (Statement) conn.createStatement();
			results = stm.executeQuery(select);

			if (results.next()) {
				id = placeid ;
				description = results.getString("PLACE_DESCRIPTION");
				rate = results.getInt("PLACE_RATE");
				numOfCheckins = results.getInt("place_check_in");
				numOfSaves = results.getInt("place_number_of_saves");
				longitude = results.getDouble("longitude");
				latitude = results.getDouble("latitude");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Statement creation failed ... ");
		}
	}

}
