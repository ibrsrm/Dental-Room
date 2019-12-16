package com.ibrsrm.dentalroom.model.Database;

import androidx.annotation.NonNull;

import com.ibrsrm.dentalroom.model.Repository.Common.Contact;
import com.ibrsrm.dentalroom.model.Repository.Common.Group;
import com.ibrsrm.dentalroom.model.Repository.Common.Message;

import java.util.HashSet;

public abstract class BaseDatabase {

    protected OnGroupCreated groupListener;
    protected OnMessageReceived messageListener;
    protected OnContactStatusChanged contactListener;

    public interface OnGroupCreated {
        void onNewGroup(Group group);
    }

    public interface OnMessageReceived {
        void onNewMessage(String groupID, Message message);
    }

    public interface OnContactStatusChanged {
        void onNewContact(Contact contact);

        void onAddContactSuccess();

        void onAddContactFailed();
    }

    /* Common methods */
    public void setGroupListener(OnGroupCreated listener) {
        groupListener = listener;
    }

    public void setMessageListener(OnMessageReceived listener) {
        messageListener = listener;
    }

    public void setContactListener(OnContactStatusChanged listener) {
        contactListener = listener;
    }

    /* Abstract methods */
    public abstract void initializeInstance(String currentUserID, String currentUserEmail);

    public abstract void terminateInstance();

    public abstract void handleAddContact(@NonNull final String email, @NonNull String nick);

    public abstract void handleCreateGroup(String groupName, String groupComment, HashSet<Contact> groupMembers, String imageUri);

    public abstract void handleSendMessage(String groupID, String text, String rating);

    public abstract void updateGroupTimestamp(String groupID);

}
