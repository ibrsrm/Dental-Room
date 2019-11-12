package com.ibrsrm.dentalroom.model.Config;

import com.ibrsrm.dentalroom.model.Auth.AuthenticationFactory;
import com.ibrsrm.dentalroom.model.Auth.BaseAuthentication;
import com.ibrsrm.dentalroom.model.Database.BaseDatabase;
import com.ibrsrm.dentalroom.model.Database.DatabaseFactory;
import com.ibrsrm.dentalroom.model.Repository.BaseRepository;
import com.ibrsrm.dentalroom.model.Repository.Repository;
import com.ibrsrm.dentalroom.model.Storage.BaseStorage;
import com.ibrsrm.dentalroom.model.Storage.StorageFactory;

public class ConfigManager {

    private static ConfigManager mConfigManager;

    private boolean initialized = false;

    private BaseAuthentication mAuthenticationManager;
    private BaseDatabase mDatabaseManager;
    private BaseStorage mStorageManager;
    private BaseRepository mRepositoryManager;

    private ConfigManager() {
        mAuthenticationManager = AuthenticationFactory.DEFAULT.createAuthenticationManager("Firebase");
    }

    public static ConfigManager getInstance() {
        if (mConfigManager == null) {
            mConfigManager = new ConfigManager();
        }
        return mConfigManager;
    }

    public void initialize(String userID, String userEmail) {
        if (initialized == false) {
            mDatabaseManager = DatabaseFactory.DEFAULT.createDatabaseManager("Firebase");
            mStorageManager = StorageFactory.DEFAULT.createStorageManager("Firebase");
            mRepositoryManager = Repository.getInstance();
            mDatabaseManager.initializeInstance(userID, userEmail);
            mRepositoryManager.initializeInstance(userID, userEmail);
            mStorageManager.initializeInstance(userID);
            initialized = true;
        }
    }

    public void terminate() {
        mDatabaseManager.terminateInstance();
        mRepositoryManager.terminateInstance();
        mStorageManager.terminateInstance();
        initialized = false;
    }

    public BaseAuthentication getAuthenticationManager () {
        return mAuthenticationManager;
    }

    public BaseDatabase getDatabaseManager () {
        return mDatabaseManager;
    }

    public BaseStorage getStorageManager () {
        return mStorageManager;
    }

    public BaseRepository getRepositoryManager() {
        return mRepositoryManager;
    }

}
