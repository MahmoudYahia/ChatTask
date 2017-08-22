package com.project.chattask.activities;

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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import com.project.chattask.models.Contact;
import com.project.chattask.R;

public class SignIn extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 2;
    private SignInButton mSignInButton;


    Button loginBtn, RegisterBtn;
    EditText InputEmial, InputPass;
    ImageView logoImg;
    ProgressBar SignInProgresBar;


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

        buildApiClient(); // build Api Client

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();


    }

    public void buildApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
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
    }

    public void onLogInBtnPressed() {
        String userMail = InputEmial.getText().toString();
        String userPass = InputPass.getText().toString();

        if (TextUtils.isEmpty(userMail) || TextUtils.isEmpty(userPass)) {
            Toast.makeText(getBaseContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();

        }
        else {
            checkEmialPassword(userMail, userPass);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            SignInProgresBar.setVisibility(View.GONE); // hide

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // not Success
                Toast.makeText(this, "Authentication Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void checkEmialPassword(String Email, String Pass) {
        /// SignInProgresBar.setVisibility(View.VISIBLE);
        mFirebaseAuth.signInWithEmailAndPassword(Email, Pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            final FirebaseUser user = mFirebaseAuth.getCurrentUser();

                            if (user.isEmailVerified()) {
                                addAuthUserToContacts(user);
                                startActivity(new Intent(SignIn.this, ContactsList.class));
                                finish();
                            } else {
                                Toast.makeText(SignIn.this, "Not Verified Emial",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            addUserToContacts(mFirebaseAuth.getCurrentUser());
                            startActivity(new Intent(SignIn.this, ContactsList.class));
                            finish();

                        }
                    }
                });
    }

    public void addUserToContacts(FirebaseUser account) {

        //public Contact(String uid, String tokenId, String uname, String uemail, String imgURl)
        Contact contact = new Contact(
                account.getUid()
                , account.getDisplayName()
                , account.getEmail()
                , account.getPhotoUrl().toString());


        mDatabaseRef.child("Contacts")
                .child(account.getUid())
                .setValue(contact);

    }

    public void addAuthUserToContacts(FirebaseUser account) {
        //public Contact(String uid, String tokenId, String uname, String uemail, String imgURl)
        String email = account.getEmail();
        String FormattedName = email.substring(0, email.indexOf('@'));

        Contact contact = new Contact(
                account.getUid()
                , FormattedName
                , account.getEmail()
                , " empty ");


        mDatabaseRef.child("Contacts")
                .child(account.getUid())
                .setValue(contact);

        Toast.makeText(SignIn.this, "Added To Contacts",
                Toast.LENGTH_SHORT).show();
    }

    private void signIn() {
        SignInProgresBar.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


}