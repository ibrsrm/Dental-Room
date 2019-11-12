package com.ibrsrm.dentalroom.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ibrsrm.dentalroom.model.Config.ConfigManager;
import com.ibrsrm.dentalroom.model.Repository.Common.Group;
import com.ibrsrm.dentalroom.model.Repository.BaseRepository;

import java.util.ArrayList;

public class GroupsViewModel extends ViewModel {

    private final static String TAG = "GroupsViewModel:";

    private MutableLiveData<ArrayList<Group>> mGroupsLiveData;

    private BaseRepository mBaseRepository;

    public void initialize() {
        if (mGroupsLiveData == null) {
            mGroupsLiveData = new MutableLiveData<>();
        }
        if (mBaseRepository == null) {
            mBaseRepository = ConfigManager.getInstance().getRepositoryManager();
        }
        mGroupsLiveData.setValue(mBaseRepository.getGroups());
        mBaseRepository.setGroupListener(new BaseRepository.OnGroupsListChanged() {
            @Override
            public void onGroupsListChanged() {
                mGroupsLiveData.setValue(mBaseRepository.getGroups());
            }
        });
    }

    public LiveData<ArrayList<Group>> getGroupsLiveData() {
        return mGroupsLiveData;
    }

}
