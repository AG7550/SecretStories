package com.example.secretstoriesuiv01;

import java.io.Serializable;
import java.util.Date;

import javax.swing.ImageIcon;

public class Message implements Serializable {
	private int messageID;
	private String text;
	private String sender;
	private Object[] recipients;
	private ImageIcon image;
	private Date dateSent;

	//If the user sends a message-object without a picture
	
	public Message(int id, String text, String sender, Object[] recipients) {
		this.messageID = id;
		this.text = text;
		this.sender = sender;
		this.recipients = recipients;
	}
	
	//If the user sends a message-object with a picture
	
	public Message(int id, String text, String sender, Object[] recipients, ImageIcon image) {
		this(id, text, sender, recipients);
		this.image = image;
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

	public Object[] getRecipients() {
		return recipients;
	}

	public void setRecipients(Object[] recipients) {
		this.recipients = recipients;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}
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
