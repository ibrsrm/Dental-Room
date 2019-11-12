package com.ibrsrm.dentalroom.model.Auth;

import androidx.annotation.NonNull;

import com.ibrsrm.dentalroom.utils.Constants;

public abstract class BaseAuthentication {

    protected OnRegisterStatusChanged registerStatusListener;
    protected OnLoginStatusChanged loginStatusListener;

    /* listeners */
    public interface OnRegisterStatusChanged {
        void onStatusChanged(@Constants.RegisterStatus int status);
    }

    public interface OnLoginStatusChanged {
        void onStatusChanged(@Constants.LoginStatus int status);
    }

    public void setRegisterListener(OnRegisterStatusChanged listener) {
        registerStatusListener = listener;
    }

    public void setLoginListener(OnLoginStatusChanged listener) {
        loginStatusListener = listener;
    }

    /* Abstract Methods */
    public abstract String getCurrentUserID();

    public abstract String getCurrentUserEMail();

    public abstract boolean checkAlreadyLogin ();

    public abstract void handleLogin(@NonNull String email, @NonNull String password);

    public abstract void handleRegister(@NonNull String email, @NonNull String password);

    public abstract void handleLogout();

}
