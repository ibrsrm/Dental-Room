package com.ibrsrm.dentalroom.model.Storage;

import android.net.Uri;

import com.ibrsrm.dentalroom.utils.Constants;

public abstract class BaseStorage {

    protected OnUploadStatusChanged uploadStatusListener;

    public interface OnUploadStatusChanged {
        void onStatusChanged(@Constants.UploadStatus int status);

        void onStatusSuccess(String downloadURI);
    }

    public void setUploadStatusListener(OnUploadStatusChanged listener) {
        uploadStatusListener = listener;
    }

    public abstract void initializeInstance(String userID);

    public abstract void terminateInstance();

    public abstract void handleUploadFile(Uri uri);

}
