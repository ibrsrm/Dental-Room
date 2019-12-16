package com.ibrsrm.dentalroom.model.Repository;

import androidx.annotation.NonNull;

import com.ibrsrm.dentalroom.model.Repository.Common.Contact;
import com.ibrsrm.dentalroom.model.Repository.Common.Group;
import com.ibrsrm.dentalroom.model.Repository.Common.Message;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class BaseRepository {

    protected String mGroupID;

    protected OnGroupsListChanged groupListener;
    protected OnMessageListChanged messageListener;
    protected OnContactListChanged contactListener;

    public interface OnGroupsListChanged {
        void onGroupsListChanged();
    }

    public interface OnMessageListChanged {
        void OnMessageListChanged(String groupID);
    }

    public interface OnContactListChanged {
        void onNewContact();

        void onAddContactSuccess();

        void onAddContactFailed();
    }

    /* Common methods */
    public void setGroupListener(OnGroupsListChanged listener) {
        groupListener = listener;
    }

    public void setMessageListener(String groupID, OnMessageListChanged listener) {
        mGroupID = groupID;
        messageListener = listener;
    }

    public void setContactListener(OnContactListChanged listener) {
        contactListener = listener;
    }

    /* Abstract functions */
    public abstract void initializeInstance(String currentUserID, String email);

    public abstract void terminateInstance();

    /* Hash-map functions */
    public abstract Contact getContact(String email);

    public abstract Group getGroup(String groupID);

    /* List functions */
    public abstract ArrayList<Contact> getContacts();

    public abstract ArrayList<Group> getGroups();

    public abstract ArrayList<Message> getMessages(String groupID);

    /* Commands */
    public abstract void addContact(@NonNull String email, @NonNull String nick);

    public abstract void sendMessage(@NonNull String groupID, @NonNull String text, @NonNull String rating);

    public abstract void createGroup(String groupName, String groupComment, HashSet<Contact> groupMembers, String imageUri);

    public abstract void updateGroupTimestamp(@NonNull String groupID);

}
