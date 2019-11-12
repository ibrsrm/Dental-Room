package com.ibrsrm.dentalroom.model.Auth;

import android.util.Log;

public interface AuthenticationFactory {
    BaseAuthentication createAuthenticationManager(String type);

    AuthenticationFactory DEFAULT = new AuthenticationFactory() {

        private final static String TAG = "AuthenticationFactory:";

        @Override
        public BaseAuthentication createAuthenticationManager(String type) {
            switch (type) {
                case "Firebase":
                    return FirebaseAuthentication.getInstance();
                default:
                    Log.e(TAG, "No Type Found");
                    return null;
            }
        }
    };

}
