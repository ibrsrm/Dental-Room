package com.ibrsrm.dentalroom.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ibrsrm.dentalroom.model.Config.ConfigManager;
import com.ibrsrm.dentalroom.model.Repository.Common.Contact;
import com.ibrsrm.dentalroom.model.Repository.BaseRepository;
import com.ibrsrm.dentalroom.utils.Constants;

import java.util.List;

public class ContactViewModel extends ViewModel {

    private final static String TAG = "ContactViewModel:";

    private MutableLiveData<Integer> mAddContactStatus;

    private @Constants.ContactStatus int mStatus;

    private BaseRepository mBaseRepository;

    public void initialize() {
        if (mAddContactStatus == null) {
            mAddContactStatus = new MutableLiveData<>();
        }
        if (mBaseRepository == null) {
            mBaseRepository = ConfigManager.getInstance().getRepositoryManager();
        }
        mStatus = Constants.CONTACT_LIST_STATUS_IDLE;

        mBaseRepository.setContactListener(new BaseRepository.OnContactListChanged() {
            @Override
            public void onNewContact() {
            }

            @Override
            public void onAddContactSuccess() {
                mStatus = Constants.CONTACT_LIST_STATUS_ADD_SUCCESS;
                mAddContactStatus.setValue(mStatus);
            }

            @Override
            public void onAddContactFailed() {
                mStatus = Constants.CONTACT_LIST_STATUS_ADD_FAILED;
                mAddContactStatus.setValue(mStatus);
            }
        });
    }

    public LiveData<Integer> getStatusLiveData() {
        return mAddContactStatus;
    }

    public List<Contact> getContacts() {
        return mBaseRepository.getContacts();
    }

    public void addContact(String email, String nick) {
        mStatus = Constants.CONTACT_LIST_STATUS_IDLE;
        mBaseRepository.addContact(email, nick);
    }

}
