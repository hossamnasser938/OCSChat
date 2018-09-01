package com.example.android.ocschat.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import com.example.android.ocschat.R
import com.example.android.ocschat.model.User
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.viewModel.SettingsViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_update_profile.*
import javax.inject.Inject

class UpdateProfileFragment : Fragment(){

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    lateinit var disposable : Disposable

    private lateinit var currentlyLoggedUser : User

    companion object {
        fun newInstance(user : User) : UpdateProfileFragment{
            val updateProfileFragment = UpdateProfileFragment()
            val args = Bundle()
            args.putSerializable(Constants.USER_KEY, user)
            updateProfileFragment.arguments = args
            return updateProfileFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_update_profile , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentlyLoggedUser = arguments?.get(Constants.USER_KEY) as User
        loadUserInfo()
    }

    override fun onStop() {
        super.onStop()

        try { disposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.user_info_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.update_profile_menu_item -> {
                //call firebase service to update current user info
                try {
                    currentlyLoggedUser.name = update_profile_name_edit_text.text.toString()
                    disposable = settingsViewModel.updateCurrentUser(currentlyLoggedUser)
                            .subscribe({
                                //Inform user and go back to Home activity
                                Toast.makeText(context, Constants.ACCOUNT_UPDATED_SUCCESSFULLY, Toast.LENGTH_SHORT).show()
                                activity?.finish()
                            }, {
                                Log.d("UpdateProfileFragment", it.message)
                                Toast.makeText(context, Constants.ERROR_UPDATING_ACCOUNT, Toast.LENGTH_SHORT).show()
                            })
                }
                catch (e : UninitializedPropertyAccessException){

                }
            }
        }
        return true
    }


    private fun loadUserInfo(){
        try {
            update_profile_name_edit_text.setText(currentlyLoggedUser.name)
            //TODO: set user image
        }
        catch (e : UninitializedPropertyAccessException){ }
    }
}