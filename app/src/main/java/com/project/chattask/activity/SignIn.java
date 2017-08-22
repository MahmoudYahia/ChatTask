package com.project.chattask.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.chattask.callBackInterface.onCheckAuthorizationListener;
import com.project.chattask.model.Contact;
import com.project.chattask.R;
import com.project.chattask.model.SignInAuthorization;

public class SignIn extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, onCheckAuthorizationListener {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 2;
    private SignInButton mSignInButton;


    Button loginBtn, RegisterBtn;
    EditText InputEmial, InputPass;
    ImageView logoImg;
    ProgressBar SignInProgresBar;

    SignInAuthorization signInAuthorization;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        logoImg = (ImageView) findViewById(R.id.signin_logo_img);
        SignInProgresBar = (ProgressBar) findViewById(R.id.sign_in_progress);

        InputEmial = (EditText) findViewById(R.id.txt_email);
        InputPass = (EditText) findViewById(R.id.txt_pass);

        loginBtn = (Button) findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);

        RegisterBtn = (Button) findViewById(R.id.btn_sign_up);
        RegisterBtn.setOnClickListener(this);


        mSignInButton = (SignInButton) findViewById(R.id.sign_in_google);
        mSignInButton.setOnClickListener(this);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mFirebaseAuth = FirebaseAuth.getInstance();
        signInAuthorization = new SignInAuthorization(this, this);
        mGoogleApiClient = signInAuthorization.buildApiClient(); // build Api Client


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sign_in_google:
                signIn();

                break;

            case R.id.btn_login:
                onLogInBtnPressed();
                break;
            case R.id.btn_sign_up:
                startActivity(new Intent(SignIn.this, SignUp.class));
                break;

        }
    } //t

    public void onLogInBtnPressed() {
        String userMail = InputEmial.getText().toString();
        String userPass = InputPass.getText().toString();

        if (TextUtils.isEmpty(userMail) || TextUtils.isEmpty(userPass)) {
            Toast.makeText(getBaseContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();

        } else {
            signInAuthorization.checkEmialPassword(userMail, userPass);
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            //SignInProgresBar.setVisibility(View.GONE); // hide

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                signInAuthorization.firebaseAuthWithGoogle(account);

            } else {
                // not Success
                Toast.makeText(this, "Authentication Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    @Override
    public void autherizationSuccess() {
        startActivity(new Intent(this, ContactsListActivity.class));
        finish();
    }

    @Override
    public void autherizationFailed() {
        Toast.makeText(SignIn.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

}