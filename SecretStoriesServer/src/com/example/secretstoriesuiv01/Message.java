package com.example.secretstoriesuiv01;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;

public class Message implements Serializable {
	private int messageID;
	private String text;
	private String sender;
	private ArrayList<String> recipients;
	private Date dateSent;

	//If the user sends a message-object without a picture
	
	public Message(String text, String sender, ArrayList<String> recipients) {
		this.text = text;
		this.sender = sender;
		this.recipients = recipients;
	}
	
	//If the user sends a message-object with a picture
	//public Message2(int id, String text, String sender, Object[] recipients) {
	//	this(id, text, sender, recipients);
	//}
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

//	public ImageIcon getImage() {
//		return image;
//	}

//	public void setImage(ImageIcon image) {
//		this.image = image;
//	}
	public void setDateSent(Date date) {
		this.dateSent = dateSent;
	}
	public Date getDateSent(Date date) {
		return dateSent;
	}
	public int getMessageID() {
		return messageID;
	}
	

}
