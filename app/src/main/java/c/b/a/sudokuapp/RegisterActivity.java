package c.b.a.sudokuapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;


/**
 * This Activity is to register new user into the app.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText email, password;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.emailreg);
        password = findViewById(R.id.passwordred);

        firebaseAuth = FirebaseAuth.getInstance();

        //if you are logged in, you should go straight to the menu activity
        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        }

        progressDialog = new ProgressDialog(this);
    }

    public void Register(View view) {
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
                                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                                finish();

                            } else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                //if the email is taken
                                email.requestFocus();
                                email.setError(getString(R.string.emailTaken));
                            }
                        }
                    });
        }
    }

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
     * if user already has an account
     */
    public void goToLogin(View v) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
