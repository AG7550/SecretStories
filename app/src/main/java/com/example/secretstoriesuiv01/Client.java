package com.example.secretstoriesuiv01;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

public class Client extends AppCompatActivity {
	private Socket socketClient;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private int port;
	private String ip;
	private User user;
	private Context contextClient;
	private Context activityContext;
	
	public Client(int port, String ip) {
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
	public void createUser(User user) {
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
	public void login(User user){
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

	private class Listener extends Thread{
		public void run() {
		Object response;
		try {
			while(true) {
				response = (Object) input.readObject();
				if(response instanceof Message) {
					Message message = (Message) response;
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
						activityContext.startActivity(new Intent(activityContext, MainActivity.class));
					}
				}else if(response.equals("Failed")){
						Toast.makeText(activityContext, "Login failed. Please try again", Toast.LENGTH_SHORT);

				}else if(response instanceof ArrayList ) {
					ArrayList<String> chatMembers = (ArrayList<String>) response;
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activityContext);
					Boolean hasLoggedIn = prefs.edit().putBoolean("hasLoggedIn", true).commit();
					MainActivity.setNames(chatMembers);
					activityContext.startActivity(new Intent(activityContext, MainActivity.class));
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
				String serverAddr = "10.2.29.99";
				socketClient = new Socket(serverAddr, 8000);

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

}