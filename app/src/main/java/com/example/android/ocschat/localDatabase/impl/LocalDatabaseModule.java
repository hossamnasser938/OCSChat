package com.example.android.ocschat.localDatabase.impl;

import android.content.Context;

import com.example.android.ocschat.localDatabase.Gate;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalDatabaseModule {

    @Provides @Singleton
    Gate provideGate(Context context){
        return new GateImpl(context);
    }

}
