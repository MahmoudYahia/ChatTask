package com.project.chattask.model;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.project.chattask.callBackInterface.signOut.OnCompleteSignOut;
import com.project.chattask.callBackInterface.signOut.OnSignOut;

/**
 * Created by mah_y on 8/22/2017.
 */

public class SignOut implements OnSignOut {
    Context mContext;
    OnCompleteSignOut onCompleteSignOut;

    public SignOut(Context context, OnCompleteSignOut onCompleteSignOut) {
        this.mContext=context;
        this.onCompleteSignOut=onCompleteSignOut;
    }

    @Override
    public void userSignOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setMessage("SignOut")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                onCompleteSignOut.signOutCompleted();
                            }
                        });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
