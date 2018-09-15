package com.example.android.ocschat;

import android.app.Application;
import android.content.Context;

import com.example.android.ocschat.api.impl.ApiModule;
import com.example.android.ocschat.localDatabase.impl.LocalDatabaseModule;
import com.example.android.ocschat.repository.impl.RepositoryModule;
import com.example.android.ocschat.viewModel.impl.ViewModelModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ApiModule.class, LocalDatabaseModule.class, RepositoryModule.class, ViewModelModule.class})
public class AppModule {

    private Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return app;
    }

}
