package com.example.secretstoriesuiv01;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

/**
 * Screen that holds most fragments that can be accessed through bottomnavigation.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets up bottomnavigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    Fragment selectedFragment = null;
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        if(item != selectedFragment){
                            switch (item.getItemId()) {

                                case R.id.navigation_home:
                                    selectedFragment = HomeFragment.newInstance("CHANGE THIS", "CHANGE THIS");
                                    break;
                                case R.id.navigation_chat:
                                    selectedFragment = ChatFragment.newInstance("CHANGE THIS", "CHANGE THIS");
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

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance("CHANGE THIS", "CHANGE THIS"));
        transaction.commit();
    }

    /**
     * Opens 'LoginActivity'
     * @param view
     */
    public void toLogin(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     * Opens 'ChatActivity'
     * @param view
     */
    public void createChat(View view){
        startActivity(new Intent(this, ChatActivity.class));
    }
}
