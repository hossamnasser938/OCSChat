package com.example.android.ocschat.repository.impl;

import com.example.android.ocschat.api.AddFriendApi;
import com.example.android.ocschat.api.ChatApi;
import com.example.android.ocschat.localDatabase.Gate;
import com.example.android.ocschat.repository.AddFriendRepository;
import com.example.android.ocschat.repository.ChatRepository;
import com.example.android.ocschat.repository.HomeRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides @Singleton
    public AddFriendRepository provideAddFriendRepository(Gate gate, AddFriendApi api){
        return new AddFriendRepositoryImpl(gate, api);
    }

    @Provides @Singleton
    public ChatRepository provideChatRepository(Gate gate, ChatApi api){
        return new ChatRepositoryImpl(gate, api);
    }

    @Provides @Singleton
    public HomeRepository provideHomeRepository(Gate gate){
        return new HomeRepositoryImpl(gate);
    }

}
