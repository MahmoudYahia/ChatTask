package com.project.chattask.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
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
import com.project.chattask.R;
import com.project.chattask.callBackInterface.onCheckAuthorizationListener;

/**
 * Created by mah_y on 8/22/2017.
 */

public class SignInAuthorization implements GoogleApiClient.OnConnectionFailedListener {
    Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    onCheckAuthorizationListener mOnCheckAuthorizationListner;

    public SignInAuthorization(Context context, onCheckAuthorizationListener onCheckAuthorizationListener) {
        this.mContext = context;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mOnCheckAuthorizationListner = onCheckAuthorizationListener;
    }

    public GoogleApiClient buildApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        return mGoogleApiClient;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(mContext, mContext.getString(R.string.errorFitchingData), Toast.LENGTH_LONG).show();
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
                                mOnCheckAuthorizationListner.autherizationSuccess();
                            } else {
                                Toast.makeText(mContext, "Not Verified Emial",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                           mOnCheckAuthorizationListner.autherizationFailed();
                        }
                    }
                });
    }

    public void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithCredential", task.getException());
                            mOnCheckAuthorizationListner.autherizationFailed();
                        } else {
                            addUserToContacts(mFirebaseAuth.getCurrentUser());
                            mOnCheckAuthorizationListner.autherizationSuccess();
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


    }


}
