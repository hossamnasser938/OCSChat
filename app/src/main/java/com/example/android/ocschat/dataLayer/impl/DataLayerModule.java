package com.example.android.ocschat.dataLayer.impl;

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

}
