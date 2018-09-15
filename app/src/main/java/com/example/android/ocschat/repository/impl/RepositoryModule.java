package com.example.android.ocschat.repository.impl;

import android.content.Context;

import com.example.android.ocschat.api.AddFriendApi;
import com.example.android.ocschat.api.ChatApi;
import com.example.android.ocschat.api.HomeApi;
import com.example.android.ocschat.api.LoginApi;
import com.example.android.ocschat.api.SettingsApi;
import com.example.android.ocschat.localDatabase.Gate;
import com.example.android.ocschat.repository.AddFriendRepository;
import com.example.android.ocschat.repository.ChatRepository;
import com.example.android.ocschat.repository.HomeRepository;
import com.example.android.ocschat.repository.LoginRepository;
import com.example.android.ocschat.repository.SettingsRepository;

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
    public HomeRepository provideHomeRepository(Gate gate, HomeApi api, Context context){
        return new HomeRepositoryImpl(gate, api, context);
    }

    @Provides @Singleton
    public LoginRepository provideLoginRepository(Gate gate, LoginApi api){
        return new LoginRepositoryImpl(gate, api);
    }

    @Provides @Singleton
    public SettingsRepository provideSettingsRepository(Gate gate, SettingsApi api){
        return new SettingsRepositoryImpl(gate, api);
    }

}
