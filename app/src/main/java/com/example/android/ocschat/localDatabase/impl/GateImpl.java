package com.example.android.ocschat.localDatabase.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.android.ocschat.localDatabase.Contract;
import com.example.android.ocschat.localDatabase.Gate;
import com.example.android.ocschat.model.Friend;
import com.example.android.ocschat.model.User;
import com.example.android.ocschat.util.Constants;
import com.example.android.ocschat.util.OCSChatThrowable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.reactivestreams.Publisher;

import java.util.ArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

class GateImpl implements Gate {

    private final String TAG = "GateImpl";

    private Context context;

    public GateImpl(Context context){
        this.context = context;
    }

    /**
     * query the database for a specific user
     * @param userId
     * @return
     */
    @Override
    public Single<User> getUser(String userId) {
        String[] projection = new String[] { Contract.User.COLUMN_FIRST_NAME,
                Contract.User.COLUMN_LAST_NAME, Contract.User.COLUMN_AGE,
                Contract.User.COLUMN_HAS_IMAGE, Contract.User.COLUMN_IMAGE,
                Contract.User.COLUMN_EDUCATION, Contract.User.COLUMN_EDUCATION_ORGANIZATION,
                Contract.User.COLUMN_MAJOR, Contract.User.COLUMN_WORK,
                Contract.User.COLUMN_COMPANY };

        String selection = Contract.User._ID + "=?";
        String[] selectionArgs = new String[]{ userId };

        final Cursor cursor = context.getContentResolver()
                .query(Contract.User.CONTENT_URI, projection, selection, selectionArgs, null);

        if(cursor == null || cursor.getCount() != 1){
            return Single.error(new OCSChatThrowable(Constants.ERROR_FROM_DATABASE));
        }

        cursor.moveToNext();

        //get columns indices
        final int firstNameIndex = cursor.getColumnIndex(Contract.User.COLUMN_FIRST_NAME);
        final int lastNameIndex = cursor.getColumnIndex(Contract.User.COLUMN_LAST_NAME);
        final int ageIndex = cursor.getColumnIndex(Contract.User.COLUMN_AGE);
        final int hasImageIndex = cursor.getColumnIndex(Contract.User.COLUMN_HAS_IMAGE);
        final int imageIndex = cursor.getColumnIndex(Contract.User.COLUMN_IMAGE);
        final int educationIndex = cursor.getColumnIndex(Contract.User.COLUMN_EDUCATION);
        final int educationOrgIndex = cursor.getColumnIndex(Contract.User.COLUMN_EDUCATION_ORGANIZATION);
        final int majorIndex = cursor.getColumnIndex(Contract.User.COLUMN_MAJOR);
        final int workIndex = cursor.getColumnIndex(Contract.User.COLUMN_WORK);
        final int companyIndex = cursor.getColumnIndex(Contract.User.COLUMN_COMPANY);

        //get properties values from cursor
        String firstName = cursor.getString(firstNameIndex);
        String lastName = cursor.getString(lastNameIndex);
        Integer age = cursor.getInt(ageIndex);
        Integer hasImage = cursor.getInt(hasImageIndex);
        if(hasImage == 1){  //User has image
            //TODO: handle user image
        }
        String education = cursor.getString(educationIndex);
        String educationOrg = cursor.getString(educationOrgIndex);
        String major = cursor.getString(majorIndex);
        String work = cursor.getString(workIndex);
        String company = cursor.getString(companyIndex);

        Log.d(TAG, "getUser : " + firstName);

        cursor.close();

        //construct User object
        final User user = new User(userId, firstName, lastName);
        user.setAge(age);
        user.setEducation(education);
        user.setEducationOrganization(educationOrg);
        user.setMajor(major);
        user.setWork(work);
        user.setCompany(company);

        return Single.just(user);
    }

    /**
     * get friends of a user with a given id
     * @param userId
     * @return
     */
    @Override
    public Flowable<User> getUserFriends(String userId) {
        final ArrayList<String> friendsIds = getUserFriendsIds(userId);

        if (friendsIds == null) {
            Log.d(TAG, "error from getUserFriends");
            return Flowable.error(new OCSChatThrowable(Constants.ERROR_FROM_DATABASE));
        }
        Log.d(TAG, "friendsSize = " + friendsIds.size());

        return Flowable.create(new FlowableOnSubscribe<User>() {
            @Override
            public void subscribe(final FlowableEmitter<User> emitter) {
                for(String friendId : friendsIds){
                    Log.d(TAG, friendId);
                    Disposable disposable = getUser(friendId).subscribe(new Consumer<User>() {
                        @Override
                        public void accept(User user) {
                            Log.d(TAG, "fetched user : " + user.getFirstName());
                            emitter.onNext(user);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            Log.d(TAG, "emits throwable = " + throwable.getMessage());
                            emitter.onError(throwable);
                        }
                    });
                }
                Log.d(TAG, "send onComplete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }


    /**
     * checks whether users with given ids are friends or not
     * @param friendId
     * @return
     */
    @Override
    public Single<Boolean> isFriend(String friendId) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String[] projection = new String[] { Contract.Friend.COLUMN_USER_ID,
                Contract.Friend.COLUMN_FRIEND_ID };

        String selection = Contract.Friend.COLUMN_USER_ID + "=?" + " AND "
                + Contract.Friend.COLUMN_FRIEND_ID + "=?";
        String[] selectionArgs = new String[]{ currentUserId, friendId };

        final Cursor cursor = context.getContentResolver()
                .query(Contract.Friend.CONTENT_URI,
                        projection,
                        selection,
                        selectionArgs,
                        null);

        if(cursor == null || cursor.getCount() > 1){
            if(cursor == null)
                Log.d(TAG, "error from isFriend : null coursor");
            else
                Log.d(TAG, "error from isFriend : cursor.getCount() > 1");
            return Single.error(new OCSChatThrowable(Constants.ERROR_FROM_DATABASE));
        }

        final int count = cursor.getCount();
        cursor.close();

        if(count == 1)
            return Single.just(true);

        return Single.just(false);
    }

    /**
     * insert a user in the database
     * @param user
     * @return
     */
    @Override
    public Completable insertUser(User user) {
        Log.d(TAG, "insert user : " + user.getFirstName());
        final ContentValues values = userToContentValues(user);

        if(context.getContentResolver().insert(Contract.User.CONTENT_URI, values) != null)
            return Completable.complete();  //inserted successfully

        return Completable.error(new OCSChatThrowable(Constants.ERROR_FROM_DATABASE));  //something went wrong
    }

    /**
     * add friend in the database
     * @param friend
     * @return
     */
    @Override
    public Completable addFriend(final User friend) {
        Log.d(TAG, "add friend : " + friend.getFirstName());
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final ContentValues friendValues = friendToContentValues(currentUserId, new Friend(friend.getId()));

        //add friend as user in users table
        //and then add friend in friends table
        return insertUser(friend)
                .andThen(new CompletableSource() {
                    @Override
                    public void subscribe(CompletableObserver cs) {
                        if(context.getContentResolver().insert(Contract.Friend.CONTENT_URI, friendValues) != null){
                            Log.d(TAG, "inserted friend " + friend.getFirstName() + " in database");
                            cs.onComplete();  //inserted successfully
                        }
                        else{
                            Log.d(TAG, "error occurred");
                            cs.onError(new OCSChatThrowable(Constants.ERROR_FROM_DATABASE));  //something went wrong
                        }
                    }
                });
    }

    @Override
    public void clearDatabase() {
        //delete all users
        context.getContentResolver().delete(Contract.User.CONTENT_URI,
                null,
                null);

        //delete all friends
        context.getContentResolver().delete(Contract.Friend.CONTENT_URI,
                null,
                null);
    }

    /**
     * update a specific user with the same id in the database
     * @param user
     * @return
     */
    @Override
    public Completable updateUser(User user) {
        final ContentValues values = userToContentValues(user);

        String selection = Contract.User._ID + "=?";
        String[] selectionArgs = new String[]{ user.getId() };

        int rowsUpdatedCount = context.getContentResolver().update(Contract.User.CONTENT_URI, values, selection, selectionArgs);
        if(rowsUpdatedCount == 1)
            return Completable.complete();  //updated successfully

        return Completable.error(new OCSChatThrowable(Constants.ERROR_FROM_DATABASE));  //something went wrong
    }

    /**
     * get friends' ids of a user with a given id
     * @param userId
     * @return
     */
    private ArrayList<String> getUserFriendsIds(String userId){
        String[] projection = new String[] { Contract.Friend.COLUMN_FRIEND_ID };

        String selection = Contract.Friend.COLUMN_USER_ID + "=?";
        String[] selectionArgs = new String[]{ userId };

        final Cursor cursor = context.getContentResolver()
                .query(Contract.Friend.CONTENT_URI, projection, selection, selectionArgs, null);

        if(cursor == null){
            return null;
        }
        Log.d(TAG, "cursor count = " + cursor.getCount());
        final int friendIdIndex = cursor.getColumnIndex(Contract.Friend.COLUMN_FRIEND_ID);

        final ArrayList<String> friendsIds = new ArrayList<>();

        while(cursor.moveToNext()){
            Log.d(TAG, "add friend id " + cursor.getString(friendIdIndex));
            friendsIds.add(cursor.getString(friendIdIndex));
        }

        cursor.close();

        Log.d(TAG, "got friends ids count = " + friendsIds.size());
        return friendsIds;
    }

    private ContentValues userToContentValues(User user){
        final ContentValues values = new ContentValues();

        values.put(Contract.User._ID, user.getId());
        values.put(Contract.User.COLUMN_FIRST_NAME, user.getFirstName());
        values.put(Contract.User.COLUMN_LAST_NAME, user.getLastName());
        values.put(Contract.User.COLUMN_AGE, user.getAge());
        if(user.getHasImage()){
            values.put(Contract.User.COLUMN_HAS_IMAGE, 1);
            //TODO: handle inserting user image in the database
        }
        values.put(Contract.User.COLUMN_EDUCATION, user.getEducation());
        values.put(Contract.User.COLUMN_EDUCATION_ORGANIZATION, user.getEducationOrganization());
        values.put(Contract.User.COLUMN_MAJOR, user.getMajor());
        values.put(Contract.User.COLUMN_WORK, user.getWork());
        values.put(Contract.User.COLUMN_COMPANY, user.getCompany());

        return values;
    }

    private ContentValues friendToContentValues(String userId, Friend friend){
        final ContentValues values = new ContentValues();

        values.put(Contract.Friend.COLUMN_USER_ID, userId);
        values.put(Contract.Friend.COLUMN_FRIEND_ID, friend.getId());
        values.put(Contract.Friend.COLUMN_FRIEND_STATE, friend.getState().ordinal());

        return values;
    }

}
