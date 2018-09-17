package com.example.android.ocschat.fragment

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
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
import kotlinx.android.synthetic.main.fragment_friend_info.*
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
        return inflater.inflate(R.layout.fragment_friend_info, container, false)
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
        friend_info_add_friend_button.setOnClickListener {
            friend_info_add_friend_button.isClickable = false
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
                friend_info_add_friend_button.isClickable = true
                Toast.makeText(context, R.string.error_adding_friend, Toast.LENGTH_SHORT).show()
                Log.d(TAG, it.message)
            })
        }
    }

    private fun checkFriendImage(currentUser: User){
        Log.d(TAG, "checkFriendImage")
        friend_info_image_view.setImageResource(R.drawable.person_placeholder)
        if(currentUser.hasImage){
            Log.d(TAG, "user has image")
            //show loading progress bar
            friend_info_image_loading_progress_bar.visibility = View.VISIBLE
            if(isFriend!!){
                Log.d(TAG, "user is a friend")
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, Uri.parse(currentUser.imageFilePath))
                friend_info_image_view.setImageBitmap(bitmap)
            }
            else{
                Log.d(TAG, "user is not a friend")
                //download image from fire-base storage
                addFriendViewMdel
                        .downloadImage(Uri.parse(currentUser.imageUrl), currentUser.id)
                        .subscribe ({
                            userImageFile = it
                            //hide loading progress bar
                            friend_info_image_loading_progress_bar.visibility = View.GONE
                            Log.d(TAG, "got downloaded image")
                            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, Uri.fromFile(it))
                            friend_info_image_view.setImageBitmap(bitmap)
                        }, {
                            //hide loading progress bar
                            friend_info_image_loading_progress_bar.visibility = View.GONE
                            Toast.makeText(context, getString(R.string.error_downloading_image), Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "failed to download image")
                        })
            }
        }
    }

    private fun displayFriendInfo(user : User){
        friend_info_name_text_view.text = user.name
        if(user.age != null){
            friend_info_age_text_view.visibility = View.VISIBLE
            friend_info_age_text_view.setText(user.age.toString())
        }
        if(user.education != null){
            friend_info_education_text_view.visibility = View.VISIBLE
            friend_info_education_text_view.setText(user.education)
        }
        if(user.educationOrganization != null){
            friend_info_education_org_text_view.visibility = View.VISIBLE
            friend_info_education_org_text_view.setText(user.educationOrganization)
        }
        if(user.major != null){
            friend_info_major_text_view.visibility = View.VISIBLE
            friend_info_major_text_view.setText(user.major)
        }
        if(user.work != null){
            friend_info_work_text_view.visibility = View.VISIBLE
            friend_info_work_text_view.setText(user.work)
        }
        if(user.company != null){
            friend_info_company_text_view.visibility = View.VISIBLE
            friend_info_company_text_view.setText(user.company)
        }
    }

    private fun showFriendState(){
        setMenuVisibility(false)
        friend_info_add_friend_button.visibility = View.VISIBLE
        friend_info_add_friend_button.background = resources.getDrawable(R.drawable.already_friend)
        friend_info_add_friend_button.setTextColor(resources.getColor(R.color.black))
        friend_info_add_friend_button.text = resources.getString(R.string.friend)
    }

    private fun showAddFriendState(){
        setMenuVisibility(false)
        friend_info_add_friend_button.visibility = View.VISIBLE
        friend_info_add_friend_button.background = resources.getDrawable(R.drawable.add_friend)
        friend_info_add_friend_button.setTextColor(resources.getColor(R.color.white))
        friend_info_add_friend_button.text = resources.getString(R.string.add_friend)
    }
}