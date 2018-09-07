package com.example.android.ocschat.localDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.android.ocschat.model.User;

public class Gate {

    private Context context;

    public Gate(Context context){
        this.context = context;
    }

    /**
     * query the database for a specific user
     * @param userId
     * @return
     */
    public User getUser(String userId){
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
            return null;
        }

        cursor.moveToNext();

        //get columns indices
        int firstNameIndex = cursor.getColumnIndex(Contract.User.COLUMN_FIRST_NAME);
        int lastNameIndex = cursor.getColumnIndex(Contract.User.COLUMN_LAST_NAME);
        int ageIndex = cursor.getColumnIndex(Contract.User.COLUMN_AGE);
        int hasImageIndex = cursor.getColumnIndex(Contract.User.COLUMN_HAS_IMAGE);
        int imageIndex = cursor.getColumnIndex(Contract.User.COLUMN_IMAGE);
        int educationIndex = cursor.getColumnIndex(Contract.User.COLUMN_EDUCATION);
        int educationOrgIndex = cursor.getColumnIndex(Contract.User.COLUMN_EDUCATION_ORGANIZATION);
        int majorIndex = cursor.getColumnIndex(Contract.User.COLUMN_MAJOR);
        int workIndex = cursor.getColumnIndex(Contract.User.COLUMN_WORK);
        int companyIndex = cursor.getColumnIndex(Contract.User.COLUMN_COMPANY);

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

        cursor.close();

        //construct User object
        final User user = new User(userId, firstName, lastName);
        user.setAge(age);
        user.setEducation(education);
        user.setEducationOrganization(educationOrg);
        user.setMajor(major);
        user.setWork(work);
        user.setCompany(company);

        return user;
    }

    /**
     * insert a user in the database
     * @param user
     * @return
     */
    public boolean insertUser(User user){
        final ContentValues values = userToContentValues(user);

        if(context.getContentResolver().insert(Contract.User.CONTENT_URI, values) != null)
            return true;  //inserted successfully

        return false;  //something went wrong
    }

    /**
     * update a specific user with the same id in the database
     * @param user
     * @return
     */
    public boolean updateUser(User user){
        final ContentValues values = userToContentValues(user);

        String selection = Contract.User._ID + "=?";
        String[] selectionArgs = new String[]{ user.getId() };

        int rowsUpdatedCount = context.getContentResolver().update(Contract.User.CONTENT_URI, values, selection, selectionArgs);
        if(rowsUpdatedCount == 1)
            return true;  //updated successfully

        return false;  //something went wrong
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
}
