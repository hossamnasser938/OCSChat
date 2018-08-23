package com.example.android.ocschat;

import com.example.android.ocschat.fragment.AddFriendFragment;
import com.example.android.ocschat.fragment.ChatFragment;
import com.example.android.ocschat.fragment.HomeFragment;
import com.example.android.ocschat.fragment.LoginFragment;
import com.example.android.ocschat.fragment.RegisterFragment;
import com.example.android.ocschat.fragment.UserInfoFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(LoginFragment loginFragment);
    void inject(RegisterFragment registerFragment);
    void inject(HomeFragment homeFragment);
    void inject(AddFriendFragment addFriendFragment);
    void inject(UserInfoFragment userInfoFragment);
    void inject(ChatFragment chatFragment);

}
