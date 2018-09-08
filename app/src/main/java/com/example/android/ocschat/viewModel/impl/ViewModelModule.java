package com.example.android.ocschat.viewModel.impl;

import com.example.android.ocschat.api.AddFriendApi;
import com.example.android.ocschat.api.ChatApi;
import com.example.android.ocschat.api.HomeApi;
import com.example.android.ocschat.api.LoginApi;
import com.example.android.ocschat.api.SettingsApi;
import com.example.android.ocschat.repository.AddFriendRepository;
import com.example.android.ocschat.viewModel.AddFriendViewModel;
import com.example.android.ocschat.viewModel.ChatViewModel;
import com.example.android.ocschat.viewModel.HomeViewModel;
import com.example.android.ocschat.viewModel.LoginViewModel;
import com.example.android.ocschat.viewModel.SettingsViewModel;

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

    @Provides @Singleton
    AddFriendViewModel provideAddFriendViewModel(AddFriendRepository repository){
        return new AddFriendViewModelImpl(repository);
    }

    @Provides @Singleton
    ChatViewModel provideChatViewModel(ChatApi api){
        return new ChatViewModelImpl(api);
    }

    @Provides @Singleton
    SettingsViewModel provideSettingsViewModel(SettingsApi api){
        return new SettingsViewModelImpl(api);
    }

}
