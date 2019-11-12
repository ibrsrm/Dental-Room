package com.ibrsrm.dentalroom.viewmodels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ibrsrm.dentalroom.model.Config.ConfigManager;
import com.ibrsrm.dentalroom.model.Storage.BaseStorage;
import com.ibrsrm.dentalroom.utils.Constants;

public class StorageViewModel extends ViewModel {

    private final static String TAG = "StorageViewModel:";

    private BaseStorage mStorage;

    private String mDownloadUri;
    private @Constants.UploadStatus int mStatus;
    private MutableLiveData<Integer> mStatusLiveData;

    public void initialize() {
        if (mStatusLiveData == null) {
            mStatusLiveData = new MutableLiveData<Integer>();
        }
        if (mStorage == null) {
            mStorage = ConfigManager.getInstance().getStorageManager();
        }
        mStatus = Constants.UPLOAD_STATUS_IDLE;
        mStorage.setUploadStatusListener(new BaseStorage.OnUploadStatusChanged() {
            @Override
            public void onStatusChanged(int status) {
                mStatus = status;
                mStatusLiveData.setValue(mStatus);
            }

            @Override
            public void onStatusSuccess(String downloadUri) {
                mDownloadUri = downloadUri;
            }
        });
    }

    public LiveData<Integer> getStatus() {
        return mStatusLiveData;
    }

    public void uploadFile(Uri uri) {
        mStorage.handleUploadFile(uri);
    }

    public String getDownloadUri () {
        return mDownloadUri;
    }

}
