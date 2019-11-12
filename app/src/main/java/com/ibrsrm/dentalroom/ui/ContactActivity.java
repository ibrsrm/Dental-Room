package com.ibrsrm.dentalroom.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ibrsrm.dentalroom.R;
import com.ibrsrm.dentalroom.adapters.ContactRVAdapter;
import com.ibrsrm.dentalroom.utils.Constants;
import com.ibrsrm.dentalroom.viewmodels.ContactViewModel;

public class ContactActivity extends AppCompatActivity {

    private final static String TAG = "ContactActivity:";

    private Toolbar mToolBar;
    private EditText mNick;
    private EditText mEmail;
    private TextView mCreateGroupLink;
    private Button mBtnAdd;
    private ProgressBar mProgressBar;
    private AlertDialog mDialog;
    private RecyclerView mRecyclerContacts;
    private ContactRVAdapter mAdapter;
    private ContactViewModel mContactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        /* Toolbar Modifications */
        mToolBar = findViewById(R.id.toolbarAddContact);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /* Initialize Views */
        mEmail = (EditText) findViewById(R.id.editTextMail);
        mNick = (EditText) findViewById(R.id.editTextNick);
        mBtnAdd = (Button) findViewById(R.id.addContactButton);
        mCreateGroupLink = (TextView) findViewById(R.id.createGroupLink);
        mRecyclerContacts = findViewById(R.id.recyclerViewContacts);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        mRecyclerContacts.setLayoutManager(recyclerLayoutManager);
        mRecyclerContacts.setNestedScrollingEnabled(true);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerContacts.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.divider));
        mRecyclerContacts.addItemDecoration(divider);

        /* Initialize view model */
        mContactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        mContactViewModel.initialize();

        /* Initialize adapter */
        mAdapter = new ContactRVAdapter(getApplicationContext(), mContactViewModel.getContacts(), false);
        mRecyclerContacts.setAdapter(mAdapter);

        /* Set Actions */
        setActions();
    }

    private void setActions () {
        mContactViewModel.getStatusLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer status) {
                switch (status) {
                    case Constants.CONTACT_LIST_STATUS_IDLE:
                        break;
                    case Constants.CONTACT_LIST_STATUS_ADD_SUCCESS:
                        Toast.makeText(getApplicationContext(), "Add Contact Success", Toast.LENGTH_SHORT).show();
                        mAdapter.notifyDataSetChanged();
                        resetAfterSuccess();
                        hideProgressBar();
                        break;
                    case Constants.CONTACT_LIST_STATUS_ADD_FAILED:
                        hideProgressBar();
                        showErrorDialog();
                        break;
                }
            }
        });

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        mCreateGroupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String nick = mNick.getText().toString();
                if (!email.isEmpty() && !nick.isEmpty()) {
                    Log.e("mail:" + email, " nick:" + nick);
                    showProgressBar();
                    mContactViewModel.addContact(email, nick);
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

    private void resetAfterSuccess() {
        mEmail.setText("");
        mNick.setText("");
    }

    private void showErrorDialog() {
        if (mDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Contact Failed");
            builder.setMessage("Please try again later");
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
