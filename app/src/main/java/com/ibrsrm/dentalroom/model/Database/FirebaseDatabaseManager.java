package com.ibrsrm.dentalroom.model.Database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ibrsrm.dentalroom.model.Repository.Common.Contact;
import com.ibrsrm.dentalroom.model.Repository.Common.Group;
import com.ibrsrm.dentalroom.model.Repository.Common.Message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class FirebaseDatabaseManager extends BaseDatabase {

    private final static String TAG = "FirebaseDatabaseM:";
    private final static String GROUP_TAG = "groups";
    private final static String USERS_TAG = "users";
    private final static String MESSAGES_TAG = "messages";
    private final static String CONTACTS_TAG = "contacts";

    private static FirebaseDatabaseManager mManager;

    private String mUID;
    private String mMail;

    private DatabaseReference mRootRef;
    private DatabaseReference mUserRef;
    private DatabaseReference mGroupRef;
    private DatabaseReference mMessageRef;
    private DatabaseReference mCurrentUserRef;

    private ChildEventListener mGroupListener;
    private ChildEventListener mContactListener;
    private HashMap<String, ChildEventListener> mGroupMessageListeners;

    private FirebaseDatabaseManager() {
        mRootRef = com.google.firebase.database.FirebaseDatabase.getInstance().getReference();
        mGroupRef = mRootRef.child(GROUP_TAG);
        mUserRef = mRootRef.child(USERS_TAG);
        mMessageRef = mRootRef.child(MESSAGES_TAG);
    }

    public static FirebaseDatabaseManager getInstance() {
        if (mManager == null) {
            mManager = new FirebaseDatabaseManager();
        }
        return mManager;
    }

    @Override
    public void initializeInstance(String currentUserID, String currentUserEmail) {
        mUID = currentUserID;
        mMail = currentUserEmail;
        mCurrentUserRef = mUserRef.child(mUID);
        mCurrentUserRef.child("email").setValue(currentUserEmail);
        mGroupMessageListeners = new HashMap<>();
        addFirebaseGroupListener();
        addContactListener();
    }

    @Override
    public void terminateInstance() {
        mCurrentUserRef.child(CONTACTS_TAG).removeEventListener(mContactListener);
        mCurrentUserRef.child(GROUP_TAG).removeEventListener(mGroupListener);
        Iterator<Map.Entry<String, ChildEventListener>> iterator = mGroupMessageListeners.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ChildEventListener> entry = iterator.next();
            mMessageRef.child(entry.getKey()).removeEventListener(entry.getValue());
        }
        mUID = null;
        mMail = null;
        mCurrentUserRef = null;
    }

    /* Add firebase group listener, initialize groups and add message listeners */
    private void addFirebaseGroupListener () {
        mGroupListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() == true) {
                    initializeFirebaseGroup(dataSnapshot.getKey(), (Long) dataSnapshot.getValue());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mCurrentUserRef.child(GROUP_TAG).addChildEventListener(mGroupListener);
    }

    private void initializeFirebaseGroup (String groupID, final long timestamp) {
        Query query = mGroupRef.child(groupID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() == true) {
                    Group group = dataSnapshot.getValue(Group.class);
                    if (groupListener != null) {
                        group.setTimestamp(timestamp);
                        groupListener.onNewGroup(group);
                    }
                    addFirebaseGroupMessageListener(group.getUid());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /* TODO: limitToLast(), endAt(), orderby() */
    private void addFirebaseGroupMessageListener (final String groupID) {
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() == true) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (messageListener != null) {
                        messageListener.onNewMessage(groupID, message);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mMessageRef.child(groupID).addChildEventListener(listener);
        mGroupMessageListeners.put(groupID, listener);
    }

    /* Handle Contact and add contact status listeners */
    private void addContactListener() {
        mContactListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() == true) {
                    Contact contact = dataSnapshot.getValue(Contact.class);
                    if (contactListener != null) {
                        contactListener.onNewContact(contact);
                        contactListener.onAddContactSuccess();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mCurrentUserRef.child(CONTACTS_TAG).orderByChild("nick").addChildEventListener(mContactListener);
    }

    private void addVerifiedUser(@NonNull final String userID, @NonNull final String email, @NonNull final String nick) {
        Map<String, Object> newContact = new HashMap<>();
        newContact.put(userID, new Contact(nick, email, userID));
        mCurrentUserRef.child(CONTACTS_TAG).updateChildren(newContact, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    if (contactListener != null) {
                        contactListener.onAddContactFailed();
                    } else {
                        contactListener.onAddContactSuccess();
                    }
                }
            }
        });
    }

    @Override
    public void handleAddContact(@NonNull final String email, @NonNull final String nick) {
        Query query = mUserRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() == true) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        addVerifiedUser(child.getKey(), email, nick);
                        break;
                    }
                } else {
                    if (contactListener != null) {
                        contactListener.onAddContactFailed();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void handleCreateGroup(String groupName, String groupComment, HashSet<Contact> groupMembers, String imageUri) {
        String id = mGroupRef.push().getKey();
        Group group = new Group(id, groupName, groupComment, imageUri);
        group.addMember(mUID, mMail);
        for (Contact c : groupMembers) {
            group.addMember(c.getUID(), c.getMail());
        }
        Map<String, Object> postValues = group.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(id, postValues);
        mGroupRef.updateChildren(childUpdates);
    }

    @Override
    public void handleSendMessage(String groupID, String text, String rating) {
        String messageID = mMessageRef.child(groupID).push().getKey();
        Message message = new Message(messageID, text, mMail, Integer.parseInt(rating), System.currentTimeMillis());
        Map<String, Object> postValues = message.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(messageID, postValues);
        mMessageRef.child(groupID).updateChildren(childUpdates);
    }

    @Override
    public void updateGroupTimestamp(String groupID) {
        mCurrentUserRef.child(GROUP_TAG).child(groupID).setValue(System.currentTimeMillis());
    }

}
