package nas509.groceries.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import nas509.groceries.R;

/**
 * The welcome screen first showed when app is launched, provides options to
 * login an existing user or register a new user
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    /**
     * Handles when the login button is pressed
     * @param view      The view calling this method
     */
    public void loginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Handles when the register button is pressed
     * @param view      The view calling this method
     */
    public void registerClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
