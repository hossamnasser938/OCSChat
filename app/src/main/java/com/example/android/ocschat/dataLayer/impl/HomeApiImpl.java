package com.example.android.ocschat.dataLayer.impl;

import com.example.android.ocschat.dataLayer.HomeApi;
import com.example.android.ocschat.model.User;
import com.example.android.ocschat.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import durdinapps.rxfirebase2.RxFirebaseQuery;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;


public class HomeApiImpl implements HomeApi {

    @Override
    public Flowable<DataSnapshot> getCurrentUserFriends() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentUserFriendsReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY).child(currentUserId).child(Constants.FRIENDS_KEY);
        return RxFirebaseDatabase.observeValueEvent(currentUserFriendsReference);
    }

    @Override
    public Single<List<DataSnapshot>> getCurrentUserFriendsAsUsers(final List<String> friendsIdsList) {
        DatabaseReference usersReferences = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY);
        return RxFirebaseQuery.getInstance().filter(usersReferences, new Function<DataSnapshot, DatabaseReference[]>() {
            @Override
            public DatabaseReference[] apply(DataSnapshot dataSnapshot) throws Exception {
                //Filter users to get only friends of current user
                List<DatabaseReference> referencesList = new ArrayList<DatabaseReference>();
                User user;
                for(DataSnapshot dsS : dataSnapshot.getChildren()){
                    user = dsS.getValue(User.class);
                    if(friendsIdsList.contains(user.getId())){
                        referencesList.add(dsS.getRef());
                    }
                }
                DatabaseReference[] referencesArray = new DatabaseReference[referencesList.size()];
                return referencesList.toArray(referencesArray);
            }
        }).asList();
    }
}
