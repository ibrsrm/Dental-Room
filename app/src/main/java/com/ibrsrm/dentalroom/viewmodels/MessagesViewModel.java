package com.ibrsrm.dentalroom.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ibrsrm.dentalroom.model.Config.ConfigManager;
import com.ibrsrm.dentalroom.model.Repository.Common.Message;
import com.ibrsrm.dentalroom.model.Repository.BaseRepository;

import java.util.ArrayList;

public class MessagesViewModel extends ViewModel {

    private final static String TAG = "MessagesViewModel:";

    private MutableLiveData<ArrayList<Message>> mMessageLiveData;

    private BaseRepository mBaseRepository;

    public void initialize(String groupID) {
        if (mMessageLiveData == null) {
            mMessageLiveData = new MutableLiveData<>();
        }
        if (mBaseRepository == null) {
            mBaseRepository = ConfigManager.getInstance().getRepositoryManager();
        }
        mMessageLiveData.setValue(mBaseRepository.getMessages(groupID));
        mBaseRepository.setMessageListener(groupID, new BaseRepository.OnMessageListChanged() {
            @Override
            public void OnMessageListChanged(String groupID) {
                mMessageLiveData.setValue(mBaseRepository.getMessages(groupID));
            }
        });
    }

    public LiveData<ArrayList<Message>> getMessagesLiveData() {
        return mMessageLiveData;
    }

    public void sendMessage(String groupID, String text, String rating) {
        mBaseRepository.sendMessage(groupID, text, rating);
    }

    public void updateGroupTimestamp(String groupID) {
        mBaseRepository.updateGroupTimestamp(groupID);
    }

}
