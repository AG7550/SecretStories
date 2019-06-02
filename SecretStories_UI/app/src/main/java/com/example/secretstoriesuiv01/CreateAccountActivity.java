package com.example.secretstoriesuiv01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Sandra Smrekar, Ali Menhem
 * Activity for creating an account and reads inputs
 */
public class CreateAccountActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText chatPassword;
    private EditText password2;
    private EditText chatPassword2;
    public static Client client;
    private ConnectDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);
        chatPassword = findViewById(R.id.chat_password);
        password2 = findViewById(R.id.input_password2);
        chatPassword2 = findViewById(R.id.chat_password2);


         // "192.168.1.104"
    }

    public void toLogin(View view){
        finish();
    }

    public void createAccount(View view){
        //TODO check if first and surname is entered
        Toast.makeText(this, username.getText().toString() + " " + password.getText().toString(), Toast.LENGTH_SHORT).show();

        String userString = username.getText().toString();
        String passwordString = password.getText().toString();
        String chatPasswordString = chatPassword.getText().toString();
        String passwordString2 = password2.getText().toString();
        String chatPasswordString2 = chatPassword2.getText().toString();

        if(!(passwordString.equals(passwordString2) || chatPasswordString.equals(chatPasswordString2))){
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_LONG).show();
        }
        else if(userString.length() < 2){
            Toast.makeText(this, "Username cannot be under 2 characters.", Toast.LENGTH_LONG).show();
        }
        else if(passwordString.length() <= 4){
            Toast.makeText(this, "Password cannot be under 5 characters.", Toast.LENGTH_LONG).show();
        }
        else if(chatPasswordString.length() <= 4){
            Toast.makeText(this, "Password for chat-lock cannot be under 5 characters", Toast.LENGTH_LONG).show();
        }
        else{
            User user = new User(userString, passwordString, chatPasswordString);
            client = new Client();
            client.connect(this);
            client.createUser(user);
            client.setUser(user);
        }


    }

    public void onFinishCallback()
    {
        this.finish();
    }
}
