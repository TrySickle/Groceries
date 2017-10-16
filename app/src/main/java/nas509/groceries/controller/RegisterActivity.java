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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import nas509.groceries.R;
import nas509.groceries.model.User;
import nas509.groceries.model.UserManager;

/**
 * A registration screen that offers registration via username and password
 */
public class RegisterActivity extends AppCompatActivity {

    /** UI references */
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Spinner mUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        // sets up password edit action listener
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        // Spinner setup
        UserManager userManager = UserManager.getInstance();
        //mUserType = (Spinner) findViewById(R.id.user_type_spinner);
        //ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, userManager.getUserTypes());
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //mUserType.setAdapter(adapter);

        // setup register button listener
        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
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

    /** when cancel is pressed, closes activity */
    private void cancel() {
        finish();
    }

    /**
     * Attempts to register the account specified by the registration form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual registration attempt is made.
     */
    private void attemptRegister() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        //boolean spinnerValue = ((String) mUserType.getSelectedItem()).equals("User");

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError("This field is required");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError("This field is required");
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError("This username is taken");
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // depending on spinner value, registers a new User or Admin
            UserManager userManager = UserManager.getInstance();
            User user = new User(username, password);
            userManager.addUser(user);
            userManager.setLoggedInUser(user);
            

            // Starts AppActivity and clears the activity stack
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // hide the keyboard after register
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            startActivity(intent);
        }

    }

    /**
     * Checks if a username is valid - must not be empty and must not already
     * be registered
     * @param username      The username to check
     * @return              True if username is valid, false if not
     */
    private boolean isUsernameValid(String username) {
        UserManager userManager = UserManager.getInstance();
        return username.length() > 0 && !userManager.containsUser(username);
    }

    /**
     * Checks if a password is valid, if it is not empty
     * @param password      The password to check
     * @return              True if valid, false if not
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }
}

