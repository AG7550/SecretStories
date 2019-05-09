package com.example.secretstoriesuiv01;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
			while(true) {
				try(Socket socket = serverSocket.accept()){
					ClientHandler handler = new ClientHandler(socket);		// Spara Clienthandlers i en samling för att kunna skicka till allas strömmar sen.
					handlers.add(handler);
					handler.start();
				}catch(IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public boolean verifyUser(String[] user) {
		/*
		 * När man loggar in så ska användarnamn och lösenord verifieras genom att kolla i databasen.
		 * Om användare finns så ska true returneras 
		 */
		return false;
	}
	
	public void login(/*User, ClientHandler*/) {
		// hämta all data för den användaren och skicka till klienten. 
	}
	
	
	public void sendMessage(Message message) {
		User[] recipients = (User[]) message.getRecipients();
		String messages = database.getChat(message.getMessageID());
		messages += message.getText();
		database.insertChat(message.getMessageID(), messages);
		for(ClientHandler handler : handlers) {
			for(User user: recipients) {
				if (handler.user.getUsername() == user.getUsername()) {
					try {
						handler.oos.writeObject(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		/*
		 * Skickar ett meddelande till valda recievers.
		 * Om någon inte är online så ska en notifiering skickas till den.  
		 */
	}
	
	public void saveContacts(/*Message av typen Contacts*/) {
		/*
		 * Sparar de valda kontakterna i databasen för just den klienten.
		 */
	}
	
	public void createAccount(/*User*/) {
		/*
		 * Skapar ett konto för den klienten. Sparar i databasen. 
		 */
	}
	
	public void searchUser(/*User eller username*/) {
		/*
		 * En användare ska kunna leta efter en användare som existerar men kanske inte är inloggad. 
		 */
	}
	
	public void logout(/*User*/) {
		/*
		 * Denna metod ska anropas när en användare loggar ut.
		 * Sen ska också metoden updadeOnline anropas för att då ta bort användaren som precis loggade ut. 
		 */
	}
	public void disconnect(Socket socket) {
		try {
			socket.close();
		}catch(IOException e) {e.printStackTrace();}
	}
	
	
	
	
	
	
	
	private class ClientHandler extends Thread{
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		private Socket socket;
		private User user;
		public ClientHandler(Socket socket) throws ClassNotFoundException {
			try {
				this.socket = socket;
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
			}catch(IOException e) {
				e.printStackTrace();
			}
			
		}
		
		public void run() {
			Object object;
			
			while(true) {
				try {
					object = (Object) ois.readObject();
					
					if(object instanceof String[]) {	//Kollar användarnamn och lösenord för inloggning.
						String[] user = (String[]) object;
						Boolean exist = database.verifyUser(user[0]);
						
						if(exist == true) {
							//Logga in
//							login();
//							loadConvos(); 
//							loadSavedContacts();
							
							
						}
						else {
							// skriva tillbaka felmeddelande. 
						}
					}
					
					else if(object instanceof User) {
						User user = (User) object;
						Boolean exist = database.verifyUser(user.getUsername());
						if(exist == true) {
							// skicka felmeddelande att man inte kan skapa en som redan finns
						}
						else {
							database.createUser(user.getUsername(), user.getPassword());
						}
						
					}
					
					else if(object instanceof Message) {
						Message message = (Message) object;
						sendMessage(message);
						// Om någon reciver inte är online så ska en notifiering skickas. 
					}
					
				}catch(IOException e) {
					e.printStackTrace();
					disconnect(socket);
				}
				catch(ClassNotFoundException ex) {
					ex.printStackTrace();
				}
			}		
		}
		
		
	}
	
	
	 

}