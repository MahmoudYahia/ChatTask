package com.project.chattask.callback.signIn;

/**
 * Created by mah_y on 8/22/2017.
 */

public interface EmailAndPassAuthenticator {
    void authenticateEmailAndPass(String email, String pass, CheckAuthorizationListener authorizationListener);
}
