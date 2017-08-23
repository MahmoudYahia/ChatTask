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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.chattask.callback.signIn.OnCheckEmailAndPass;
import com.project.chattask.callback.signIn.OnGoogleSignListner;
import com.project.chattask.callback.signIn.onCheckAuthorizationListener;
import com.project.chattask.R;
import com.project.chattask.model.EmailAndPassSignIn;
import com.project.chattask.model.GoogleSignIn;

public class SignInActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        onCheckAuthorizationListener {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 2;
    private SignInButton mSignInButton;


    Button loginBtn, RegisterBtn;
    EditText InputEmial, InputPass;
    ImageView logoImg;
    ProgressBar SignInProgresBar;
    OnGoogleSignListner onGoogleSignListner;

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
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                break;

        }
    } //t

    public void onLogInBtnPressed() {
        String userMail = InputEmial.getText().toString();
        String userPass = InputPass.getText().toString();

        if (TextUtils.isEmpty(userMail) || TextUtils.isEmpty(userPass)) {
            Toast.makeText(getBaseContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();

        } else
            {

            OnCheckEmailAndPass onCheckEmailAndPass=new EmailAndPassSignIn(this,this);
            onCheckEmailAndPass.onEmailAndPassEntered(userMail,userPass);
        }
    }
    private void signIn() {

        onGoogleSignListner= new GoogleSignIn(this,this);

        mGoogleApiClient= onGoogleSignListner.BuildingApiClient();
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
                onGoogleSignListner.AuthAccountWithGoogle(account);

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
    public void onAutherizationSuccess() {
        startActivity(new Intent(this, ContactsListActivity.class));
        finish();
    }

    @Override
    public void onAutherizationFailed() {
        Toast.makeText(SignInActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

}