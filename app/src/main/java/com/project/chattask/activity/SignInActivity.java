package com.project.chattask.activity;

import android.content.Intent;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.project.chattask.callback.signIn.ApiClientBuilderListener;
import com.project.chattask.callback.signIn.CheckAuthorizationListener;
import com.project.chattask.callback.signIn.EmailAndPassAuthenticator;
import com.project.chattask.R;
import com.project.chattask.callback.signIn.GoogleLoginAuthenticator;
import com.project.chattask.model.GoogleSignIn;
import com.project.chattask.model.UserAuthentecation;

public class SignInActivity extends AppCompatActivity implements
        View.OnClickListener{

    int RC_SIGN_IN = 2;
    SignInButton mSignInButton;
    Button loginBtn, RegisterBtn;
    EditText InputEmial, InputPass;
    ImageView logoImg;
  //  ProgressBar SignInProgresBar;

    GoogleLoginAuthenticator googleLoginAuthenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        logoImg = (ImageView) findViewById(R.id.signin_logo_img);
     //   SignInProgresBar = (ProgressBar) findViewById(R.id.sign_in_progress);

        InputEmial = (EditText) findViewById(R.id.txt_email);
        InputPass = (EditText) findViewById(R.id.txt_pass);

        loginBtn = (Button) findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);

        RegisterBtn = (Button) findViewById(R.id.btn_sign_up);
        RegisterBtn.setOnClickListener(this);


        mSignInButton = (SignInButton) findViewById(R.id.sign_in_google);
        mSignInButton.setOnClickListener(this);

       googleLoginAuthenticator = new GoogleSignIn();

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sign_in_google:
                signInWithGoogle();
                break;

            case R.id.btn_login:
                loginWithemailAndPass();
                break;
            case R.id.btn_sign_up:
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                break;

        }
    }

    public void loginWithemailAndPass() {
        String userMail = InputEmial.getText().toString();
        String userPass = InputPass.getText().toString();

        if (TextUtils.isEmpty(userMail) || TextUtils.isEmpty(userPass)) {
            Toast.makeText(getBaseContext(), R.string.empty_fields, Toast.LENGTH_LONG).show();

        } else {

            EmailAndPassAuthenticator emailAndPassAuthenticator= new UserAuthentecation();
            emailAndPassAuthenticator.authenticateEmailAndPass(userMail, userPass, new CheckAuthorizationListener() {
                @Override
                public void onAutherizationSuccess() {
                    startActivity(new Intent(SignInActivity.this, ContactsListActivity.class));
                    finish();
                }

                @Override
                public void onAutherizationFailed() {
                    Toast.makeText(SignInActivity.this, "authentication Failed", Toast.LENGTH_LONG).show();

                }
            });
            /*
            userAuthentecation.authenticateEmailAndPass(userMail, userPass, new CheckAuthorizationListener() {
                @Override
                public void onAutherizationSuccess() {
                    startActivity(new Intent(SignInActivity.this, ContactsListActivity.class));
                    finish();
                }

                @Override
                public void onAutherizationFailed() {
                    Toast.makeText(SignInActivity.this, "authentication Failed", Toast.LENGTH_LONG).show();
                }
            });
*/
        }
    }

    private void signInWithGoogle() {
        googleLoginAuthenticator.BuildApiClient(this, new ApiClientBuilderListener() {
            @Override
            public void onApiClientCreated(GoogleApiClient googleApiClient) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

            googleLoginAuthenticator.authGoogleAccount(data, new CheckAuthorizationListener() {
                    @Override
                    public void onAutherizationSuccess() {
                        startActivity(new Intent(SignInActivity.this, ContactsListActivity.class));
                        finish();
                    }

                    @Override
                    public void onAutherizationFailed() {
                        Toast.makeText(SignInActivity.this, "authentication Failed", Toast.LENGTH_LONG).show();
                    }
                });

        }
    }


}