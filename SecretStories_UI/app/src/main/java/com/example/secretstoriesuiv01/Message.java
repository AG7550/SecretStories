package com.example.secretstoriesuiv01;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Ali Menhem, Jerry Rosengren
 * Class which holds information about a message and handles the encryption of a message
 *
 */
public class Message implements Serializable {
	private int key; //Key to encrypt/decrypt
	private String text;
	private String sender;
	private byte[] encryptedMessage;
	private ArrayList<String> recipients;


	public Message(String text, String sender, ArrayList<String> recipients, int key) {
		this.text = text;
		this.sender = sender;
		this.recipients = recipients;
		this.key = key;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public ArrayList<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(ArrayList<String> recipients) {
		this.recipients = recipients;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public void setEncryptedMessage(byte[] message){
		this.encryptedMessage = message;
	}
	public byte[] getEncryptedMessage(){
		return encryptedMessage;
	}

}
