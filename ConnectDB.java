package com.example.secretstoriesuiv01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;


public class ConnectDB {
	
	
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con;
				con = DriverManager.getConnection("jdbc:mysql://ddwap.mah.se/ai1585", "ai1585", "Aliasse251");
				return con;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void executeSQLQuery(String query) {
		Connection con = getConnection();
		Statement st;
		try {
			st = con.createStatement();
			if (st.executeUpdate(query) == 1) {
				System.out.println("Success");
			}
			else {
				System.out.println("Failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void insertConversation(ArrayList<String> members, String convo) {
		String users = "";
		for(String member : members) {
			users += member +":";
		}
		String query = "INSERT INTO `conversations`(`chatID`, `chatMembers`, `conversation`) VALUES (NULL,'"+users+"','"+convo+"')";
		executeSQLQuery(query);
	}
	public void insertContact(String username, String contact) { 
		String contacts = "";
		Connection connection = getConnection();
		String query = "SELECT * FROM users";
		Statement st;
		ResultSet rs;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				String name = rs.getString("username");
				if (name.equals(username)) {
					contacts = (rs.getString("contacts"));
					contacts += contact;
				}
				
			}
			query = "UPDATE `users` SET `contacts` = '"+contacts+":"+"' WHERE `users`.`username` = '"+username+"'";
			executeSQLQuery(query);
		} 
		catch (Exception e) {
		 e.printStackTrace();
		}
	}
	public ArrayList<String> getContacts(String username){
		ArrayList<String> contactList = new ArrayList<String>();
		String contacts = "";
		Connection connection = getConnection();
		String query = "SELECT * FROM users";
		Statement st;
		ResultSet rs;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				if(rs.getString("username").equals(username)) {
					contacts = rs.getString("contacts");
				}
			}
			String[] array = contacts.split(":");
			for(String contact : array) {
				contactList.add(contact);
			}
			return contactList;
		} 
		catch (Exception e) {

		 e.printStackTrace();
		 return null;
		}
	}
	//Returnerar alla anv�ndare i en lista fr�n databasen, format anv�ndare:l�senord(SHA-256)
	public static ArrayList<String> getUsers() {
		ArrayList<String> list = new ArrayList<String>();
		Connection connection = getConnection();
		String query = "SELECT * FROM users";
		Statement st;
		ResultSet rs;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
					list.add(rs.getString("username")); 
			}
			return list;
		} 
		catch (Exception e) {

		 e.printStackTrace();
		 return null;
		}
		
	}
	public void insertChat(int id, String message) {
		if (verifyChatID(id)) {
			insertNewChat(id, message);
		}
		else {
		String query = "UPDATE `conversations` SET `chat` = '"+message+"' WHERE `conversations`.`chatID` = '"+id+"'";
		executeSQLQuery(query);
		}
	}
	public void insertNewChat(int id, String message) {
		String query = "INSERT INTO `conversations`(`chatID`, `chat`) VALUES ('"+id+"','"+message+"')";
		executeSQLQuery(query);
	}
	public boolean verifyChatID(int id) {
		Connection con = getConnection();
		String query = "SELECT * FROM conversations";
		Statement st;
		ResultSet rs;
		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				if (rs.getInt("chatID") == id) {
					return true;
				}
			}
			return false;
		} 
		catch (Exception e) {

		 e.printStackTrace();
		 return false;
		}
	}
	public String getChat(int id) {
		String messages = "";
		Connection con = getConnection();
		String query = "SELECT * FROM conversations";
		Statement st;
		ResultSet rs;
		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				if (rs.getInt("chatID") == id) {
					messages = rs.getString("chat");
				}
			}
			return messages;
		} 
		catch (Exception e) {

		 e.printStackTrace();
		 return null;
		}
	}
	//Returnerar true om anv�ndare finns med i databasen, annars false
	public static boolean verifyUser(String username) {
		ArrayList<String> users = getUsers();
		for(String user : users) {
			if (user.equals(username)) {
				return true;
			}
		}
		return false;
	}
	public boolean verifyLogin(String username, String password) {
		if(verifyUser(username)) {
			String pass = "-1";
			Connection connection = getConnection();
			String query = "SELECT * FROM users";
			Statement st;
			ResultSet rs;
			try {
				st = connection.createStatement();
				rs = st.executeQuery(query);
				while (rs.next()) {
						pass = rs.getString("password"); 
				}
			} 
			catch (Exception e) {

				 e.printStackTrace();
			}
			if (pass.equals(password)) {
				return true;
			}
			else {return false;
			}		
	}
		else {
			return false;
		}
}
	//L�gger till anv�ndaren i databas
	public static void createUser(String username, String password) {
		String query = "INSERT INTO `users`(`userid`, `username`, `password`, `contacts`) VALUES (NULL,'"+username+"','"+password+"', '"+""+"')";
		executeSQLQuery(query);
	}
	//Tar bort anv�ndaren fr�n databas
	public static void deleteUser(String username) {
		String query = "DELETE FROM `users` WHERE `users`.`username` = '"+username+"'";
		executeSQLQuery(query);
	}
	
	public static void main(String[] args) {
		ConnectDB db = new ConnectDB();
		
	}
}

