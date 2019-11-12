package com.ibrsrm.dentalroom.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ibrsrm.dentalroom.R;
import com.ibrsrm.dentalroom.adapters.GroupRVAdapter;
import com.ibrsrm.dentalroom.adapters.GroupRVAdapter.ItemClickListener;
import com.ibrsrm.dentalroom.model.Config.ConfigManager;
import com.ibrsrm.dentalroom.model.Repository.Common.Group;
import com.ibrsrm.dentalroom.utils.Constants;
import com.ibrsrm.dentalroom.viewmodels.GroupsViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemClickListener {

    private final static String TAG = "ContactActivity:";

    private Toolbar mToolBar;
    private RecyclerView mRecyclerGroups;
    private GroupRVAdapter mAdapter;
    private GroupsViewModel mGroupsViewModel;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar = findViewById(R.id.toolbarMain);
        setSupportActionBar(mToolBar);
        mFab = findViewById(R.id.fab);
        mRecyclerGroups = findViewById(R.id.recyclerViewGroups);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerGroups.setLayoutManager(layoutManager);
        mRecyclerGroups.setNestedScrollingEnabled(true);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerGroups.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.divider));
        mRecyclerGroups.addItemDecoration(divider);

        /* Initialize view model */
        mGroupsViewModel = ViewModelProviders.of(this).get(GroupsViewModel.class);
        mGroupsViewModel.initialize();

        /* Initialize adapter */
        mAdapter = new GroupRVAdapter(getApplicationContext(), mGroupsViewModel.getGroupsLiveData().getValue());
        mRecyclerGroups.setAdapter(mAdapter);
        mAdapter.setClickListener(this);

        /* Set Actions */
        setActions();
    }

    private void setActions () {
        mGroupsViewModel.getGroupsLiveData().observe(this, new Observer<ArrayList<Group>>() {
            @Override
            public void onChanged(ArrayList<Group> groups) {
                mAdapter.notifyDataSetChanged();
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view, int position) {
        final Group group = mGroupsViewModel.getGroupsLiveData().getValue().get(position);
        Intent i = new Intent(this, GroupActivity.class);
        i.putExtra(Constants.GROUP_ID_TAG, group.getUid());
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_add_contact) {
            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_logout) {
            ConfigManager.getInstance().getAuthenticationManager().handleLogout();
            ConfigManager.getInstance().terminate();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
