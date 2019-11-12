package com.ibrsrm.dentalroom.model.Repository.Common;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private final static String TAG = "Message:";

    private String uid;
    private String text;
    private String sender;
    private int rating;
    private long timestamp;

    public Message () {}

    public Message(@NonNull String _uid, @NonNull String _text, @NonNull String _sender, int _rating, long _timestamp) {
        this.uid = _uid;
        this.text = _text;
        this.sender = _sender;
        this.rating = _rating;
        this.timestamp = _timestamp;
    }

    public String getUid() {
        return this.uid;
    }

    public String getText() {
        return this.text;
    }

    public String getSender() {
        return this.sender;
    }

    public int getRating() {
        return this.rating;
    }

    public long getTimestamp() {return this.timestamp;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("text", text);
        result.put("sender", sender);
        result.put("rating", rating);
        result.put("timestamp", timestamp);
        return result;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MESSAGE: [ID:" + uid + "]");
        builder.append("[TEXT:" + text + "]");
        builder.append("[SENDER:" + sender + "]");
        builder.append("[REATING:" + rating + "]");
        builder.append("[TIMESTAMP:" + timestamp + "]");
        return builder.toString();
    }

}
