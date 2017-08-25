package com.project.chattask.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.chattask.callback.signIn.CheckAuthorizationListener;
import com.project.chattask.callback.signIn.EmailAndPassAuthenticator;

/**
 * Created by mah_y on 8/24/2017.
 */

public class EmailAndPassLogin implements EmailAndPassAuthenticator {

    @Override
    public void authenticateEmailAndPass(String email, String pass, final CheckAuthorizationListener authorizationListener) {
        final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
                        if (task.isSuccessful()) {
                            if (user.isEmailVerified()) {
                                authorizationListener.onAutherizationSuccess();
                            } else {
                                authorizationListener.onAutherizationFailed();
                            }

                        } else {
                            authorizationListener.onAutherizationFailed();
                        }
                    }
                });
    }
}
