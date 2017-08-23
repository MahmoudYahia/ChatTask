package com.project.chattask.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.project.chattask.callBackInterface.signIn.OnCheckEmailAndPass;
import com.project.chattask.callBackInterface.signIn.onCheckAuthorizationListener;

/**
 * Created by mah_y on 8/22/2017.
 */

public class EmailAndPassSignIn extends SignInAuthorization implements OnCheckEmailAndPass {

    public EmailAndPassSignIn(Context context, onCheckAuthorizationListener onCheckAuthorizationListener) {
        super(context, onCheckAuthorizationListener);
    }

    public void checkEmialPassword(String Email, String Pass) {

        mFirebaseAuth.signInWithEmailAndPassword(Email, Pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if (user.isEmailVerified()) {
                                addAuthUserToContacts(user);
                                mOnCheckAuthorizationListner.onAutherizationSuccess();
                            } else {
                                Toast.makeText(mContext, "Not Verified Emial",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            mOnCheckAuthorizationListner.onAutherizationFailed();
                        }
                    }
                });
    }

    @Override
    public void onEmailAndPassEntered(String email, String pass) {
        checkEmialPassword(email, pass);
    }
}
