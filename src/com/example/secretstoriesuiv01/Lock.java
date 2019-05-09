package com.example.secretstoriesuiv01;

import java.io.Serializable;

public class Lock implements Serializable{
	private String password;
	private Boolean valid = false;
	
	public Lock(String password) {
		this.password = password;
	}
	
	public String getpassword() {
		return this.password;
	}
	
	public void setValid(Boolean bol) {
		this.valid = bol;
	}
	
	public Boolean getValid() {
		return this.valid;
	}
}
