package com.project.chattask.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.chattask.R;
import com.project.chattask.callback.signUp.OnAccountCreatedListner;
import com.project.chattask.callback.signUp.OnCreateNewAccount;
import com.project.chattask.model.CreatNewAccount;

public class SignUpActivity extends AppCompatActivity implements OnAccountCreatedListner {

    EditText RegUserEmail, RegUserPassword, RegUserConfirmPass;
    String UserEmail, UserPass, UserConfirmPass;
    Button RegisterButton;

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
                        OnCreateNewAccount onCreateNewAccount= new CreatNewAccount(this,this);
                        onCreateNewAccount.onCreateAccount(UserEmail,UserPass);

                    } else {
                        // no internet connection
                        Toast.makeText(SignUpActivity.this, "no internet connection",
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


    @Override
    public void onCreateAccountSuccess() {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }

    @Override
    public void onCreateAccountFailed() {
        Toast.makeText(SignUpActivity.this, R.string.errorRegisterAccount,
                Toast.LENGTH_SHORT).show();
    }
}
