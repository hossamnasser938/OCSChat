package com.example.android.ocschat.localDatabase;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String DATA_TEXT = " TEXT";
    public static final String DATA_INTEGER = " INTEGER";
    public static final String DATA_BLOB = " BLOB";

    public static final String CONSTRAINT_PRIMARY_KEY = " PRIMARY KEY";
    public static final String CONSTRAINT_NOT_NULL = " NOT NULL";
    public static final String CONSTRAINT_DEFAULT = " DEFAULT";

    public static final String CONTENT_AUTHORITY = "com.example.android.ocschat";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_USER = "User";
    public static final String PATH_FRIEND = "Friend";

    public static final class User implements BaseColumns {

        public static final String TABLE_NAME = " User";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USER);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of users.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single user.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;


        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_FIRST_NAME = "First_Name";
        public static final String COLUMN_LAST_NAME = "Last_Name";
        public static final String COLUMN_HAS_IMAGE = "Has_Image";
        public static final String COLUMN_AGE = "Age";
        public static final String COLUMN_EDUCATION = "Education";
        public static final String COLUMN_EDUCATION_ORGANIZATION = "Education_Organization";
        public static final String COLUMN_MAJOR = "major";
        public static final String COLUMN_WORK = "work";
        public static final String COLUMN_COMPANY = "company";

    }

    public static final class Friend implements BaseColumns {

        public static final String TABLE_NAME = " Friend";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FRIEND);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of friends.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FRIEND;


        public static final String COLUMN_USER_ID = "User_Id";
        public static final String COLUMN_FRIEND_ID = "Friend_Id";
        public static final String COLUMN_FRIEND_STATE = "Friend_State";

    }

}
