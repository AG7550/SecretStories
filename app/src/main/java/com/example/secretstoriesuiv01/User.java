package com.example.secretstoriesuiv01;

import java.io.Serializable;
import java.security.MessageDigest;

public class User implements Serializable {
	private String username;
	private String password;
	private String[] contacts;
	private static final long serialVersionUID = 4568216;
	
	public User(String username, String password) {
		this.username = username;
		this.password = getSHA256Hash(password);
	}
	
	public String getUsername() {
		return this.username;
	}
	public String getPassword() {
		return this.password;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = getSHA256Hash(password);
	}
	public String[] getContacts() {
		return contacts;
	}

	private String getSHA256Hash(String password) {
		String result = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes("UTF-8"));
			return bytesToHex(hash);
		}catch(Exception e) {e.printStackTrace();}
		return null;
	}
	private String  bytesToHex(byte[] hash) {
		StringBuilder sb = new StringBuilder();
		for (byte b : hash) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
	


}
