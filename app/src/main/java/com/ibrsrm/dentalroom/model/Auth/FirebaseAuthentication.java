package com.ibrsrm.dentalroom.model.Auth;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ibrsrm.dentalroom.utils.Constants;

public class FirebaseAuthentication extends BaseAuthentication {

    private final static String TAG = "FirebaseAuthentication:";

    private static FirebaseAuthentication mManager;

    private FirebaseAuth mAuth;

    private String mCurrentUserID;
    private String mCurrentUserEmail;

    private FirebaseAuthentication() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuthentication getInstance() {
        if (mManager == null) {
            mManager = new FirebaseAuthentication();
        }
        return mManager;
    }

    @Override
    public String getCurrentUserID () {
        return mCurrentUserID;
    }

    @Override
    public String getCurrentUserEMail () {
        return mCurrentUserEmail;
    }

    @Override
    public boolean checkAlreadyLogin() {
        if (mAuth.getCurrentUser() != null) {
            mCurrentUserID = mAuth.getCurrentUser().getUid();
            mCurrentUserEmail =  mAuth.getCurrentUser().getEmail();
            return true;
        }
        return false;
    }

    @Override
    public void handleLogin(@NonNull String email, @NonNull final String password) {
        Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        int status;
                        if (task.isSuccessful()) {
                            status = Constants.LOGIN_STATUS_SUCCESS;
                            mCurrentUserID = mAuth.getCurrentUser().getUid();
                            mCurrentUserEmail =  mAuth.getCurrentUser().getEmail();
                        } else {
                            status = Constants.LOGIN_STATUS_FAILED;
                        }
                        if (loginStatusListener != null) {
                            loginStatusListener.onStatusChanged(status);
                        }
                    }
                });
    }

    @Override
    public void handleRegister(@NonNull final String email, @NonNull final String password) {
        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        int status;
                        if (task.isSuccessful()) {
                            status = Constants.REGISTER_STATUS_SUCCESS;
                            mCurrentUserID = mAuth.getCurrentUser().getUid();
                            mCurrentUserEmail =  mAuth.getCurrentUser().getEmail();
                        } else {
                            status = Constants.REGISTER_STATUS_FAILED;
                        }
                        if (registerStatusListener != null) {
                            registerStatusListener.onStatusChanged(status);
                        }
                    }
                });
    }

    @Override
    public void handleLogout() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
        mCurrentUserID = null;
        mCurrentUserEmail = null;
    }

}
