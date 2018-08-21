package com.example.android.ocschat.viewModel.impl;

import com.example.android.ocschat.dataLayer.HomeApi;
import com.example.android.ocschat.dataLayer.LoginApi;
import com.example.android.ocschat.viewModel.HomeViewModel;
import com.example.android.ocschat.viewModel.LoginViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelModule {

    @Provides @Singleton
    LoginViewModel provideLoginViewModel(LoginApi api){
        return new LoginViewModelImpl(api);
    }

    @Provides @Singleton
    HomeViewModel provideHomeViewModel(HomeApi api){
        return new HomeViewModelImpl(api);
    }

}
