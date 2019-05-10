package com.example.secretstoriesuiv01;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
import java.util.ArrayList;
import java.util.Observable;

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
	
	public void connect(Context context) {
		activityContext = context;

//		try {
//			socketClient = new Socket(ip,port);
//			output = new ObjectOutputStream(socketClient.getOutputStream());
//			input = new ObjectInputStream(socketClient.getInputStream());
//			new Listener().start();

//		}
//		catch(IOException e)
//		{
//			e.printStackTrace();
//		}

		try{
			new ConnectTask().execute();
		} catch (Exception e){
			e.printStackTrace();
		}


	}
	public void setUser(User user){
		this.user = user;
	}

	public void createUser(User user) {
		this.user = user;
//		try {
//			output.writeObject(user);
//			output.flush();
//		}
//		catch(IOException e) {
//			e.printStackTrace();
//		}

		try{
			new CreateUserTask(user).execute();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public void sendMessage(Message message){
		try{
			new SendMessageTask(message).execute();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public void login(User user){
		this.user = user;
//		try {
//			output.writeObject(user);
//		}
//		catch(IOException e) {
//			e.printStackTrace();
//		}
		try{
			new LoginUserTask(user).execute();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public void getChat(Conversations convo){
		try{
			new SendConversationTask(convo).execute();
		}
		catch(Exception e){e.printStackTrace();}
	}

	public void getAllUsers(Context context){
		try{
			new GetAllUsersTask().execute();
		}catch(Exception e){e.printStackTrace();}
	}
	public void logOut(){
		try{
			new LogOutUserTask().execute();
		}catch(Exception e){e.printStackTrace();}
	}

	public void createChat(NewChatInfo chatInfo){
		try{
			new CreateChatTask(chatInfo).execute();
		}catch(Exception e){e.printStackTrace();}
	}

	public void verifyChatPassword(Lock lock){
		try{
			new VerifyChatPassword(lock).execute();
		}catch (Exception e){};
	}

	public String getUsername(){
		return user.getUsername();
	}

	/**
	 *
	 * public void createButton(ArrayList<String> chatMembers){
	 * 		String[] names;
	 * 		LinearLayout layout;
	 * 		setContentView(R.layout.fragment_chat);
	 * 		for(String member : chatMembers){
	 * 			names = member.split(":");
	 * 			Button b = new Button(this);
	 * 			for(String name : names){
	 * 				b.append(name + ", ");
	 *                        }
	 * 			layout = (LinearLayout) findViewById(R.id.rootlayout);
	 * 			layout.addView(b);
	 *
	 *
	 * 			b.setOnClickListener(new View.OnClickListener() {
	 *                @Override
	 * 				public void onClick(View v) {
	 * 					// vad som ska hända när man trycker på knapparna.
	 * 				}
	 * 			});
	 * 		}
	 * 	}
	 */

	private class Listener extends Thread {
		Intent chatIntent = new Intent(activityContext, ChattingActivity.class);
		public void run() {
		Object response;
		try {
			while(true) {
				response = (Object) input.readObject();
				if(response instanceof Message) {
					final Message message = (Message) response;
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							ChattingActivity.list.add(message.getSender() + ":" + message.getText());
							ChattingActivity.chatAdapter.notifyDataSetChanged();
						}
					});
					//chatIntent.putExtra(txtMessage, "newMessage"); //Detta ska hända innan "getExtra" i chattingActivity när man trycker på "send" knappen
				}
				else if(response instanceof Boolean){
					Boolean exists = (Boolean) response;
					if(exists){
						Toast.makeText(activityContext, "User already exists. Please choose a different usename.", Toast.LENGTH_SHORT);
					}
					else{
						// Öppna fönster blabla
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activityContext);
						Boolean hasLoggedIn = prefs.edit().putBoolean("hasLoggedIn", true).commit();
						String[] users = new String[1];
						MainActivity.setNames(users);
						activityContext.startActivity(new Intent(activityContext, MainActivity.class));
					}
				}else if(response.equals("Failed")){		//funkar ej måste använda instace
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
						Toast.makeText(activityContext, "Login failed. Please try again", Toast.LENGTH_SHORT);

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
							// för att uppdatera chattarna

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
//								CustomAdapter.activeLockBtn.setBackground(getResources().getDrawable(R.drawable.baseline_lock_black_18dp));
								} else {
									CustomAdapter.ativeChat.setEnabled(true);
//								CustomAdapter.activeLockBtn.setBackground(getResources().getDrawable(R.drawable.baseline_lock_open_black_18dp));
								}


							}
						});
					}

//					if(valid){
//						CustomAdapter.ativeChat.setEnabled(true);
//					}
//					else {
//						//felmeddelande
//					}
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
				String serverAddr =  "10.2.19.40";
				socketClient = new Socket(serverAddr, 6666);

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
