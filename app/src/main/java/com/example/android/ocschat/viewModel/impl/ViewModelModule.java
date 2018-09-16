package com.example.android.ocschat.viewModel.impl;

import com.example.android.ocschat.repository.AddFriendRepository;
import com.example.android.ocschat.repository.ChatRepository;
import com.example.android.ocschat.repository.HomeRepository;
import com.example.android.ocschat.repository.LoginRepository;
import com.example.android.ocschat.repository.SettingsRepository;
import com.example.android.ocschat.repository.impl.BaseRepository;
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
    LoginViewModel provideLoginViewModel(LoginRepository repository, BaseRepository baseRepository){
        return new LoginViewModelImpl(repository, baseRepository);
    }

    @Provides @Singleton
    HomeViewModel provideHomeViewModel(HomeRepository repository, BaseRepository baseRepository){
        return new HomeViewModelImpl(repository, baseRepository);
    }

    @Provides @Singleton
    AddFriendViewModel provideAddFriendViewModel(AddFriendRepository repository, BaseRepository baseRepository){
        return new AddFriendViewModelImpl(repository, baseRepository);
    }

    @Provides @Singleton
    ChatViewModel provideChatViewModel(ChatRepository repository, BaseRepository baseRepository){
        return new ChatViewModelImpl(repository, baseRepository);
    }

    @Provides @Singleton
    SettingsViewModel provideSettingsViewModel(SettingsRepository repository, BaseRepository baseRepository){
        return new SettingsViewModelImpl(repository, baseRepository);
    }

}
