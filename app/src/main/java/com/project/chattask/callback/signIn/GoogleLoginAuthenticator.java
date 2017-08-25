package com.project.chattask.callback.signIn;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by mah_y on 8/22/2017.
 */

public interface GoogleLoginAuthenticator {
    void authGoogleAccount(Intent data, final CheckAuthorizationListener authorizationListener);
    void BuildApiClient (Context context,ApiClientBuilderListener apiClientBuilderListener);
}
