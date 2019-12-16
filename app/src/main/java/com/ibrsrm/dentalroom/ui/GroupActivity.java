package com.ibrsrm.dentalroom.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ibrsrm.dentalroom.R;
import com.ibrsrm.dentalroom.adapters.GlideApp;
import com.ibrsrm.dentalroom.adapters.MessageRVAdapter;
import com.ibrsrm.dentalroom.model.Config.ConfigManager;
import com.ibrsrm.dentalroom.model.Repository.Common.Group;
import com.ibrsrm.dentalroom.model.Repository.Common.Message;
import com.ibrsrm.dentalroom.utils.Constants;
import com.ibrsrm.dentalroom.viewmodels.MessagesViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private EditText mText;
    private Button mSend;
    private Spinner mRating;
    private AlertDialog mDialog;
    private ImageView mXrayImageView;
    private TextView mComment;
    private TextView mMembers;
    private Button mReorderButton;
    private MessageRVAdapter mAdapter;
    private RecyclerView mMessagesRecyclerView;
    private MessagesViewModel mMessagesViewModel;

    private String mGroupID;
    private Group mGroup;
    private boolean mSorted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        /* Get Group ID */
        Intent intent = getIntent();
        mGroupID = intent.getStringExtra(Constants.GROUP_ID_TAG);
        mGroup = ConfigManager.getInstance().getRepositoryManager().getGroup(mGroupID);
        mGroup.setNewMessageStatus(false);

        mToolBar = findViewById(R.id.toolbarGroup);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(mGroup.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /* Initialize views */
        mText = findViewById(R.id.editTextInput);
        mSend = findViewById(R.id.sendButton);
        mMessagesRecyclerView = findViewById(R.id.recyclerViewGroupMessages);
        mRating = findViewById(R.id.spinnerInputComment);
        mXrayImageView = findViewById(R.id.imageViewXRay);
        mComment = findViewById(R.id.textComment);
        mMembers = findViewById(R.id.groupMembers);
        mReorderButton = (Button) findViewById(R.id.toolbarButtonReorder);

        String[] values = getResources().getStringArray(R.array.SpinnerValues);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, values);
        mRating.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMessagesRecyclerView.setLayoutManager(layoutManager);
        mMessagesRecyclerView.setNestedScrollingEnabled(true);

        DividerItemDecoration divider = new DividerItemDecoration(mMessagesRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.divider));
        mMessagesRecyclerView.addItemDecoration(divider);

        /* Initialize view model */
        mMessagesViewModel = ViewModelProviders.of(this).get(MessagesViewModel.class);
        mMessagesViewModel.initialize(mGroupID);

        /* Initialize Adapter */
        mAdapter = new MessageRVAdapter(getApplicationContext(), mMessagesViewModel.getMessagesLiveData().getValue());
        mMessagesRecyclerView.setAdapter(mAdapter);

        if (mGroup.getUrl() != null) {
            /* TODO: find a better way */
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(mGroup.getUrl());
            GlideApp.with(getApplicationContext())
                    .load(ref)
                    .into(mXrayImageView);
        }
        if (mGroup.getComment() != null) {
            mComment.setText(mGroup.getComment());
        }
        if (mGroup.getMembers() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(mMembers.getText().toString());
            sb.append("\n");
            Iterator<Map.Entry<String, String>> iterator = mGroup.getMembers().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                sb.append(entry.getValue());
                sb.append("\n");
            }
            mMembers.setText(sb.toString());
        }

        setActions();
    }

    private void setActions() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                mMessagesViewModel.updateGroupTimestamp(mGroupID);
                finish();
            }
        });

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating = mRating.getSelectedItem().toString();
                String input = mText.getText().toString();
                if (rating.equals("RATE") || input.isEmpty()) {
                    showErrorDialog();
                } else {
                    mMessagesViewModel.sendMessage(mGroupID, input, rating);
                    mText.setText("");
                    mRating.setSelection(0);
                }
            }
        });

        mMessagesViewModel.getMessagesLiveData().observe(this, new Observer<ArrayList<Message>>() {
            @Override
            public void onChanged(ArrayList<Message> messages) {
                mAdapter.notifyDataSetChanged();
            }
        });

        mXrayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupActivity.this, RenderActivity.class);
                i.putExtra(Constants.GROUP_ID_TAG, mGroup.getUid());
                startActivity(i);
            }
        });

        mReorderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSorted == true) {
                    Collections.sort(mGroup.getMessages(), new Comparator<Message>() {
                        @Override
                        public int compare(Message m1, Message m2) {
                            return m1.getTimestamp() < m2.getTimestamp() ? -1 : 0;
                        }
                    });
                    mSorted = false;
                } else {
                    Collections.sort(mGroup.getMessages(), new Comparator<Message>() {
                        @Override
                        public int compare(Message m1, Message m2) {
                            return m1.getRating() > m2.getRating() ? -1 : 0;
                        }
                    });
                    mSorted = true;
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onPause() {
        hideErrorDialog();
        mMessagesViewModel.updateGroupTimestamp(mGroupID);
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
            builder.setTitle("Send Message Failed");
            builder.setMessage("Please add an input and select rating");
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

}
