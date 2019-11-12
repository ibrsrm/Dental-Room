package com.ibrsrm.dentalroom.model.Database;

import android.util.Log;

public interface DatabaseFactory {
    BaseDatabase createDatabaseManager(String type);

    DatabaseFactory DEFAULT = new DatabaseFactory() {

        private final static String TAG = "DatabaseFactory:";

        @Override
        public BaseDatabase createDatabaseManager(String type) {
            switch (type) {
                case "Firebase":
                    return FirebaseDatabaseManager.getInstance();
                default:
                    Log.e(TAG, "No Type Found");
                    return null;
            }
        }
    };

}
