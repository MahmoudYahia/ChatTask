package com.project.chattask.callback.signIn;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by mah_y on 8/24/2017.
 */

public interface ApiClientBuilderListener {
    void onApiClientCreated(GoogleApiClient googleApiClient);
}
