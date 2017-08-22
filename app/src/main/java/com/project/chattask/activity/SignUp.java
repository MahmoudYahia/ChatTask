package com.project.chattask.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.chattask.R;

public class SignUp extends AppCompatActivity {

    EditText RegUserEmail, RegUserPassword, RegUserConfirmPass;
    String UserEmail, UserPass, UserConfirmPass;
    Button RegisterButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        RegUserEmail = (EditText) findViewById(R.id.reg_email);
        RegUserPassword = (EditText) findViewById(R.id.reg_password);
        RegUserConfirmPass = (EditText) findViewById(R.id.reg_confirm_pass);
        RegisterButton = (Button) findViewById(R.id.btnRegister);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount();
            }
        });
        mAuth = FirebaseAuth.getInstance();

    }

    public void addAccount() {
        UserEmail = RegUserEmail.getText().toString();
        UserPass = RegUserPassword.getText().toString();
        UserConfirmPass = RegUserConfirmPass.getText().toString();

        if (TextUtils.isEmpty(UserEmail)
                || TextUtils.isEmpty(UserPass) || TextUtils.isEmpty(UserConfirmPass)) {

            Toast.makeText(this, "Empty Fields", Toast.LENGTH_LONG).show();
            // make Snackbar >> Empty Fields
        } else {

            if (UserPass.length() > 5) {
                if (UserPass.equals(UserConfirmPass)) {
                    // matched pass
                    if (isOnline(this)) {
                        // call register method and check  iff success
                        mAuth.createUserWithEmailAndPassword(UserEmail, UserPass)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            user.sendEmailVerification();
                                            startActivity(new Intent(SignUp.this, SignIn.class));
                                            SignUp.this.finish();

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(SignUp.this, "please enter valid email or password ",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                    } else {
                        // no internet connection
                        Toast.makeText(SignUp.this, "no internet connection",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    RegUserConfirmPass.setText("");
                }
            } else {
                RegUserPassword.setError("minimum size 6 ");
            }

        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
