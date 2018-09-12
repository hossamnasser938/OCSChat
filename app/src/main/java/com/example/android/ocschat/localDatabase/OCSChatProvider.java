package com.example.android.ocschat.localDatabase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.android.ocschat.R;
import com.example.android.ocschat.model.FriendState;

public class OCSChatProvider extends ContentProvider {

    private static final int USERS = 100;
    private static final int FRIENDS = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_USER, USERS);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_FRIEND, FRIENDS);
    }

    Helper mDbHelper;


    @Override
    public boolean onCreate() {
        mDbHelper = new Helper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        final Cursor cursor;
        switch (match){
            case USERS :
                cursor = database.query(Contract.User.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case  FRIENDS :
                cursor = database.query(Contract.Friend.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default :
                    throw new IllegalArgumentException("Unknown Uri:" + uri);
        }

        //TODO: notification
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case USERS :
                return Contract.User.CONTENT_LIST_TYPE;
            case FRIENDS :
                return Contract.Friend.CONTENT_LIST_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case USERS :
                return insertUser(values);
            case FRIENDS :
                return insertFriend(uri, values);
            default :
                throw new IllegalArgumentException("Unknown Uri:" + uri);
        }
    }

    private Uri insertUser(ContentValues values){
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final long rowId;

        //Validate data before inserting
        validateUser(values);

        rowId = database.insert(Contract.User.TABLE_NAME,
                null,
                values);

        if(rowId != -1){
            Uri insertedRowUri = ContentUris.withAppendedId(Contract.User.CONTENT_URI, rowId);
            //TODO: notifychange
            return insertedRowUri;
        }
        return null;
    }

    private Uri insertFriend(Uri uri, ContentValues values){
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final long rowId;

        //validate data before inserting
        validateFriend(values);

        rowId = database.insert(Contract.Friend.TABLE_NAME,
                null,
                values);

        if(rowId != -1){
            Uri insertedRowUri = ContentUris.withAppendedId(Contract.Friend.CONTENT_URI, rowId);
            //TODO: notifychange
            return insertedRowUri;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case USERS :
                return deleteUser(uri, selection, selectionArgs);
            case FRIENDS :
                return deleteFriend(uri, selection, selectionArgs);
            default :
                throw new IllegalArgumentException("Unknown Uri:" + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case USERS :
                return updateUser(uri, values, selection, selectionArgs);
            case FRIENDS :
                return updateFriend(uri, values, selection, selectionArgs);
            default :
                throw new IllegalArgumentException("Unknown Uri:" + uri);
        }
    }

    private int updateUser(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        //Validate data before updating
        validateUser(values);

        //TODO: notifyChange
        return database.update(Contract.User.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    private int updateFriend(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Validate data before updating
        validateFriend(values);

        //TODO: notifyChange
        return database.update(Contract.Friend.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    private int deleteUser(Uri uri, String selection, String[] selectionArgs){
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //TODO: notifyChange
        return database.delete(Contract.User.TABLE_NAME,
                selection,
                selectionArgs);
    }

    private int deleteFriend(Uri uri, String selection, String[] selectionArgs){
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //TODO: notifyChange
        return database.delete(Contract.Friend.TABLE_NAME,
                selection,
                selectionArgs);
    }

    private void validateUser(ContentValues values){
        //Validate user id
        String id = values.getAsString(Contract.User._ID);
        if(id == null || TextUtils.isEmpty(id)){
            throw new IllegalArgumentException(getContext().getString(R.string.not_valid_id_property));
        }
        //Validate user first name
        String firstName = values.getAsString(Contract.User.COLUMN_FIRST_NAME);
        if(firstName == null || TextUtils.isEmpty(firstName)){
            throw new IllegalArgumentException(getContext().getString(R.string.enter_first_name));
        }
        //Validate user last name
        String lastName = values.getAsString(Contract.User.COLUMN_LAST_NAME);
        if(lastName == null || TextUtils.isEmpty(lastName)){
            throw new IllegalArgumentException(getContext().getString(R.string.enter_last_name));
        }
        //validate friendsCount
        Integer friendsCount = values.getAsInteger(Contract.User.COLUMN_FRIENDS_COUNT);
        if(friendsCount == null || friendsCount < 0){
            throw new IllegalArgumentException(getContext().getString(R.string.not_valid_friends_count_property));
        }
        //Validate user age
        Integer age = values.getAsInteger(Contract.User.COLUMN_AGE);
        if(age != null){
            if(age <= 0){
                throw new IllegalArgumentException(getContext().getString(R.string.enter_valid_age));
            }
        }
        //Validate user hasImage property
        Integer hasImage = values.getAsInteger(Contract.User.COLUMN_HAS_IMAGE);
        if(hasImage != null){
            if(!(hasImage == 0 || hasImage == 1)){
                throw new IllegalArgumentException(getContext().getString(R.string.not_valid_hasImage_property));
            }
        }
    }

    private void validateFriend(ContentValues values){
        //validate user id
        String userId = values.getAsString(Contract.Friend.COLUMN_USER_ID);
        if(userId == null || TextUtils.isEmpty(userId)){
            throw new IllegalArgumentException(getContext().getString(R.string.not_valid_user_id_property));
        }
        //validate friend id
        String friendId = values.getAsString(Contract.Friend.COLUMN_FRIEND_ID);
        if(friendId == null || TextUtils.isEmpty(friendId)){
            throw new IllegalArgumentException(getContext().getString(R.string.not_valid_friend_id_property));
        }
        //validate friend state
        Integer friendState = values.getAsInteger(Contract.Friend.COLUMN_FRIEND_STATE);
        if(friendState == null || !(friendState == FriendState.NORMAL.ordinal() || friendState == FriendState.BEST.ordinal() || friendState== FriendState.MUTED.ordinal() || friendState== FriendState.BLOCKED.ordinal())){
            throw new IllegalArgumentException(getContext().getString(R.string.not_valid_friend_state_property));
        }
    }
}
