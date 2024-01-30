package com.walit.Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Storage serves as a helper class to CLI and UI by handling the storage and calling from specific files
 * used by the program.
 *
 * @author Jackson Swindell
 */
public class Storage {
	private final Logger logger;
	private Connection connection = null;

	/**
	 * Sets the logger for program the duration of the program's runtime and set up the database connection.
	 */
	public Storage(Logger logger) throws ClassNotFoundException {
		final String dbPath = "resources\\utilities\\data\\userData.db";
		this.logger = logger;
		Class.forName("org.sqlite.JDBC");
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
			connection.setAutoCommit(true);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate("create table if not exists userDetails (user varchar(255), pass varchar(255));");
		}
		catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to parse database credentials.");
			System.exit(1);
		}
	}

	/**
	 * Closes the database connection.
	 */
	public void closeConnections() {
		try {
			if (connection != null) {
				connection.close();
			}
		}
		catch (SQLException e) {
			logger.log(Level.WARNING, "Unable to close database connection.");
		}
	}
	/**
	 * Stores the name and password for the user.
	 * @param info User information to be stored.
	 */
	public void storeData(String[] info) {
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("insert into userDetails (user, pass) select ?, ? where not exists (select 1 from userDetails where lower(user) = ? and pass = ?)");
			statement.setString(1, info[0]);
			statement.setString(2, info[1]);
			statement.setString(3, info[0]);
			statement.setString(4, info[1]);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			logger.log(Level.WARNING, "Could not write elements to database.");
		}
	}
	/**
	 * Displays all passwords and associated names for the user.
	 */
	public void displayUserPassCombos() {
		System.out.println();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from userDetails order by lower(user)");
			for (int i = 1; rs.next(); i++) {
				System.out.println("[" + i + "] Username = " + rs.getString("user") + ", Password = " + rs.getString("pass"));
			}
		}
		catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to read the database.");
		}
	}
	/**
	 * Queries all usernames and passwords from the database.
	 * @return Returns a string array containing all username and password combinations.
	 */
	public String[] getUserPassCombosForUI() {
		List<String> comboList = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from userDetails order by lower(user)");
			while (rs.next()) {
				comboList.add(rs.getString("user") + "~~SEPARATOR~~" + rs.getString("pass"));
			}
		}
		catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to read the database.");
		}
		String[] combos = new String[comboList.size()];
		for (int i = 0; i < combos.length; i++) {
			combos[i] = comboList.get(i);
		}
		return combos;
	}
	/**
	 * Searches through the database for a specific name.
	 * @param name The specific name to find in the database.
	 * @return Returns a list of all lines that have a name matching the given one.
	 */
	public ArrayList<String> checkStoredDataForName(String name) {
		ArrayList<String> acceptedStrings = new ArrayList<>();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement("select * from userDetails where lower(user) = ? order by lower(user)");
			statement.setString(1, name);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				if (rs.getString("user").equalsIgnoreCase(name)) {
					acceptedStrings.add(rs.getString("user") + "~~SEPARATOR~~" + rs.getString("pass"));
				}
			}
		}
		catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to read the database.");
		}
		return acceptedStrings;
	}
	/**
	 * Searches the database for all contents.
	 * @return Returns the contents of the database for the Manager to search through for a specific name.
	 */
	public List<String> findNameToAlter() {
		List<String> comboList = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from userDetails order by lower(user)");
			while (rs.next()) {
				comboList.add(rs.getString("user") + "~~SEPARATOR~~" + rs.getString("pass"));
			}
		}
		catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to read the database.");
		}
		return comboList;

	}

	/**
	 * Removes a row from the database.
	 * @param user The username to remove from the database.
	 * @param pass The password to remove from the database.
	 */
	public void removeRow(String user, String pass) {
		try {
			PreparedStatement statement = connection.prepareStatement("delete from userDetails where lower(user) = ? and pass = ?");
			statement.setString(1, user);
			statement.setString(2, pass);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to remove element from the database.");
		}
	}

	/**
	 * Changes a row within the database.
	 * @param oldUser The old username to query form the database.
	 * @param oldPass The old password to query form the database.
	 * @param newUser The new username to update the database to.
	 * @param newPass The new password to update the database to.
	 */
	public void changeRow(String oldUser, String oldPass, String newUser, String newPass) {
		try {
			PreparedStatement statement = connection.prepareStatement("update userDetails set user = ?, pass = ? where user = ? and pass = ?");
			statement.setString(1, newUser);
			statement.setString(2, newPass);
			statement.setString(3, oldUser);
			statement.setString(4, oldPass);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to change element from the database.");
		}
	}
}