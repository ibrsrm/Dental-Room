package com.ibrsrm.dentalroom.model.Repository.Common;

import android.util.Log;

import androidx.annotation.NonNull;

public class Contact {

    private final static String TAG = "Contact:";

    private String nick;
    private String mail;
    private String uid;

    public Contact () {}

    public Contact(@NonNull String _nick, @NonNull String _mail, @NonNull String _uid) {
        this.nick = _nick;
        this.mail = _mail;
        this.uid = _uid;
    }

    public String getNick () {
        return this.nick;
    }

    public String getMail () {
        return this.mail;
    }

    public String getUID () {
        return this.uid;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CONTACT: [ID:" + uid + "]");
        builder.append("[NICK:" + nick + "]");
        builder.append("[MAIL:" + mail + "]");
        return builder.toString();
    }
}
