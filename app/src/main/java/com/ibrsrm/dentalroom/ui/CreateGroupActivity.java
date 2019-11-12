package com.ibrsrm.dentalroom.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibrsrm.dentalroom.R;
import com.ibrsrm.dentalroom.adapters.ContactRVAdapter;
import com.ibrsrm.dentalroom.model.Config.ConfigManager;
import com.ibrsrm.dentalroom.model.Repository.Common.Contact;
import com.ibrsrm.dentalroom.utils.Constants;
import com.ibrsrm.dentalroom.viewmodels.StorageViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;

public class CreateGroupActivity extends AppCompatActivity {

    private final static String TAG = "CreateGroupActivity:";

    private Uri mFileUri;
    private String mUploadUri;
    private final int ACTION_CODE = 1000;

    private Toolbar mToolBar;
    private Button mCreateButton;
    private Button mAttachXRay;
    private EditText mTextGroupName;
    private EditText mTextGroupComment;
    private TextView mAddContact;
    private AlertDialog mDialog;
    private ContactRVAdapter mAdapter;
    private RecyclerView mContactsRecyclerView;
    private StorageViewModel mViewModelStorage;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cretate_group);

        mToolBar = findViewById(R.id.toolbarCreateGroup);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /* Initialize Views */
        mContactsRecyclerView = findViewById(R.id.recyclerViewContacts);
        mCreateButton = findViewById(R.id.createGroupButton);
        mTextGroupName = findViewById(R.id.editTextGroupName);
        mTextGroupComment = findViewById(R.id.editTextGroupComment);
        mAttachXRay = findViewById(R.id.attachButton);
        mAddContact = (TextView) findViewById(R.id.addContactLink);
        mProgressBar = findViewById(R.id.progressBar);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        mContactsRecyclerView.setLayoutManager(recyclerLayoutManager);
        mContactsRecyclerView.setNestedScrollingEnabled(true);

        DividerItemDecoration divider = new DividerItemDecoration(mContactsRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.divider));
        mContactsRecyclerView.addItemDecoration(divider);

        /* Initialize view models */
        mViewModelStorage = ViewModelProviders.of(this).get(StorageViewModel.class);
        mViewModelStorage.initialize();

        /* Initialize Adapter (Contacts cannot be changed at this point so no need to create view model) */
        mAdapter = new ContactRVAdapter(getApplicationContext(), ConfigManager.getInstance().getRepositoryManager().getContacts(), true);
        mContactsRecyclerView.setAdapter(mAdapter);

        setActions();
    }

    private void setActions () {
        mViewModelStorage.getStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer status) {
                switch (status) {
                    case Constants.UPLOAD_STATUS_FAILED:
                        showErrorDialog("Internal Error occured, try again later");
                        hideProgressBar();
                        break;
                    case Constants.UPLOAD_STATUS_SUCCESS:
                        mUploadUri = mViewModelStorage.getDownloadUri();
                        hideProgressBar();
                        createGroup();
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

        mAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGroupActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashSet<Contact> set = mAdapter.getContactSet();
                String groupName = mTextGroupName.getText().toString();
                String groupComment = mTextGroupComment.getText().toString();
                if (checkCreateConditions(set, groupName, groupComment) == false) {
                    showErrorDialog("Please Enter Group Name, Comment and Contacts");
                } else {
                    showProgressBar();
                    mViewModelStorage.uploadFile(mFileUri);
                }
            }
        });

        mAttachXRay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, ACTION_CODE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == ACTION_CODE && resultCode == Activity.RESULT_OK) {
            mFileUri = data.getData();
            mAttachXRay.setText("X-RAY Attached...");
        }
    }

    private void showErrorDialog(String error) {
        if (mDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Create Group Failed");
            builder.setMessage(error);
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

    private boolean checkCreateConditions (HashSet<Contact> set, String groupName, String groupComment) {
        if (set.isEmpty() || groupName.isEmpty() || groupComment.isEmpty() || mFileUri == null) {
            return false;
        }
        return true;
    }

    private void createGroup() {
        HashSet<Contact> set = mAdapter.getContactSet();
        String groupName = mTextGroupName.getText().toString();
        String groupComment = mTextGroupComment.getText().toString();
        if (checkCreateConditions(set, groupName, groupComment) == false) {
            showErrorDialog("Please Enter Group Name, Comment and Contacts");
        } else {
            ConfigManager.getInstance().getRepositoryManager().createGroup(groupName, groupComment, set, mUploadUri);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

}
