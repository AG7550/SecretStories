package com.example.secretstoriesuiv01;

import java.io.Serializable;
import java.security.MessageDigest;
/**
 *
 * @author Fredrik Johnson, Ali Menhem
 * User class, contains information about a user
 */
public class User implements Serializable {
	private String username;
	private String password;
	private String chat_password;
	private String[] contacts;
	private static final long serialVersionUID = 4568216;

	public User(String username, String password, String chat_password) {
		this.username = username;
		this.password = getSHA256Hash(password);
		this.chat_password = getSHA256Hash(chat_password);
	}

	public String getUsername() {
		return this.username;
	}
	public String getPassword() {
		return this.password;
	}
	public String getChat_password(){return this.chat_password;}

	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = getSHA256Hash(password);
	}
	public String[] getContacts() {
		return contacts;
	}
	/**
	 * Encrypts a string value (SHA256)
	 */
	private String getSHA256Hash(String password) {
		String result = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes("UTF-8"));
			return bytesToHex(hash);
		}catch(Exception e) {e.printStackTrace();}
		return null;
	}
	public void setChatPassword(String password) {
		chat_password = password;
	}
	/**
	 * This method converts a byte array to hex value
	 */
	private String  bytesToHex(byte[] hash) {
		StringBuilder sb = new StringBuilder();
		for (byte b : hash) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
}
