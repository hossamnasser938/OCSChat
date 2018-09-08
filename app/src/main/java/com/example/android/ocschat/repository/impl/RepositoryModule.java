package com.example.android.ocschat.repository.impl;

import com.example.android.ocschat.api.AddFriendApi;
import com.example.android.ocschat.localDatabase.Gate;
import com.example.android.ocschat.repository.AddFriendRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides @Singleton
    public AddFriendRepository provideAddFriendRepository(Gate gate, AddFriendApi api){
        return new AddFriendRepositoryImpl(gate, api);
    }

}
