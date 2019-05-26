package com.example.secretstoriesuiv01;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client  {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private int port;
	private String ip;
	private User user;
	
	public Client(int port, String ip) {
		this.port = port;
		this.ip = ip;
	}
	
	public void connect() {
		try {
			this.socket = new Socket(ip,port);
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}
	}
	public void createUser(User user) {
		try {
			output.writeObject(user);
		} 
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	

	private class Listener extends Thread{
		
		public void run() {
		Object response;
		try {
			while(true) {
				response = input.readObject();
				if(response instanceof Message) {
					Message message = (Message) response;
				}
					
			}
		} catch(IOException | ClassNotFoundException e) {e.printStackTrace();}
	}
	}
}

