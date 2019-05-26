package com.example.secretstoriesuiv01;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


/**
 * @version 1.0
 * @author sandrasmrekar
 * 
 * Klassen ska hantera varje klient som kopplar upp sig, genom att använda en inre klass som klienthanterare. 
 * Servern har en tråden som lyssnar på klienter som kopplar upp sig. 
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
	 * Lyssnar på klienter som kopplas upp.
	 * @author sandrasmrekar
	 */
	private class Connection extends Thread{
		public void run() {
			while(!serverSocket.isClosed()) {
				try{
					ClientHandler handler = new ClientHandler(serverSocket.accept());		// Spara Clienthandlers i en samling för att kunna skicka till allas strömmar sen.
					handler.start();
				}catch(IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public boolean verifyUser(String username) {
		return database.verifyUser(username);
	}


	public void sendMessage(Message message) {
		String user = message.getSender();
		String textMessage = user + ":" + message.getText();
		ArrayList<String> recipients = message.getRecipients();
		int convoID = database.getChatIDFromConversation(user, recipients);
		database.insertConversation(convoID, textMessage );  
		/*
		 * Skickar ett meddelande till valda recievers.
		 * Om någon inte är online så ska en notifiering skickas till den.  
		 */
	}

	public void disconnect(Socket socket) {
		try {
			socket.close();
		}catch(IOException e) {e.printStackTrace();}
	}

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

					if(object instanceof String[]) {	//Kollar användarnamn och lösenord för inloggning.
						String[] stringUser = (String[]) object;
						boolean exist = database.verifyLogin(stringUser[0], stringUser[1]);
						if(exist == true) {
							user = new User(stringUser[0], stringUser[1],"");
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

					else if(object instanceof Message) {
						Message message = (Message) object;
						sendMessage(message);
						ArrayList<String> rec = message.getRecipients();
						System.out.println(rec.toString());
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
						// Om någon reciver inte är online så ska en notifiering skickas. 
					}
					else if(object instanceof Conversations) {
						Conversations convo = (Conversations) object;
						int id = database.getChatIDFromConversation(convo.getChatMembers());
						ArrayList<String> chat = database.getChat(id);
						convo.setConversation(chat);
						output.writeObject(convo);
						output.flush();
					}
					// TODO ta bort sig själv från userArray
					else if(object instanceof String) {
						String req = (String) object;
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
					else if(object instanceof NewChatInfo) {
						NewChatInfo chatInfo = (NewChatInfo) object;		
						System.out.println("Chattar = " + chatInfo.getMembers());
						database.createNewChat(database.getNumberOfConversations() + 1, chatInfo.getMembers());
						//skriva tillbaka något som uppdaterar chattar. arrayList
						//						output.writeObject(database.getConversationNames(this.user.getUsername()));		// skriva till den andra user också, alla som finns i gen sista platsen
						for(int j = 0; j < chatInfo.getMembers().size();j++) {
							for(int i = 0; i < handlers.size(); i++) {
								if(handlers.get(i).user.getUsername().equals(chatInfo.getMembers().get(j))){
									handlers.get(i).output.writeObject(database.getConversationNames(handlers.get(i).user.getUsername()));
								}
							}
						}

					}
					else if(object instanceof Lock) {
						Lock lock = (Lock) object;
						Boolean resp = database.verifyChattPassword(this.user.getUsername(),lock.getpassword());
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
		Server server = new Server(6666); 
		//		InetAddress a = InetAddress.getLocalHost();
		//		System.out.println(a.getHostAddress());
	}


}
