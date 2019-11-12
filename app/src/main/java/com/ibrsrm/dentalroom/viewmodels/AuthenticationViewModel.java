package com.ibrsrm.dentalroom.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ibrsrm.dentalroom.model.Auth.BaseAuthentication;
import com.ibrsrm.dentalroom.model.Config.ConfigManager;
import com.ibrsrm.dentalroom.utils.Constants;

public class AuthenticationViewModel extends ViewModel {

    private final static String TAG = "AuthenticationViewModel:";

    private BaseAuthentication mAuthentication;

    private MutableLiveData<Integer> mRegisterLiveData;
    private MutableLiveData<Integer> mLoginLiveData;

    private @Constants.RegisterStatus int mRegisterStatus;
    private @Constants.LoginStatus int mLoginStatus;

    public void initializeRegister() {
        if (mRegisterLiveData == null) {
            mRegisterLiveData = new MutableLiveData<Integer>();
        }
        if (mAuthentication == null) {
            mAuthentication = ConfigManager.getInstance().getAuthenticationManager();
        }
        mRegisterStatus = Constants.REGISTER_STATUS_IDLE;
        mAuthentication.setRegisterListener(new BaseAuthentication.OnRegisterStatusChanged() {
            @Override
            public void onStatusChanged(int status) {
                mRegisterStatus = status;
                mRegisterLiveData.setValue(mRegisterStatus);
            }
        });
    }

    public void initializeLogin() {
        if (mLoginLiveData == null) {
            mLoginLiveData = new MutableLiveData<Integer>();
        }
        if (mAuthentication == null) {
            mAuthentication = ConfigManager.getInstance().getAuthenticationManager();
        }
        mLoginStatus = Constants.LOGIN_STATUS_IDLE;
        mAuthentication.setLoginListener(new BaseAuthentication.OnLoginStatusChanged() {
            @Override
            public void onStatusChanged(int status) {
                mLoginStatus = status;
                mLoginLiveData.setValue(mLoginStatus);
            }
        });
    }

    public LiveData<Integer> getRegisterStatus() {
        return mRegisterLiveData;
    }

    public LiveData<Integer> getLoginStatus() {
        return mLoginLiveData;
    }

    public boolean checkAlreadyLogin() {
        return  mAuthentication.checkAlreadyLogin();
    }

    public void loginUser(String email, String password) {
        mAuthentication.handleLogin(email, password);
    }

    public void registerUser(String email, String password) {
        mAuthentication.handleRegister(email, password);
    }

    public String getUserID() {
        return mAuthentication.getCurrentUserID();
    }

    public String getUserEmail() {
        return mAuthentication.getCurrentUserEMail();
    }

}
