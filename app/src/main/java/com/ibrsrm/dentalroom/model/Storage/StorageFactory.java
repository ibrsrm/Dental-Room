package com.ibrsrm.dentalroom.model.Storage;

import android.util.Log;

public interface StorageFactory {
    BaseStorage createStorageManager(String type);

    StorageFactory DEFAULT = new StorageFactory() {

        private final static String TAG = "StorageFactory:";

        @Override
        public BaseStorage createStorageManager(String type) {
            switch (type) {
                case "Firebase":
                    return FirebaseStorageManager.getInstance();
                default:
                    Log.e(TAG, "No Type Found");
                    return null;
            }
        }
    };

}
