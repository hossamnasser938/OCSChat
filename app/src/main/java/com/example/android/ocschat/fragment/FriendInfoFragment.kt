package com.example.android.ocschat.fragment

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.model.User
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.util.Utils
import com.example.android.ocschat.viewModel.AddFriendViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_user_info.*
import java.io.File
import javax.inject.Inject

class FriendInfoFragment : Fragment() {

    private val TAG = "FriendInfoFragment"

    @Inject
    lateinit var addFriendViewMdel : AddFriendViewModel

    private lateinit var transition : AddFriendFragment.AddFriendTransitionInterface

    private lateinit var isFriendDisposable : Disposable
    private lateinit var addFriendDisposable : Disposable

    private lateinit var currentUser: User
    private lateinit var userImageFile : File
    private var isFriend : Boolean? = null
    private var addedFriend = false

    companion object {
        fun newInstance(user : User) : FriendInfoFragment {
            val friendInfoFragment = FriendInfoFragment()
            val args = Bundle()
            args.putSerializable(Constants.USER_KEY, user)
            friendInfoFragment.arguments = args
            return friendInfoFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
        transition = activity as AddFriendFragment.AddFriendTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setMenuVisibility(false)
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUser = arguments?.get(Constants.USER_KEY) as User

        displayFriendInfo(currentUser)
        checkFriendshipState(currentUser)

        if(!Utils.isNetworkConnected(context)){
            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        Log.d(TAG,"onPause executes")
        super.onPause()

        try { isFriendDisposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }

        try { addFriendDisposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy executes")
        //check if user image is downloaded and user is not added as a friend
        if(!addedFriend){
            Log.d(TAG, "user is not added as a friend")
            try {
                if(userImageFile.delete()){
                    Log.d(TAG, "file deleted")
                }
            }
            catch (e : UninitializedPropertyAccessException){ }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.friend_profile)
    }

    /**
     * Check if this user is a friend of current user or not
     */
    private fun checkFriendshipState(currentUser : User){
        isFriendDisposable = addFriendViewMdel.isFriend(currentUser.id).subscribe({
            isFriend = it
            checkFriendImage(currentUser)
            if(it){
                showFriendState()
            }
            else{
                showAddFriendState()
                setAddFriendButtonClickListen(currentUser)
            }
        }, {
            Toast.makeText(context, Constants.FAILED_CHECKING_FRIENDSHIPSTATE, Toast.LENGTH_SHORT).show()
            Log.d(TAG, it.message)
        })
    }

    private fun setAddFriendButtonClickListen(currentUser: User) {
        user_info_main_button.setOnClickListener {
            user_info_main_button.isClickable = false
            //try to add image path if downloaded
            try{
                currentUser.imageFilePath = Uri.fromFile(userImageFile).toString()
            }
            catch (e : UninitializedPropertyAccessException){
                //did not download image
            }
            addFriendDisposable = addFriendViewMdel.addFriend(currentUser).subscribe({
                addedFriend = true
                showFriendState()
                Toast.makeText(context, R.string.friend_added, Toast.LENGTH_SHORT).show()
                activity?.finish()
            }, {
                user_info_main_button.isClickable = true
                Toast.makeText(context, R.string.error_adding_friend, Toast.LENGTH_SHORT).show()
                Log.d(TAG, it.message)
            })
        }
    }

    private fun checkFriendImage(currentUser: User){
        Log.d(TAG, "checkFriendImage")
        //show profile picture placeholder
        //TODO: convert into rounded
        user_info_image_view.setImageResource(R.drawable.person_placeholder)
        //check if user has image
        if(currentUser.hasImage){
            Log.d(TAG, "user has image")
            //show loading progress bar
            user_info_image_loading_progress_bar.visibility = View.VISIBLE
            if(isFriend!!){
                Log.d(TAG, "user is a friend")
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, Uri.parse(currentUser.imageFilePath))
                val drawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
                drawable.isCircular = true
                user_info_image_view.setImageDrawable(drawable)
                //hide loading progress bar
                user_info_image_loading_progress_bar.visibility = View.GONE
            }
            else{
                Log.d(TAG, "user is not a friend")
                //download image from fire-base storage
                addFriendViewMdel
                        .downloadImage(Uri.parse(currentUser.imageUrl), currentUser.id)
                        .subscribe ({
                            userImageFile = it
                            //hide loading progress bar
                            user_info_image_loading_progress_bar.visibility = View.GONE
                            Log.d(TAG, "got downloaded image")
                            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, Uri.fromFile(it))
                            val drawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
                            drawable.isCircular = true
                            user_info_image_view.setImageDrawable(drawable)
                        }, {
                            //hide loading progress bar
                            user_info_image_loading_progress_bar.visibility = View.GONE
                            Toast.makeText(context, getString(R.string.error_downloading_image), Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "failed to download image")
                        })
            }
        }
    }

    private fun displayFriendInfo(user : User){
        user_info_name_text_view.text = user.name
        if(user.age != null){
            user_info_age_layout.visibility = View.VISIBLE
            user_info_age_text_view.setText(user.age.toString())
        }
        if(user.education != null){
            user_info_education_layout.visibility = View.VISIBLE
            user_info_education_text_view.setText(user.education)
        }
        if(user.educationOrganization != null){
            user_info_education_org_layout.visibility = View.VISIBLE
            user_info_education_org_text_view.setText(user.educationOrganization)
        }
        if(user.major != null){
            user_info_major_layout.visibility = View.VISIBLE
            user_info_major_text_view.setText(user.major)
        }
        if(user.work != null){
            user_info_work_text_view.visibility = View.VISIBLE
            user_info_work_text_view.setText(user.work)
        }
        if(user.company != null){
            user_info_company_layout.visibility = View.VISIBLE
            user_info_company_text_view.setText(user.company)
        }
    }

    private fun showFriendState(){
        user_info_main_button.setImageDrawable(resources.getDrawable(R.mipmap.checked_user_male_24dp))
        user_info_main_button.isClickable = false
    }

    private fun showAddFriendState(){
        user_info_main_button.setImageDrawable(resources.getDrawable(R.mipmap.add_user_male_24dp))
    }
}