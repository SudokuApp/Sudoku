package c.b.a.sudokuapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


/**
 * This Activity is to register new user into the app.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    // Views
    private EditText email, password;
    private Button register;
    private TextView login_txt;

    private ProgressDialog progressDialog;

    // Firebase authentication
    private FirebaseAuth firebaseAuth;
    // Reference of Firebase database
    private DatabaseReference mDatabaseRef;


    /**
     * Sets variables and makes sure to take the user to the Main menu if he/she
     * is already logged in
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set variables
        setVariables();

        // TODO taka út? ferð aldrei her inn
        // If you are logged in, you should go straight to the menu activity
        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        }
    }

    /**
     * Set instance variables
     */
    private void setVariables() {
        email = findViewById(R.id.emailreg);
        password = findViewById(R.id.passwordred);
        register = findViewById(R.id.buttonregister);
        login_txt = findViewById(R.id.textgotolgoin);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        // Set click listeners
        register.setOnClickListener(this);
        login_txt.setOnClickListener(this);
    }

    /**
     * Called when the register button is clicked
     * If registration is successful, user is created in the database
     * and the user is taken to the Main menu
     * If registration fails, user gets descriptive message
     */
    private void register() {
        String pw = password.getText().toString().trim();
        final String em = email.getText().toString().trim();

        //If everything is valid
        if(validPassword(pw) && validEmail(em)) {
            progressDialog.setMessage(getString(R.string.progressRegister));
            progressDialog.show();

            //creates user
            firebaseAuth.createUserWithEmailAndPassword(em,pw)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()) {
                                writeNewUser(em);

                                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                                finish();

                            } else if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    email.requestFocus();
                                    email.setError(getString(R.string.email_taken));
                            } else {
                                Toast.makeText(RegisterActivity.this, getString(R.string.registration_failed), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    /**
     * Input validator without db access
     * @param em the email
     * @return
     */

    private boolean validEmail(String em) {
        if(em.length() == 0) {
            email.requestFocus();
            email.setError(getString(R.string.required));
            return false;
        }
        //the email has to be in a valid email form
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
            email.requestFocus();
            email.setError(getString(R.string.invalidEmail));
            return false;
        }
        return true;
    }

    /**
     * Input validator without db access
     * @param pw the password
     * @return
     */
    private boolean validPassword(String pw) {
        if(pw.length() == 0) {
            password.requestFocus();
            password.setError(getString(R.string.required));
            return false;

        }
        //the password has to be 6 or more letters
        else if(pw.length() < 6) {
            password.requestFocus();
            password.setError(getString(R.string.invalidPassword));
            return false;
        }
        return true;
    }

    /**
     * If user already has an account he/she is taken to the Login screen
     */
    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /**
     * Called when registration is successful
     * Creates a new user in the database
     * @param email user's email
     */
    private void writeNewUser(String email) {

        User user = new User(email);
        mDatabaseRef.child("users").child(firebaseAuth.getUid()).setValue(user);
    }

    @Override
    public void onClick(View v) {
        // Called when register button is clicked
        if (v == register) {
            register();
        }
        // Called when user already has an account and chooses to log in
        if (v == login_txt) {
            goToLogin();
        }
    }
}
