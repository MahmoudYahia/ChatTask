package com.project.chattask.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.chattask.callBackInterface.signUp.OnAccountCreatedListner;
import com.project.chattask.callBackInterface.signUp.OnCreateNewAccount;

/**
 * Created by mah_y on 8/22/2017.
 */

public class CreatNewAccount implements OnCreateNewAccount{
    private FirebaseAuth mAuth;
    Context mContext;
    OnAccountCreatedListner mOnAccountCreatedListner;
    public CreatNewAccount(Context context, OnAccountCreatedListner onAccountCreatedListner) {
        this.mContext=context;
        mAuth = FirebaseAuth.getInstance();
        this.mOnAccountCreatedListner=onAccountCreatedListner;

    }

    @Override
    public void onCreateAccount(String mail, String pass) {
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            mOnAccountCreatedListner.onCreateAccountSuccess();

                        } else {
                            // If sign in fails, display a message to the user.
                            mOnAccountCreatedListner.onCreateAccountFailed();

                        }

                    }
                });
    }
}
