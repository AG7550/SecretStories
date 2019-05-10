package com.example.secretstoriesuiv01;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static String[] names;
    Dialog dialog;
    SharedPreferences prefs;
    Boolean hasLocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean hasLoggedIn = prefs.getBoolean("hasLoggedIn", false);
        if(!hasLoggedIn){
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            setContentView(R.layout.activity_main);

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);
            bottomNavigationView.setOnNavigationItemSelectedListener
                    (new BottomNavigationView.OnNavigationItemSelectedListener() {
                        Fragment selectedFragment = null;

                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                            if (item != selectedFragment) {
                                switch (item.getItemId()) {

                                    case R.id.navigation_chat:
                                        selectedFragment = ChatFragment.newInstance("CHANGE THIS", names);
                                        break;
                                    case R.id.navigation_contacts:
                                        selectedFragment = ContactsFragment.newInstance("CHANGE THIS", "CHANGE THIS");
                                        break;
                                    case R.id.navigation_search:
                                        selectedFragment = SearchFragment.newInstance("CHANGE THIS", "CHANGE THIS");
                                        break;
                                    case R.id.navigation_notifications:
                                        selectedFragment = NotificationsFragment.newInstance("CHANGE THIS", "CHANGE THIS");
                                        break;
                                }
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                                transaction.replace(R.id.frame_layout, selectedFragment);
                                transaction.commit();
                                return true;
                            }
                            return true;
                        }
                    });

            Button createChatbtn = (Button) findViewById(R.id.writemessagebutton);
            createChatbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // skicka till client som skickar till server. När client tar emot så ska den starta aktiviten nedan men först ge datan i kunstruktorn
                    if(LoginActivity.client != null){
                        LoginActivity.client.getAllUsers(v.getContext());
                    }
                    else{
                        CreateAccountActivity.client.getAllUsers(v.getContext());
                    }

                }
            });

            Button keyButton = (Button) findViewById(R.id.keyBtn);
            keyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hasLocked = prefs.getBoolean("hasLocked", false);
                    dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.dialog_template);
                    EditText lockPassword = (EditText) dialog.findViewById(R.id.lockPassword);
                    if(!hasLocked){
                        lockPassword.setText("Enter password to unlock chats");
                    }
                    else{
                        lockPassword.setText("Enter password to lock chats");
                    }
                    dialog.show();
                    Button lockBtn = (Button) dialog.findViewById(R.id.lockBtn);
                    lockBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // kolla lösenord
                            if(!hasLocked){
                                ChatFragment.lvwUsers.setClickable(false);
                                hasLocked = prefs.edit().putBoolean("hasLocked", true).commit();

                            }
                            else{
                                ChatFragment.lvwUsers.setClickable(true);
                                hasLocked = prefs.edit().putBoolean("hasLocked", false).commit();

                            }

                            // Gå igenom och diseble alla chattar. sen när man går igenom dem så kolla om om de är enable..
                            dialog.cancel();
                        }
                    });
                }
            });

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, ChatFragment.newInstance("CHANGE THIS",  names));
            transaction.commit();
            hasLoggedIn = prefs.edit().putBoolean("hasLoggedIn", false).commit();
        }


    }

    public void toLogin(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void createChat(View view){
        startActivity(new Intent(this, ChatActivity.class));
    }

    public static void setNames(ArrayList<String> list){
        names = new String[list.size()];
        for(int i = 0; i<names.length; i++){
            String tempName = "";
            String[] temp = list.get(i).split(":");
            for(String name : temp){
                String attach = name;
                if(!attach.isEmpty()) {
                    tempName += attach + ", ";
                }
            }
            names[i] = tempName.substring(0, tempName.length() - 2);
        }
    }
    public static void setNames(String[] list){
        names = list;
    }

}