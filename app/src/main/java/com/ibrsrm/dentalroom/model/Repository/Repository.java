package com.ibrsrm.dentalroom.model.Repository;

import androidx.annotation.NonNull;

import com.ibrsrm.dentalroom.model.Config.ConfigManager;
import com.ibrsrm.dentalroom.model.Database.BaseDatabase;
import com.ibrsrm.dentalroom.model.Repository.Common.Contact;
import com.ibrsrm.dentalroom.model.Repository.Common.ContactCollection;
import com.ibrsrm.dentalroom.model.Repository.Common.Group;
import com.ibrsrm.dentalroom.model.Repository.Common.GroupCollection;
import com.ibrsrm.dentalroom.model.Repository.Common.Message;

import java.util.ArrayList;
import java.util.HashSet;

public class Repository extends BaseRepository {

    private final static String TAG = "Repository:";

    private String mCurrentUserID;
    private String mCurrentUserEmail;

    private static Repository mRepository;
    private BaseDatabase mDatabaseManager;

    private GroupCollection mGroups;
    private ContactCollection mContacts;

    private Repository() {
        mContacts = new ContactCollection();
        mGroups = new GroupCollection();
        mDatabaseManager = ConfigManager.getInstance().getDatabaseManager();
    }

    public static Repository getInstance() {
        if (mRepository == null) {
            mRepository = new Repository();
        }
        return mRepository;
    }

    @Override
    public void initializeInstance(String currentUserID, String email) {
        mCurrentUserID = currentUserID;
        mCurrentUserEmail = mCurrentUserEmail;
        setListeners();
    }

    @Override
    public void terminateInstance() {
        mContacts.clear();
        mGroups.clear();
        mCurrentUserID = null;
        mCurrentUserEmail = null;
    }

    @Override
    public Group getGroup(String groupID) {
        return mGroups.getMap().get(groupID);
    }

    @Override
    public Contact getContact(String email) {
        return mContacts.getMap().get(email);
    }

    private void setListeners() {
        mDatabaseManager.setContactListener(new BaseDatabase.OnContactStatusChanged() {
            @Override
            public void onNewContact(Contact contact) {
                mContacts.addItem(contact);
                if (contactListener != null) {
                    contactListener.onNewContact();
                }
            }

            @Override
            public void onAddContactSuccess() {
                if (contactListener != null) {
                    contactListener.onAddContactSuccess();
                }
            }

            @Override
            public void onAddContactFailed() {
                if (contactListener != null) {
                    contactListener.onAddContactFailed();
                }
            }
        });

        mDatabaseManager.setGroupListener(new BaseDatabase.OnGroupCreated() {
            @Override
            public void onNewGroup(Group group) {
                mGroups.addItem(group);
                if (groupListener != null) {
                    groupListener.onGroupsListChanged();
                }
            }
        });

        mDatabaseManager.setMessageListener(new BaseDatabase.OnMessageReceived() {
            @Override
            public void onNewMessage(String groupID, Message message) {
                mGroups.getMap().get(groupID).addMessage(message);
                mGroups.reorder(groupID, 0);
                if ((messageListener != null) && (mGroupID.equals(groupID))) {
                    messageListener.OnMessageListChanged(mGroupID);
                }
                if (groupListener != null) {
                    groupListener.onGroupsListChanged();
                }
            }
        });
    }

    @Override
    public ArrayList<Contact> getContacts() {
        return mContacts.getList();
    }

    @Override
    public ArrayList<Group> getGroups() {
        return mGroups.getList();
    }

    @Override
    public ArrayList<Message> getMessages(String groupID) {
        return mGroups.getMap().get(groupID).getMessages();
    }

    @Override
    public void addContact(@NonNull String email, @NonNull String nick) {
        mDatabaseManager.handleAddContact(email, nick);
    }

    @Override
    public void sendMessage(@NonNull String groupID, @NonNull String text, @NonNull String rating) {
        mDatabaseManager.handleSendMessage(groupID, text, rating);
    }

    @Override
    public void createGroup(String groupName, String groupComment, HashSet<Contact> groupMembers, String imageUri) {
        mDatabaseManager.handleCreateGroup(groupName, groupComment, groupMembers, imageUri);
    }

    @Override
    public void updateGroupTimestamp(String groupID) {
        mDatabaseManager.updateGroupTimestamp(groupID);
    }

}
