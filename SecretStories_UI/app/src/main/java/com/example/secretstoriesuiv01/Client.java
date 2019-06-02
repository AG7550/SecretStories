package com.example.secretstoriesuiv01;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * author Ali Menhem, Sandra Smrekar, Jerry Rosengren, Klara Rosengren, Fredrik Johnson
 * Client class, handles connection between server
 */
public class Client extends AppCompatActivity {
	private Socket socketClient;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private int port;
	private String ip;
	private User user;
	private Context contextClient;
	private Context activityContext;

	
	public Client() {
		this.port = port;
		this.ip = ip;

	}

	/**
	 * Connects a user to server and saves the users activity context (to interact with UI)
	 */
	public void connect(Context context) {
		activityContext = context;
		try{
			new ConnectTask().execute();
		} catch (Exception e){
			e.printStackTrace();
		}


	}

	/**
	 * Saves user information
	 */
	public void setUser(User user){
		this.user = user;
	}

	/**
	 * Sends user info to server, server creates user
	 */
	public void createUser(User user) {
		this.user = user;
		try{
			new CreateUserTask(user).execute();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Sends message info to server, server stores message
	 */
	public void sendMessage(Message message){
		try{
			new SendMessageTask(message).execute();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Sends user info to server, server validates user
	 */
	public void login(User user){
		this.user = user;
		try{
			new LoginUserTask(user).execute();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Sends conversation info to server, server creates new chat
	 */
	public void getChat(Conversations convo){
		try{
			new SendConversationTask(convo).execute();
		}
		catch(Exception e){e.printStackTrace();}
	}
	/**
	 * Sends a request to server, server sends a list with all existing users back
	 */
	public void getAllUsers(Context context){
		try{
			new GetAllUsersTask().execute();
		}catch(Exception e){e.printStackTrace();}
	}

	/**
	 * Sends a logout request to server, server disconnects client
	 */
	public void logOut(){
		try{
			new LogOutUserTask().execute();
		}catch(Exception e){e.printStackTrace();}
	}
	/**
	 * Sends NewChatInfo information to server, server stores new conversation
	 */
	public void createChat(NewChatInfo chatInfo){
		try{
			new CreateChatTask(chatInfo).execute();
		}catch(Exception e){e.printStackTrace();}
	}
	/**
	 * Sends Lock information to server, server verifies password
	 */
	public void verifyChatPassword(Lock lock){
		try{
			new VerifyChatPassword(lock).execute();
		}catch (Exception e){};
	}

	public String getUsername(){
		return user.getUsername();
	}
	/**
	 * Decrypts an encrypted message
	 */
	private void decryptMessage(Message message) {
		try {
			byte[] encrypted = message.getEncryptedMessage();
			String key = String.valueOf(message.getKey());
			for (int i = key.length(); i < 16; i++) {
				key += "A";
			}
			Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			// encrypt the text
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			String decrypted = new String(cipher.doFinal(encrypted));
			message.setText(decrypted);
		}
		catch ( Exception e){e.printStackTrace();}
	}

	/**
	 * Listener class with thread, handles communication between client/server
	 */
	private class Listener extends Thread {
		Intent chatIntent = new Intent(activityContext, ChattingActivity.class);
		public void playMessageRecieved(){
			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			MediaPlayer mp = MediaPlayer.create(activityContext, notification);
			mp.start();
		}
		public void run() {
		Object response;
		try {
			while(true) {
				response = (Object) input.readObject();
				if(response instanceof Message) {
					final Message message = (Message) response;
					decryptMessage(message);

					runOnUiThread(new Runnable(){

						@Override
						public void run() {
                            ArrayList<String> members = new ArrayList<>(ChattingActivity.getChatMembers());
							ArrayList<String> recipients = new ArrayList<>(message.getRecipients());
							recipients.add(message.getSender());
							Collections.sort(members);
							Collections.sort(recipients);
                            if(user.getUsername().equals(message.getSender())) {
                                ChattingActivity.list.add(message.getSender() + ":" + message.getText());
                            }
                            else if (members.equals(recipients)){
								ChattingActivity.list.add(message.getSender() + ":" + message.getText());
								if(!ChattingActivity.active) {
                                    Toast.makeText(activityContext, "New message from " + message.getSender(), Toast.LENGTH_SHORT).show();
                                    playMessageRecieved();
                                }
							}
							if(ChattingActivity.chatAdapter != null) {
								ChattingActivity.chatAdapter.notifyDataSetChanged();
							}
						}
					});
				}
				else if(response instanceof Boolean){
					Boolean exists = (Boolean) response;
					if(exists){
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(activityContext, "Username already exists, please choose a different username.", Toast.LENGTH_LONG).show();
							}
						});
					}
					else{
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activityContext);
						Boolean hasLoggedIn = prefs.edit().putBoolean("hasLoggedIn", true).commit();
						String[] users = new String[1];
						MainActivity.setNames(users);
						activityContext.startActivity(new Intent(activityContext, MainActivity.class));
					}
				}
				else if(response.equals("Failed")){
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(activityContext, "Wrong username or password, please try again.", Toast.LENGTH_LONG).show();
						}
					});
				}
				else if(response.equals("Logout")){
					activityContext.startActivity(new Intent(activityContext, LoginActivity.class));
				}
				else if(response instanceof ArrayList ) {
					ArrayList<String> chatMembers = (ArrayList<String>) response;

					if(chatMembers == null || chatMembers.size() == 0){
						String[] users = new String[1];
						MainActivity.setNames(users);
					}
					else{
						MainActivity.setNames(chatMembers);
					}
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activityContext);
					Boolean hasLoggedIn = prefs.edit().putBoolean("hasLoggedIn", true).commit();
					activityContext.startActivity(new Intent(activityContext, MainActivity.class));
				}
				else if(response instanceof Conversations){
					Conversations convo = (Conversations) response;
					chatIntent.putExtra("conversationList", convo.getConversation());
					chatIntent.putExtra("username", user.getUsername());
					chatIntent.putExtra("chatMembers", convo.getChatMembers());
					chatIntent.putExtra("chatID", convo.getChatID());
					activityContext.startActivity(chatIntent);
				}
				else if(response instanceof String[]){
					final String[] users = (String[]) response;
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							CreateChatActivity createChatActivity = new CreateChatActivity();
							createChatActivity.setData(users);
							Intent intent = new Intent(activityContext, createChatActivity.getClass());
							activityContext.startActivity(intent);

						}
					});
				}
				else if( response instanceof Lock){
					Lock lock = (Lock) response;
					Boolean valid = lock.getValid();
					if(valid) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (CustomAdapter.ativeChat.isEnabled()) {
									CustomAdapter.ativeChat.setEnabled(false);
								} else {
									CustomAdapter.ativeChat.setEnabled(true);
									Drawable img = activityContext.getDrawable(R.drawable.baseline_lock_open_black_18dp);
									img.setBounds(0,0,100,90);
									CustomAdapter.activeLockBtn.setCompoundDrawables(null, null, null, img );
									runOnUiThread(new Runnable() {
										public void run() {
											Toast.makeText(activityContext, "Chat unlocked!", Toast.LENGTH_LONG).show();
										}
									});
								}
							}
						});
					}
				}
			}
		} catch(IOException | ClassNotFoundException e) {e.printStackTrace();}
	}
	}
	private class ConnectTask extends AsyncTask<String, String, String> {

		private String resp;
		ProgressDialog progressDialog;

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				String serverAddr =  "192.168.1.22";
				socketClient = new Socket(serverAddr, 6445);

				output = new ObjectOutputStream(socketClient.getOutputStream());
				input = new ObjectInputStream(socketClient.getInputStream());
				new Listener().start();
			}
			catch (UnknownHostException e1) {
			e1.printStackTrace();
			} catch (IOException e1) {
			e1.printStackTrace();
		}
			return resp;
		}

		@Override
		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
			progressDialog.dismiss();
		}


		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(activityContext,
					"ProgressDialog",
					"Wait for it");
		}


		@Override
		protected void onProgressUpdate(String... text) {

		}
	}

	private class CreateUserTask extends AsyncTask<String, String, String> {

		private String resp;
		private User user;
		ProgressDialog progressDialog;

		public CreateUserTask(User user){
			this.user = user;
		}

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				output.writeObject(user);
				output.flush();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			return resp;
		}

		@Override
		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
			progressDialog.dismiss();
		}


		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(activityContext,
					"ProgressDialog",
					"Wait for it");
		}
		@Override
		protected void onProgressUpdate(String... text) {

		}
	}

	private class LoginUserTask extends AsyncTask<String, String, String> {

		private String resp;
		private User user;
		private String[] userData = new String[2];
		ProgressDialog progressDialog;

		public LoginUserTask(User user){
			this.user = user;
			String username = user.getUsername();
			String password = user.getPassword();
			userData[0] = username;
			userData[1] = password;
		}

		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {

				output.writeObject(userData);
				output.flush();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			return resp;
		}

		@Override
		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
			progressDialog.dismiss();
		}


		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(activityContext,
					"ProgressDialog",
					"Wait for it");
		}
		@Override
		protected void onProgressUpdate(String... text) {

		}
	}
	private class SendConversationTask extends AsyncTask<String, Void, String> {

		private String resp;
		private Conversations convo;
		ProgressDialog progressDialog;

		public SendConversationTask(Conversations convo){
			this.convo = convo;
		}

		@Override
		protected String doInBackground(String... params) {// Calls onProgressUpdate()
			try {

				output.writeObject(convo);
				output.flush();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			return resp;
		}

		@Override
		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
			progressDialog.dismiss();
		}


		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(activityContext,
					"ProgressDialog",
					"Wait for it");
		}
	}
	private class SendMessageTask extends AsyncTask<String, String, String> {

		private String resp;
		private Message message;
		ProgressDialog progressDialog;

		public SendMessageTask(Message message){
			this.message = message;
		}

		@Override
		protected String doInBackground(String... params) {// Calls onProgressUpdate()
			try {
				byte[] encryptedMessage = encryptMessage(message.getText(), message.getKey());
				message.setEncryptedMessage(encryptedMessage);
				message.setText(new String(encryptedMessage));

				output.writeObject(message);
				output.flush();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			return resp;
		}

		@Override
		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
			progressDialog.dismiss();
		}


		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(activityContext,
					"ProgressDialog",
					"Wait for it");
		}
		private byte[] encryptMessage(String message, int id){
			try {

				String key = String.valueOf(id);
				for (int i = key.length(); i < 16; i++) {
					key += "A";
				}
				Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
				Cipher cipher = Cipher.getInstance("AES");
				// encrypt the text
				cipher.init(Cipher.ENCRYPT_MODE, aesKey);
				byte[] encrypted = cipher.doFinal(message.getBytes());
				return encrypted;
			}
			catch ( Exception e){e.printStackTrace();}

			return null;
		}
	}
	private class GetAllUsersTask extends AsyncTask<String, Void, String> {

		private String resp;
		private Message message;
		ProgressDialog progressDialog;


		@Override
		protected String doInBackground(String... params) {// Calls onProgressUpdate()
			try {

				output.writeObject("getUsers");
				output.flush();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			return resp;
		}

		@Override
		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
			progressDialog.dismiss();
		}


		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(activityContext,
					"ProgressDialog",
					"Wait for it");
		}
	}
	private class CreateChatTask extends AsyncTask<String, Void, String> {

		private String resp;
		private Message message;
		private NewChatInfo chatInfo;
		ProgressDialog progressDialog;

		public CreateChatTask(NewChatInfo chatInfo){
			this.chatInfo = chatInfo;
		}

		@Override
		protected String doInBackground(String... params) {// Calls onProgressUpdate()
			try {
				output.reset();
				output.writeObject(chatInfo);
				output.flush();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			return resp;
		}

		@Override
		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
			progressDialog.dismiss();
		}


		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(activityContext,
					"ProgressDialog",
					"Wait for it");
		}
	}
	private class VerifyChatPassword extends AsyncTask<String, Void, String> {

		private String resp;
		private Message message;
		private Lock lock;
		ProgressDialog progressDialog;

		public VerifyChatPassword(Lock lock) {
			this.lock = lock;
		}

		@Override
		protected String doInBackground(String... params) {// Calls onProgressUpdate()
			try {

				output.writeObject(lock);
				output.flush();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			return resp;
		}

		@Override
		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
			progressDialog.dismiss();
		}


		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(activityContext,
					"ProgressDialog",
					"Wait for it");
		}
	}

	private class LogOutUserTask extends AsyncTask<String, Void, String> {

		private String resp;
		ProgressDialog progressDialog;

		public LogOutUserTask() {
		}

		@Override
		protected String doInBackground(String... params) {// Calls onProgressUpdate()
			try {

				output.writeObject("Logout");
				output.flush();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			return resp;
		}

		@Override
		protected void onPostExecute(String result) {
			// execution of result of Long time consuming operation
			progressDialog.dismiss();
		}


		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(activityContext,
					"ProgressDialog",
					"Wait for it");
		}
	}


}
