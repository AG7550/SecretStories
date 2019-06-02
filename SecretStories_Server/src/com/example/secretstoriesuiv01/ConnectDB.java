package com.example.secretstoriesuiv01;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;


public class ConnectDB {
	static Connection connection;
	static Statement st;
	public ConnectDB() {
		connection = getConnection();
		try {
			st = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
				connection  = DriverManager.getConnection("jdbc:mysql://ddwap.mah.se/ai1585", "ai1585", "Aliasse251");
				return connection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void executeSQLQuery(String query) {
		
		try {
			st = connection.createStatement();
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
	public String decryptMessage(String message, int id) {
		try {
			byte[] encrypted = message.getBytes();
			String key = String.valueOf(id);
			for (int i = key.length(); i < 16; i++) {
				key += "A";
			}
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			// encrypt the text
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(encrypted));
            return decrypted;
		}
		catch ( Exception e){e.printStackTrace();}

		return message;
	}
	public void insertContact(String username, String contact) { 
		String contacts = "";
		String query = "SELECT * FROM users";
		
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
	public String getChatPassword(String username){
		ArrayList<String> contactList = new ArrayList<String>();
		String chatPassword = "";
		String query = "SELECT * FROM users";
		
		ResultSet rs;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				if(rs.getString("username").equals(username)) {
					chatPassword = rs.getString("contacts");
				}
			}
			return chatPassword;
		} 
		catch (Exception e) {

		 e.printStackTrace();
		 return null;
		}
	}
	//Returnerar alla anv�ndare i en lista fr�n databasen, format anv�ndare:l�senord(SHA-256)
	public static ArrayList<String> getUsers() {
		ArrayList<String> list = new ArrayList<String>();
		String query = "SELECT * FROM users";
		
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
	
	public void insertConversation(int id, String message) {
		String newMessages ="";
		ArrayList<String> oldMessages = getChat(id);
		oldMessages.add(message);
		for(String m : oldMessages) {
			newMessages += m +"§";
		}
		String query = "UPDATE `conversations` SET `conversation` = '"+newMessages+"' WHERE `conversations`.`chatID` = '"+id+"'";
		executeSQLQuery(query);
		}
	
	public int getChatIDFromConversation(ArrayList<String> recipients) {
		Collections.sort(recipients);
		String query = "SELECT * FROM conversations";
		
		ResultSet rs;
		int id = -1;
		String members = "";
		String[] tempMembers;
		ArrayList<String> arrayMembers = new ArrayList<String>();
		try {
			st = connection.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				members = rs.getString("chatMembers");
				tempMembers = members.split(":");
				for(String name : tempMembers) {
					arrayMembers.add(name);
				}
				Collections.sort(arrayMembers);
				if (recipients.equals(arrayMembers)) {
					id = rs.getInt("chatID");
					break;
				}
				else {
					arrayMembers.clear();
				}
			}
		}
		catch (Exception e) {
		 e.printStackTrace();
		}
		return id;
	}
	public int getChatIDFromConversation(String username, ArrayList<String> recipients) {
		recipients.add(username);
		Collections.sort(recipients);
		String query = "SELECT * FROM conversations";
		
		ResultSet rs;
		int id = -1;
		String members = "";
		String[] tempMembers;
		ArrayList<String> arrayMembers = new ArrayList<String>();
		try {
			st = connection.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				members = rs.getString("chatMembers");
				tempMembers = members.split(":");
				for(String name : tempMembers) {
					arrayMembers.add(name);
				}
				Collections.sort(arrayMembers);
				if (recipients.equals(arrayMembers)) {
					id = rs.getInt("chatID");
					break;
				}
				else {
					arrayMembers.clear();
				}
			}
			
		}
		catch (Exception e) {
		 e.printStackTrace();
		}
		recipients.remove(username);

		return id;
	}
	public void createNewChat(int id, ArrayList<String> memberList) {
		String members = "";
		for(String user: memberList) {
			members += user +":";
		}
		members = members.substring(0, members.length()-1);
		String query = "INSERT INTO `conversations`(`chatID`,`chatMembers`, `conversation`) VALUES ('"+id+"','"+members+"','"+""+"')";
		executeSQLQuery(query);
	}
	public boolean verifyChatID(int id) {
		String query = "SELECT * FROM conversations";
		
		ResultSet rs;
		try {
			st = connection.createStatement();
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
	public ArrayList<String> getChat(int id) {
		ArrayList<String> chat = new ArrayList<>();
		String messages = "";
		String query = "SELECT * FROM conversations";
		ResultSet rs;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				if (rs.getInt("chatID") == id) {
					messages = rs.getString("conversation");
				}
			}
			String[] temp = messages.split("§");
			for(String message: temp) {
				chat.add(message);
			}
			return chat;
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
			String query = "SELECT * FROM users";
			
			ResultSet rs;
			try {
				st = connection.createStatement();
				rs = st.executeQuery(query);
				while (rs.next()) {
						if(pass.equals(password)) {
							break;
						}
						else {
							pass = rs.getString("password");
						}
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
	public static void createUser(String username, String password, String chatPassword) {
		String query = "INSERT INTO `users`(`userid`, `username`, `password`, `contacts`) VALUES (NULL,'"+username+"','"+password+"', '"+chatPassword+"')";
		executeSQLQuery(query);
	}
	//Tar bort anv�ndaren fr�n databas
	public static void deleteUser(String username) {
		String query = "DELETE FROM `users` WHERE `users`.`username` = '"+username+"'";
		executeSQLQuery(query);
	}
	public ArrayList<String> getConversationNames(String username) {
		ArrayList<String> memberList = new ArrayList<String>();
		String[] tempConversations;
		String members;
		String query = "SELECT * FROM conversations";
		
		ResultSet rs;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				members = rs.getString("chatMembers");
				tempConversations = members.split(":");
				for(String name : tempConversations) {
					if (name.equals(username)) {
						members = members.replace(username, "");
						memberList.add(members);
					}
				}
			}
		} 
		catch (Exception e) {

			 e.printStackTrace();
		}
		return memberList;
		
		
		
	}
	public int getNumberOfConversations() {
		int amount = 0;
		String query = "SElECT * FROM conversations";
		ResultSet rs;
		try {
			
			st = connection.createStatement();
			rs = st.executeQuery(query);
			while(rs.next()) {
				amount++;
			}
		}
		catch(Exception e){e.printStackTrace();}
		
		return amount;
	}
	
	public boolean verifyChattPassword(String username, String chatPassword) {
			String pass = "-1";
			String query = "SELECT * FROM users WHERE `username` = '"+username+"'";
			
			ResultSet rs;
			try {
				st = connection.createStatement();
				rs = st.executeQuery(query);
				while (rs.next()) {
						if(pass.equals(chatPassword)) {
							break;
						}
						else {
							pass = rs.getString("contacts");
						}
				}
			} 
			catch (Exception e) {

				 e.printStackTrace();
			}
			if (pass.equals(chatPassword)) {
				return true;
			}
			else {return false;
			}		
	}
	public static String getAESKey(int id) {
		String key = String.valueOf(id);
		for(int i = key.length(); i<16; i++) {
			key += "A";
		}
		return key;
	}
	public static void main(String[] args) {
		ConnectDB db = new ConnectDB();
		
		}
	
}

