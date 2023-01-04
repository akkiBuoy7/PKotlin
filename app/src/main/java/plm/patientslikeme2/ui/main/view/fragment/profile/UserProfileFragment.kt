package plm.patientslikeme2.ui.main.view.fragment.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.data.model.profile.UserProfile
import plm.patientslikeme2.databinding.FragmentUserProfileBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.BaseFragmentStateAdapter
import plm.patientslikeme2.ui.main.view.dialog.messages.NewMessageDialog
import plm.patientslikeme2.ui.main.viewmodel.messages.MessagesViewModel
import plm.patientslikeme2.ui.main.viewmodel.profile.ProfileViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.loadImage
import plm.patientslikeme2.utils.extensions.openPrivateMessageConversation
import plm.patientslikeme2.utils.usercontrol.CustomViewPager2

@AndroidEntryPoint
class UserProfileFragment : BaseFragment<FragmentUserProfileBinding>() {

    private val viewModel: ProfileViewModel by activityViewModels()
    private val messageViewModel: MessagesViewModel by activityViewModels()

    private lateinit var argUserId: String

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentUserProfileBinding = FragmentUserProfileBinding.inflate(inflater, container, false)

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        args?.let {
            argUserId = args.getString(Constants.USER_ID).toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.user_profile

        viewModel.userId = argUserId

        if (viewModel.userId.isNotEmpty()) {
            binding?.llFollowPrivateMessage?.visibility = View.VISIBLE
        }
        binding?.handlers = this

        initTabLayout()

        if (isConnected()) {
            bindObserver()
        } else {
            showSuccessErrorLayout(false, getString(R.string.error_internet))
        }
    }

    fun onItemClicked(view: View) {
        if (!isAdded) {
            return
        }
        when (view.id) {
            R.id.btn_follow -> {
                if (binding?.btnFollow?.text == Constants.MEMBER_FOLLOW) {
                    memberFollowObserver()
                } else {
                    memberUnFollowObserver()
                }
            }
            R.id.btn_private_message -> {
                openPrivateMessageConversation(Constants.NEW_MESSAGE_FROM_PROFILE)
            }
        }
    }

    private fun setUnFollow() {
        binding?.btnFollow?.text = Constants.MEMBER_UNFOLLOW
        requireContext().let { context ->
            binding?.btnFollow?.setTextColor(
                ContextCompat.getColor(context, R.color.gray_dark_5)
            )
            binding?.btnFollow?.background = ContextCompat.getDrawable(
                context, R.drawable.bg_button_dark_green_scale
            )
        }
    }

    private fun setFollow() {
        binding?.btnFollow?.text = Constants.MEMBER_FOLLOW
        requireContext().let { context ->
            binding?.btnFollow?.setTextColor(
                ContextCompat.getColor(context, R.color.white)
            )
            binding?.btnFollow?.background = ContextCompat.getDrawable(
                context, R.drawable.bg_button_dark_green
            )
        }
    }

    private fun initTabLayout() {
        val tabsArray = getTabs()
        CustomViewPager2().viewPager2 = binding?.pager
        binding?.pager?.adapter =
            BaseFragmentStateAdapter(childFragmentManager, lifecycle, tabsArray)
        binding?.pager?.isUserInputEnabled = false

        binding?.tabLayout?.let {
            binding?.pager?.let { it1 ->
                TabLayoutMediator(it, it1) { tab, position ->
                    tab.text = tabsArray[position].first
                }.attach()
            }
        }
    }

    private fun getTabs(): ArrayList<Pair<String, BaseFragment<out ViewDataBinding>>> {
        val tabsArray = ArrayList<Pair<String, BaseFragment<out ViewDataBinding>>>()
        tabsArray.add(Pair("About", MemberAboutFragment()))
        tabsArray.add(Pair("Conditions", MemberConditionsFragment()))
        tabsArray.add(Pair("Following", MemberFollowingFragment()))
        return tabsArray
    }

    private fun memberFollowObserver() {
        viewModel.memberFollow().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data != null) {
                        setUnFollow()
                        it?.data?.message?.let { message ->
                            showSuccessErrorLayout(true, message)
                        }
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    showSuccessErrorLayout(false, errorMessage)
                }
            }
        }
    }

    private fun memberUnFollowObserver() {
        viewModel.memberUnFollow().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data != null) {
                        setFollow()
                        it?.data?.message?.let { message ->
                            showSuccessErrorLayout(true, message)
                        }
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    showSuccessErrorLayout(false, errorMessage)
                }
            }
        }
    }

    private fun bindObserver() {
        viewModel.getMemberProfile().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data != null) {
                        binding?.model = it.data
                        it.data?.data?.user_profile.apply {
                            renderData(this)
                        }
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    showSuccessErrorLayout(false, errorMessage)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(userProfile: UserProfile?) {

        messageViewModel.otherParticipantId = userProfile?.id.toString()
        messageViewModel.otherParticipantName = userProfile?.login.toString()

        binding?.ivUser?.loadImage(userProfile?.name, userProfile?.avatar_url)

        if (userProfile?.followed == true) setUnFollow() else setFollow()

        var livingWith = ""
        for (item in userProfile?.living_with!!) {
            livingWith += "$item, "
        }
        binding?.tvAddress?.text =
            userProfile.address + "\n" + getString(R.string.living_with) + " " + livingWith
    }

    fun showSuccessErrorView(message: String?, status: Boolean?) {
        if (status != null) {
            if (message != null) {
                showSuccessErrorLayout(status, message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigationMenu(true)
        updateToolbar(
            "", showBackArrow = true, hideRightIcon = true, showCloseRightIcon = true
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        hideBottomNavigationMenu(false)
    }
}