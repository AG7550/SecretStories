package com.example.secretstoriesuiv01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Screen that lets the user create and register a account
 */
public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    /**
     * Closes this activity taking the user back to 'LoginActivity'.
     * @param view
     */
    public void haveAccount(View view){
        finish();
    }

    /**
     * The user registers a new account.
     * Closes this activity.
     * @param view
     */
    public void createAccount(View view){ finish();}
}
