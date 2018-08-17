package com.example.android.ocschat.viewModel.impl;

import com.example.android.ocschat.dataLayer.LoginApi;
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

}
