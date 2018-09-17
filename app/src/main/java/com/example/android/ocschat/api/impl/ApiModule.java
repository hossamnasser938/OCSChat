package com.example.android.ocschat.api.impl;

import com.example.android.ocschat.api.AddFriendApi;
import com.example.android.ocschat.api.ChatApi;
import com.example.android.ocschat.api.HomeApi;
import com.example.android.ocschat.api.LoginApi;
import com.example.android.ocschat.api.SettingsApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {

    @Provides @Singleton
    LoginApi provideLoginApi(){
        return new LoginApiImpl();
    }

    @Provides @Singleton
    HomeApi provideHomeApi(){
        return new HomeApiImpl();
    }

    @Provides @Singleton
    AddFriendApi provideAddFriendApi(){
        return new AddFriendApiImpl();
    }

    @Provides @Singleton
    ChatApi provideChatApi(){
        return new ChatApiImpl();
    }

    @Provides @Singleton
    SettingsApi provideSettingsApi(){
        return new SettingsApiImpl();
    }

    @Provides @Singleton
    BaseApi provideBaseApi(){
        return new BaseApi();
    }

}
