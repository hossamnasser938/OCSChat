package com.example.android.ocschat.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import com.example.android.ocschat.OCSChatApplication
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
        (activity?.application as OCSChatApplication).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
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
        inflater?.inflate(R.menu.update_profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.update_menu_item -> {
                //Show progress dialog
                val progressDialog = ProgressDialog(activity)
                progressDialog.setTitle(R.string.updating_profile)
                progressDialog.show()

                updateUserProperties()

                //call firebase service to update current user info
                disposable = settingsViewModel.updateCurrentUser(currentlyLoggedUser)
                        .subscribe({
                            //Inform user and go back to Home activity
                            Log.d("UpdateProfileFragment", "Updated")
                            Toast.makeText(context, Constants.ACCOUNT_UPDATED_SUCCESSFULLY, Toast.LENGTH_SHORT).show()
                            progressDialog.dismiss()
                            activity?.finish()
                        }, {
                            Log.d("UpdateProfileFragment", it.message)
                            Toast.makeText(context, Constants.ERROR_UPDATING_ACCOUNT, Toast.LENGTH_SHORT).show()
                            progressDialog.dismiss()
                        })

            }
            R.id.cancel_menu_item -> {
                //finish Settings activity and go back to Home activity
                activity?.finish()
            }
        }
        return true
    }

    private fun updateUserProperties() {
        currentlyLoggedUser.name = update_profile_name_edit_text.text.toString()
    }


    private fun loadUserInfo(){
        try {
            update_profile_name_edit_text.setText(currentlyLoggedUser.name)
            if(currentlyLoggedUser.hasImage){
                //TODO: set user image
            }
            else{
                update_profile_image_view.setImageResource(R.drawable.person_placeholder)
            }
        }
        catch (e : UninitializedPropertyAccessException){ }
    }
}