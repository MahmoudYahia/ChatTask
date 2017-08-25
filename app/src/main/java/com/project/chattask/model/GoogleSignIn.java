package com.project.chattask.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.project.chattask.R;
import com.project.chattask.callback.signIn.ApiClientBuilderListener;
import com.project.chattask.callback.signIn.CheckAuthorizationListener;
import com.project.chattask.callback.signIn.GoogleLoginAuthenticator;

/**
 * Created by mah_y on 8/22/2017.
 */

public class GoogleSignIn implements
        GoogleApiClient.OnConnectionFailedListener,GoogleLoginAuthenticator {
    public GoogleSignIn() {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Toast.makeText(mContext,mContext.getString(R.string.errorFitchingData),Toast.LENGTH_LONG).show();
    }

    @Override
    public void authGoogleAccount(Intent data, final CheckAuthorizationListener authorizationListener) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        GoogleSignInAccount account= result.getSignInAccount();

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            authorizationListener.onAutherizationFailed();
                        }
                        else {
                            authorizationListener.onAutherizationSuccess();
                        }
                    }
                });
    }

    @Override
    public void BuildApiClient(Context context, ApiClientBuilderListener clientBuilderListener) {
        GoogleApiClient googleApiClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient= new GoogleApiClient.Builder(context)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        clientBuilderListener.onApiClientCreated(googleApiClient);
    }
}
