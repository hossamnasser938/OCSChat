package com.example.android.ocschat;

import com.example.android.ocschat.activity.HomeActivity;
import com.example.android.ocschat.fragment.AddFriendFragment;
import com.example.android.ocschat.fragment.AddPhotoFragment;
import com.example.android.ocschat.fragment.ChatFragment;
import com.example.android.ocschat.fragment.FriendInfoFragment;
import com.example.android.ocschat.fragment.HomeFragment;
import com.example.android.ocschat.fragment.LoginFragment;
import com.example.android.ocschat.fragment.RegisterFragment;
import com.example.android.ocschat.fragment.RegisterMoreInfoFragment;
import com.example.android.ocschat.fragment.SettingsFragment;
import com.example.android.ocschat.fragment.UpdateProfileFragment;
import com.example.android.ocschat.fragment.UserInfoFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(LoginFragment loginFragment);
    void inject(RegisterFragment registerFragment);
    void inject(AddPhotoFragment addPhotoFragment);
    void inject(HomeFragment homeFragment);
    void inject(AddFriendFragment addFriendFragment);
    void inject(FriendInfoFragment friendInfoFragment);
    void inject(ChatFragment chatFragment);
    void inject(SettingsFragment settingsFragment);
    void inject(UpdateProfileFragment updateProfileFragment);
    void inject(HomeActivity homeActivity);

}
