package c.b.a.sudokuapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple login screen where an existing user can sign in to his/her account
 */

public class LoginActivity extends AppCompatActivity {

    private EditText login_email;
    private EditText login_password;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private LoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setVariables();

        //The structure for this code was gotten from facebook developer-site.
        //https://firebase.google.com/docs/auth/android/facebook-login >> Authenticate with Firebase >> developer's documentation
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile","user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() { }

            @Override
            public void onError(FacebookException error) { }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //updateUI(currentUser);

        //check if user is logged in via facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        // If user is logged in, he/she is taken to the Main Menu
        if(firebaseAuth.getCurrentUser() != null || isLoggedIn) {
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            finish();
        }
    }


    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }

    //The structure for this code was gotten from the facebook developer-site.
    //https://firebase.google.com/docs/auth/android/facebook-login >> Authenticate with Firebase >> developer's documentation
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Set instance variables
     */
    private void setVariables() {
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }


    public void login(View view) {

        String email = login_email.getText().toString();
        String password = login_password.getText().toString();

        // If input fields are not empty
        if(isEmailValid(email) && isPasswordValid(password)) {
            // Feedback so the user knows something is happening
            progressDialog.setMessage("Logging in...");
            progressDialog.show();

            // Checks if inputs match email and password in the database, i.e. if user exists
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            // If successful, the user is taken to the Main Menu
                            if(task.isSuccessful()) {
                                goToMainMenu();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Email or password incorrect!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * User taken to the Main Menu
     */
    private void goToMainMenu() {
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }

    /**
     * If user does not have an account, he/she is taken to the Register screen
     * @param view
     */
    public void goToSignup(View view) {
        finish();
        startActivity(new Intent(this, RegisterActivity.class));
    }

    /**
     * Input validator for email input
     * Checks if input is empty and returns false if so
     * @param email
     * @return
     */
    private boolean isEmailValid(String email) {

        if(email.trim().length() == 0) {
            login_email.requestFocus();
            login_email.setError(getString(R.string.required));
            return false;
        }
        return true;
    }

    /**
     * Input validator for password input
     * Checks if input is empty and returns false if so
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {

        if(password.trim().length() == 0) {
            login_password.requestFocus();
            login_password.setError(getString(R.string.required));
            return false;
        }
        return true;
    }


}
