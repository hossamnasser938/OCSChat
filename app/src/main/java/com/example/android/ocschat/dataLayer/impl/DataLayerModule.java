package com.example.android.ocschat.dataLayer.impl;

import com.example.android.ocschat.dataLayer.AddFriendApi;
import com.example.android.ocschat.dataLayer.ChatApi;
import com.example.android.ocschat.dataLayer.HomeApi;
import com.example.android.ocschat.dataLayer.LoginApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataLayerModule {

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

}
