
package com.example.secretstoriesuiv01;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Key;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * 
 * @author Ali Menhem, Sandra Smrekar, Jerry Rosengren, Klara Rosengren, Fredrik Johnson
 * 
 * This class should handle every client that makes a connection to the server
 * This server has a thread that listens to connecting clients
 */

public class Server {
	private ServerSocket serverSocket;
	private ArrayList<ClientHandler> handlers = new ArrayList<ClientHandler>();
	private ConnectDB database = new ConnectDB();             

	public Server(int port) {
		try {
			serverSocket = new ServerSocket(port);
			new Connection().start();
		}catch(IOException e) {
			e.printStackTrace();		
		}
	}

	/**
	 * Listens to clients that connect to the server.
	 */
	private class Connection extends Thread{
		public void run() {
			while(!serverSocket.isClosed()) {
				try{
					ClientHandler handler = new ClientHandler(serverSocket.accept());
					handler.start();
				}catch(IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		}
	}
	/**
	 * Checks if a username exists in database
	 */
	public boolean verifyUser(String username) {
		return database.verifyUser(username);
	}

	/**
	 * Sends message information to the database which stores them
	 */
	public void sendMessage(Message message) {
		String user = message.getSender();
		String textMessage = user + ":" + message.getText();
		ArrayList<String> recipients = message.getRecipients();
		int convoID = database.getChatIDFromConversation(user, recipients);
		database.insertConversation(convoID, textMessage );  
	}
	/**
	 * Disconnects client
	 */
	public void disconnect(Socket socket) {
		try {
			socket.close();
		}catch(IOException e) {e.printStackTrace();}
	}
	/**
	 * Decrypts an encrypted message
	 */
	public String decryptMessage(Message message) {
		try {
			byte[] encrypted = message.getEncryptedMessage();
			String key = String.valueOf(message.getKey());
			for (int i = key.length(); i < 16; i++) {
				key += "A";
			}
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(encrypted));
            message.setText(decrypted);
            return decrypted;
		}
		catch ( Exception e){e.printStackTrace();}

		return message.getText();
	}
	
	/**
	 *	ClientHandler handles communication between client and server
	 */
	private class ClientHandler extends Thread{
		private ObjectInputStream input;
		private ObjectOutputStream output;
		private Socket socket;
		private User user;
		public ClientHandler(Socket socket) throws ClassNotFoundException {
			try {
				this.socket = socket;
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());
			}catch(IOException e) {
				e.printStackTrace();
			}

		}

		public void run() {
			Object object;
			while(!socket.isClosed()) {
				try {

					object = (Object) input.readObject();
					/**
					 * String[] contains login information (username, password)
					 * Used when a user logs in
 					 */
					if(object instanceof String[]) {
						String[] stringUser = (String[]) object;
						boolean exist = database.verifyLogin(stringUser[0], stringUser[1]);
						if(exist == true) {
							user = new User(stringUser[0], stringUser[1], "");
							user.setChatPassword(database.getChatPassword(stringUser[0]));
							ArrayList<String> chatMembers = database.getConversationNames(stringUser[0]);
							output.writeObject(chatMembers);
							output.flush();
							boolean handlerExist = false;
							for( int i = 0; i < handlers.size(); i++) {
								if(handlers.get(i).user.getUsername().equals(user.getUsername())) {
									handlerExist = true;
								}
							}
							if(!handlerExist) {
								handlers.add(this);
							}
						}
						else {
							output.writeObject("Failed");
							output.flush();
						}
					}
					/**
					 * User class, used when a new account is made
 					 */
					else if(object instanceof User) {
						user = (User) object;
						Boolean exist = verifyUser(user.getUsername());
						if(exist == true) {
							output.writeObject(true);
							output.flush();

						}
						else {
							System.out.println("User doesnt exist");
							database.createUser(user.getUsername(), user.getPassword(), user.getChat_password());
							output.writeObject(false);
							output.flush();
							handlers.add(this);

						}}
					/**
					 * Message class for sending messages
 					 */
					else if(object instanceof Message) {
						Message message = (Message) object;
						message.setText(decryptMessage(message));
						sendMessage(message);
						ArrayList<String> rec = message.getRecipients();
						System.out.println(rec.toString());
						message.setText(new String(message.getEncryptedMessage()));
						for(String reci : rec) {
							for(ClientHandler handler:handlers) {
								String user = handler.user.getUsername();
								if(reci.equals(user)) {   
									handler.output.writeObject(message);
									handler.output.flush();
								}

							}
						}
						output.writeObject(message);
						output.flush();
					}
					/**
					 * Conversation class, to load all existing conversations to a users list
 					 */
					else if(object instanceof Conversations) {
						Conversations convo = (Conversations) object;
						int id = database.getChatIDFromConversation(convo.getChatMembers());
						ArrayList<String> chat = database.getChat(id);
						convo.setConversation(chat);
						output.writeObject(convo);
						output.flush();
					}
					else if(object instanceof String) {
						String req = (String) object;
						/**
						 * Sends all existing users in the database
						 */
						if(req.equals("getUsers")) {
							ArrayList<String> users = database.getUsers();
							String[] usersArray = new String[users.size()];
							int pos = 0;
							for(String user : users) {
								usersArray[pos] = user;
								pos++;
							}
							output.writeObject(usersArray);
							output.flush();
							System.out.println("skickar users" + users.toString());
						}
						/**
						 * Logs out the user and disconnects
	 					 */
						else if(req.equals("Logout")){
							for(ClientHandler handler : handlers) {
								System.out.println(handler.user.getUsername() + " 1");
							}
							handlers.remove(this);
							output.writeObject("Logout");
							output.flush();
							disconnect(socket);
						}

					}
					/**
					 * NewChatInfo class, used when a new conversation is made
					 */
					else if(object instanceof NewChatInfo) {
						NewChatInfo chatInfo = (NewChatInfo) object;		
						System.out.println("Chattar = " + chatInfo.getMembers());
						database.createNewChat(database.getNumberOfConversations() + 1, chatInfo.getMembers());
						for(int j = 0; j < chatInfo.getMembers().size();j++) {
							for(int i = 0; i < handlers.size(); i++) {
								if(handlers.get(i).user.getUsername().equals(chatInfo.getMembers().get(j))){
									handlers.get(i).output.writeObject(database.getConversationNames(handlers.get(i).user.getUsername()));
								}
							}
						}

					}
					/**
					 * Lock class, used when a user unlocks a chat
 					 */
					else if(object instanceof Lock) {
						Lock lock = (Lock) object;
						Boolean resp = database.verifyChattPassword(this.user.getUsername(),this.user.getChat_password());
						lock.setValid(resp);
						output.writeObject(lock);
						output.flush();
					}
				}catch(IOException e) {
					disconnect(socket);
				}
				catch(ClassNotFoundException ex) {
					ex.printStackTrace();
				}
			}		
		}


	}
	public static void main(String[] args) throws UnknownHostException {
		Server server = new Server(6445); 
	}


}
