package com.ibrsrm.dentalroom.model.Storage;

import android.net.Uri;
import android.os.SystemClock;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.ibrsrm.dentalroom.utils.Constants;

public class FirebaseStorageManager extends BaseStorage {

    private final static String TAG = "FirebaseStorageManager:";
    private final static String ROOT_REF = "images/";
    private String mCurrentUserID;

    private static FirebaseStorageManager mManager;

    private StorageReference mStorageRef;

    private FirebaseStorageManager() {
        mStorageRef = FirebaseStorage.getInstance().getReference().child(ROOT_REF);
    }

    public static FirebaseStorageManager getInstance() {
        if (mManager == null) {
            mManager = new FirebaseStorageManager();
        }
        return mManager;
    }

    @Override
    public void initializeInstance(String userID) {
        mCurrentUserID = userID;
    }

    @Override
    public void terminateInstance() {
        mCurrentUserID = null;
    }

    @Override
    public void handleUploadFile(Uri uri) {
        String name = String.valueOf(SystemClock.elapsedRealtime());
        StorageReference ref = mStorageRef.child(mCurrentUserID).child(name);

        ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String path = taskSnapshot.getStorage().getPath();
                        if (uploadStatusListener != null) {
                            uploadStatusListener.onStatusSuccess(path);
                            uploadStatusListener.onStatusChanged(Constants.UPLOAD_STATUS_SUCCESS);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (uploadStatusListener != null) {
                            uploadStatusListener.onStatusChanged(Constants.UPLOAD_STATUS_FAILED);
                        }
                    }
                });

    }

}
