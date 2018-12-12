package c.b.a.sudokuapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import static c.b.a.sudokuapp.MenuFragment.currUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstructionFragment extends Fragment implements View.OnClickListener{
    private TextView logout;
    private Activity a;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInOptions gso;


    public InstructionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instructions, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        a = getActivity();
        logout = a.findViewById(R.id.diff_logout);
        logout.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(a, gso);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();

        // Current activity
        Activity a = getActivity();
        TextView userTxt = a.findViewById(R.id.diff_user);

        // If some user is logged in, welcome the user with his/her email
        if(currUser != null) {
            userTxt.setText(getString(R.string.welcome_user) + splitUserEmail(currUser.getEmail()));
        }
    }

    /**
     * A method to cut of the part in front of the @ symbol
     * @param email
     * @return
     */
    private String splitUserEmail(String email) {
        String[] emailArr = email.split("@");
        return emailArr[0];
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        mGoogleSignInClient.signOut();
        firebaseAuth.signOut();

        a.finish();
        a.startActivity(new Intent(a, LoginActivity.class));
    }

    @Override
    public void onClick(View v) {
        if(v == logout) {
            logout();
        }
    }
}
