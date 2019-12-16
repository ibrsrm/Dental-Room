package com.ibrsrm.dentalroom.model.Repository.Common;

import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Group {

    private final static String TAG = "Group:";

    private String uid;
    private String name;
    private String comment;
    private String url;

    private long timestamp;
    private long lastMessageTimestamp;

    private Map<String, String> members;
    private ArrayList<Message> messages;

    private boolean mNewMessage;

    public Group() {
        if (messages == null) {
            messages = new ArrayList<>();
        }
    }

    public Group(String _uid, String _name, String _comment, String _url) {
        this.uid = _uid;
        this.name = _name;
        this.comment = _comment;
        this.url = _url;
        members = new HashMap<>();
        messages = new ArrayList<>();
    }

    public String getUid () {
        return this.uid;
    }

    public String getName () {
        return this.name;
    }

    public String getComment () {
        return this.comment;
    }

    public String getUrl () {
        return this.url;
    }

    public Map<String, String> getMembers() {
        return this.members;
    }

    @Exclude
    public void addMember (String uid, String mail) {
        members.put(uid, mail);
    }

    @Exclude
    public boolean getNewMessageStatus () {
        if (lastMessageTimestamp >= timestamp) {
            mNewMessage = true;
        } else {
            mNewMessage = false;
        }
        return mNewMessage;
    }

    @Exclude
    public boolean setNewMessageStatus (boolean newMessage) {
        return mNewMessage = newMessage;
    }

    @Exclude
    public ArrayList<Message> getMessages() {
        return messages;
    }

    @Exclude
    public void addMessage(Message message) {
        messages.add(message);
        lastMessageTimestamp = message.getTimestamp();
    }

    @Exclude
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        if (this.timestamp > this.lastMessageTimestamp) {
            mNewMessage = false;
        }
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("comment", comment);
        result.put("url", url);
        result.put("members", members);
        return result;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GROUP: [ID:" + uid + "]");
        builder.append("[NAME:" + name + "]");
        builder.append("[COMMENT:" + comment + "]");
        return builder.toString();
    }

}
