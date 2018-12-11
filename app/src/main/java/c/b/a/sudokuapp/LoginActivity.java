package c.b.a.sudokuapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;


/**
 * A simple login screen where an existing user can sign in to his/her account
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    // Views
    private EditText login_email;
    private EditText login_password;
    private Button login_btn;
    private TextView signup_txt;

    // Variables for Google sign-in
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    // Last signed in Google account
    private GoogleSignInAccount accountGoogle;
    // Used in MenuFragment and DifficultyFragment to know if user is logged in with Google
    public static GoogleSignInAccount account;
    private SignInButton googleButton;
    private int RC_SIGN_IN = 123;

    // Variables for Facebook sign-in
    private boolean isLoggedInWithFB;
    private CallbackManager callbackManager;
    private LoginButton facebookButton;


    /**
     * Sets variables and sets up the login option for Facebook
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set variables
        setVariables();

        setUpFacebookLogin();
    }

    /**
     * Makes sure to take the user to the Main menu
     */
    @Override
    public void onStart() {
        super.onStart();

        // If user is logged in, he/she is taken to the Main Menu
        if (firebaseAuth.getCurrentUser() != null || isLoggedInWithFB || accountGoogle != null) {
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            finish();
        }
    }

    /**
     * Set instance variables
     */
    private void setVariables() {
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_btn);
        signup_txt = findViewById(R.id.signup_txt);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();
        // Facebook login button
        facebookButton = findViewById(R.id.login_button);

        // Check if user is logged in via Facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedInWithFB = accessToken != null && !accessToken.isExpired();

        // Current Google account / last signed in Google account
        accountGoogle = GoogleSignIn.getLastSignedInAccount(this);
        googleButton = findViewById(R.id.sign_in_button);

        // Configure sign-in to request the user's ID, email address, and basic profile
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // The Google sign in button
        googleButton.setSize(SignInButton.SIZE_STANDARD);

        // Set click listeners on buttons
        googleButton.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        signup_txt.setOnClickListener(this);
    }

    /**
     * Initializes the Facebook Login button
     */
    private void setUpFacebookLogin() {
        // Callback registration
        facebookButton.setReadPermissions("email", "public_profile");
        facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                handleAccessToken(credential);
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException exception) {}
        });
    }

    /**
     * For both Facebook and Google login
     * Structure for the code obtained from firebase.google.com
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Google
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    /**
     * Called when user chooses to log in with email and passwords
     * If login is successful, user is taken to the Main menu
     * If login fails, user gets descriptive message
     */
    private void loginWithEmail() {

        String email = login_email.getText().toString();
        String password = login_password.getText().toString();

        // If input fields are not empty
        if (isEmailValid(email) && isPasswordValid(password)) {
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
                            if (task.isSuccessful()) {
                                goToMainMenu();
                            } else {
                                Toast.makeText(LoginActivity.this, "Email or password incorrect!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * Input validator for email input without db access
     * Checks if input is empty and returns false if so
     * @param email
     * @return
     */
    private boolean isEmailValid(String email) {

        if (email.trim().length() == 0) {
            login_email.requestFocus();
            login_email.setError(getString(R.string.required));
            return false;
        }
        return true;
    }

    /**
     * Input validator for password input without db access
     * Checks if input is empty and returns false if so
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {

        if (password.trim().length() == 0) {
            login_password.requestFocus();
            login_password.setError(getString(R.string.required));
            return false;
        }
        return true;
    }

    /**
     * Called then user chooses to log in via Google
     * If login is successful, user is authenticated with Firebase
     * If login fails, a message appears on the screen for user to know
     * @param completedTask
     */
    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            account = completedTask.getResult(ApiException.class);
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            handleAccessToken(credential);
        } catch (ApiException e) {
            Toast.makeText(this, "Login with Google failed", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Called when user chooses to login via Google or Facebook
     * If login is successful, user is taken to the Main menu
     * If login fails, a message appears on screen for user to know
     * @param credential either Facebook or Google
     */
    private void handleAccessToken(AuthCredential credential) {

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goToMainMenu();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            LoginManager.getInstance().logOut();
                        }
                    }
                });
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
     */
    private void goToSignup() {
        finish();
        startActivity(new Intent(this, RegisterActivity.class));
    }

    /**
     * Called when user chooses to sign in with Google
     */
    private void GoogleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        // Called when user does not have an account and chooses to register
        if (v == signup_txt) {
            goToSignup();
        }
        // Called when user logs in with email and password
        else if (v == login_btn) {
            loginWithEmail();
        }
        // Called when the Google sign-in button is clicked
        else if (v == googleButton) {
            GoogleSignIn();
        }
    }
}

