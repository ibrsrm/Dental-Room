package com.ibrsrm.dentalroom.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibrsrm.dentalroom.R;
import com.ibrsrm.dentalroom.model.Config.ConfigManager;
import com.ibrsrm.dentalroom.utils.Constants;
import com.ibrsrm.dentalroom.viewmodels.AuthenticationViewModel;

public class LoginActivity extends AppCompatActivity {

    private final static String TAG = "LoginActivity:";

    private EditText mEmail;
    private EditText mPass;
    private TextView mNeedAccount;
    private Button mBtnLogin;
    private ProgressBar mProgressBar;
    private AlertDialog mDialog;
    private AuthenticationViewModel mAuthViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* Initialize view model, check already login? */
        mAuthViewModel = ViewModelProviders.of(this).get(AuthenticationViewModel.class);
        mAuthViewModel.initializeLogin();
        if (mAuthViewModel.checkAlreadyLogin()) {
            ConfigManager.getInstance().initialize(mAuthViewModel.getUserID(), mAuthViewModel.getUserEmail());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Initialize views */
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        mEmail = (EditText) findViewById(R.id.editTextLoginMail);
        mPass = (EditText) findViewById(R.id.editTextLoginPassword);
        mNeedAccount = (TextView) findViewById(R.id.needAccountLink);
        mBtnLogin = (Button) findViewById(R.id.loginButton);

        /* Set Actions */
        setActions();
    }

    private void setActions() {
        mAuthViewModel.getLoginStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer status) {
                switch (status) {
                    case Constants.LOGIN_STATUS_IDLE:
                        break;
                    case Constants.LOGIN_STATUS_SUCCESS:
                        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                        ConfigManager.getInstance().initialize(mAuthViewModel.getUserID(), mAuthViewModel.getUserEmail());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    case Constants.LOGIN_STATUS_FAILED:
                        showErrorDialog();
                        break;
                }
                hideProgressBar();
            }
        });

        mNeedAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPass.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    showProgressBar();
                    mAuthViewModel.loginUser(email, password);
                    return;
                }
                Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause() {
        hideErrorDialog();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        hideErrorDialog();
        super.onDestroy();
    }

    private void showErrorDialog() {
        if (mDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Login Failed");
            builder.setMessage("Please check your credentials");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            mDialog = builder.create();
        }
        mDialog.show();
    }

    private void hideErrorDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

}
