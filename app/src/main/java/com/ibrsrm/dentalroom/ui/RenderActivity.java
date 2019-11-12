package com.ibrsrm.dentalroom.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.ibrsrm.dentalroom.R;
import com.ibrsrm.dentalroom.adapters.GlideApp;
import com.ibrsrm.dentalroom.model.Config.ConfigManager;
import com.ibrsrm.dentalroom.model.Repository.Common.Group;
import com.ibrsrm.dentalroom.utils.Constants;

public class RenderActivity extends AppCompatActivity {

    private String groupID;
    private Group mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render);

        Toolbar toolbar = findViewById(R.id.toolbarRender);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* Get Group ID */
        Intent intent = getIntent();
        groupID = intent.getStringExtra(Constants.GROUP_ID_TAG);
        mGroup = ConfigManager.getInstance().getRepositoryManager().getGroup(groupID);

        /* Render Image Full screen */
        ImageView xRayImage = findViewById(R.id.imageView);
        StorageReference ref = FirebaseStorage.getInstance().getReference().child(mGroup.getUrl());
        GlideApp.with(getApplicationContext())
                .load(ref)
                .into(xRayImage);
    }

}
