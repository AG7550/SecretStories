package com.example.secretstoriesuiv01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    public static Client client;
    private ConnectDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);


        client = new Client(6666,  "192.168.1.25"); // "192.168.1.104"
    }

    public void toLogin(View view){
        finish();
    }

    public void createAccount(View view){
        //TODO check if first and surname is entered
        Toast.makeText(this, username.getText().toString() + " " + password.getText().toString(), Toast.LENGTH_SHORT).show();

        String userString = username.getText().toString();
        String passwordString = password.getText().toString();

        User user = new User(userString, passwordString);
        client.connect(this);
        client.createUser(user);
    }

    public void onFinishCallback()
    {
        this.finish();
    }
}
