package com.ibrsrm.dentalroom.utils;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressWarnings("InlinedApi")
public final class Constants {

    private Constants() {
    }

    /**
     * Register process status.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({REGISTER_STATUS_IDLE, REGISTER_STATUS_SUCCESS, REGISTER_STATUS_FAILED})
    public @interface RegisterStatus {}
    public static final int REGISTER_STATUS_IDLE = 0x00;

    public static final int REGISTER_STATUS_SUCCESS = 0x01;

    public static final int REGISTER_STATUS_FAILED = 0x02;

    /**
     * Login process status.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LOGIN_STATUS_IDLE, LOGIN_STATUS_SUCCESS, LOGIN_STATUS_FAILED})
    public @interface LoginStatus {}
    public static final int LOGIN_STATUS_IDLE = 0x00;

    public static final int LOGIN_STATUS_SUCCESS = 0x01;

    public static final int LOGIN_STATUS_FAILED = 0x02;

    /**
     * Contact List status.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CONTACT_LIST_STATUS_IDLE, CONTACT_LIST_STATUS_ADD_SUCCESS, CONTACT_LIST_STATUS_ADD_FAILED})
    public @interface ContactStatus {}
    public static final int CONTACT_LIST_STATUS_IDLE = 0x00;

    public static final int CONTACT_LIST_STATUS_ADD_SUCCESS = 0x01;

    public static final int CONTACT_LIST_STATUS_ADD_FAILED = 0x03;

    /**
     * Upload status.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({UPLOAD_STATUS_IDLE, UPLOAD_STATUS_SUCCESS, UPLOAD_STATUS_FAILED})
    public @interface UploadStatus {}
    public static final int UPLOAD_STATUS_IDLE = 0x00;

    public static final int UPLOAD_STATUS_SUCCESS = 0x01;

    public static final int UPLOAD_STATUS_FAILED = 0x02;


    public final static String GROUP_ID_TAG = "GROUP_ID";

}