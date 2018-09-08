package com.example.android.ocschat.viewModel.impl;

import com.example.android.ocschat.repository.AddFriendRepository;
import com.example.android.ocschat.repository.ChatRepository;
import com.example.android.ocschat.repository.HomeRepository;
import com.example.android.ocschat.repository.LoginRepository;
import com.example.android.ocschat.repository.SettingsRepository;
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
    LoginViewModel provideLoginViewModel(LoginRepository repository){
        return new LoginViewModelImpl(repository);
    }

    @Provides @Singleton
    HomeViewModel provideHomeViewModel(HomeRepository repository){
        return new HomeViewModelImpl(repository);
    }

    @Provides @Singleton
    AddFriendViewModel provideAddFriendViewModel(AddFriendRepository repository){
        return new AddFriendViewModelImpl(repository);
    }

    @Provides @Singleton
    ChatViewModel provideChatViewModel(ChatRepository repository){
        return new ChatViewModelImpl(repository);
    }

    @Provides @Singleton
    SettingsViewModel provideSettingsViewModel(SettingsRepository repository){
        return new SettingsViewModelImpl(repository);
    }

}
