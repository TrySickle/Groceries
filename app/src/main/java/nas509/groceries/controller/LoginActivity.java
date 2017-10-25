package nas509.groceries.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import nas509.groceries.R;
import nas509.groceries.model.Model;
import nas509.groceries.model.UserManager;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {

    /** UI references */
    private EditText mUsernameView;
    private EditText mPasswordView;
    /** singleton model facade */
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        model = Model.getInstance();
        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        // sets up password edit action listener, doesn't really do anything
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // setup login button listener
        Button mLogInButton = (Button) findViewById(R.id.login_button);
        mLogInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // setup cancel button listener
        Button mCancelButton = (Button) findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

    }

    /**
     * Retrieve database for users and groups
     */
    @Override
    protected void onStart() {
        super.onStart();
        model.retrieveUsers();
        model.retrieveGroups();
    }

    /** when cancel is pressed, closes activity */
    private void cancel() {
        finish();
    }

    /**
     * Attempts to login the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for an empty username
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError("This field is required");
            focusView = mUsernameView;
            cancel = true;
        }

        // Check for an empty password
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("This field is required");
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(username, password)) {
            mPasswordView.setError("This password is incorrect");
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Sets the currently logged in user and clears the activity stack
            Model model = Model.getInstance();
            model.setLoggedInUser(model.getUser(username));
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // hides the keyboard after login
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            startActivity(intent);
        }
    }

    /**
     * Checks that the username and password are a valid combination
     * @param username      The username to check
     * @param password      The password to check
     * @return              True if valid, false if not
     */
    private boolean isPasswordValid(String username, String password) {
        UserManager userManager = UserManager.getInstance();
        return userManager.checkUserCredentials(username, password);
    }
}

