package com.project.chattask.callBackInterface.signIn;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by mah_y on 8/22/2017.
 */

public interface OnGoogleSignListner {

    public GoogleApiClient BuildingApiClient();
    public void   AuthAccountWithGoogle(GoogleSignInAccount account);

}
