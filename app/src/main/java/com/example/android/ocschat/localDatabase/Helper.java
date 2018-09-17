package com.example.android.ocschat.localDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.ocschat.model.FriendState;

public class Helper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ocschat.db";
    private static final int DATABASE_VERSION = 1;

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE_USER_STATEMENT = "CREATE TABLE" + Contract.User.TABLE_NAME + " ("
                + Contract.User._ID + Contract.DATA_TEXT + Contract.CONSTRAINT_PRIMARY_KEY + ", "
                + Contract.User.COLUMN_FIRST_NAME + Contract.DATA_TEXT + Contract.CONSTRAINT_NOT_NULL + ", "
                + Contract.User.COLUMN_LAST_NAME + Contract.DATA_TEXT + Contract.CONSTRAINT_NOT_NULL + ", "
                + Contract.User.COLUMN_HAS_IMAGE + Contract.DATA_INTEGER + Contract.CONSTRAINT_DEFAULT + " \'0\'" + ", "  //default = 0: has no image
                + Contract.User.COLUMN_IMAGE_FILE_PATH + Contract.DATA_TEXT + ", "
                + Contract.User.COLUMN_AGE + Contract.DATA_INTEGER + ", "
                + Contract.User.COLUMN_EDUCATION + Contract.DATA_TEXT + ", "
                + Contract.User.COLUMN_EDUCATION_ORGANIZATION + Contract.DATA_TEXT + ", "
                + Contract.User.COLUMN_MAJOR + Contract.DATA_TEXT + ", "
                + Contract.User.COLUMN_WORK + Contract.DATA_TEXT + ", "
                + Contract.User.COLUMN_COMPANY + Contract.DATA_TEXT + ");";

        final String SQL_CREATE_TABLE_FRIEND_STATEMENT = "CREATE TABLE" + Contract.Friend.TABLE_NAME + " ("
                + Contract.Friend.COLUMN_USER_ID + Contract.DATA_TEXT + Contract.CONSTRAINT_NOT_NULL + ", "
                + Contract.Friend.COLUMN_FRIEND_ID + Contract.DATA_TEXT + Contract.CONSTRAINT_NOT_NULL + ", "
                + Contract.Friend.COLUMN_FRIEND_STATE + Contract.DATA_INTEGER + Contract.CONSTRAINT_DEFAULT + " \'" + FriendState.NORMAL.ordinal() + "\');";

        //execute create table statements
        db.execSQL(SQL_CREATE_TABLE_USER_STATEMENT);
        db.execSQL(SQL_CREATE_TABLE_FRIEND_STATEMENT);
        Log.d("Helper", SQL_CREATE_TABLE_USER_STATEMENT);
        Log.d("Helper", SQL_CREATE_TABLE_FRIEND_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Nothing for now
    }
}
