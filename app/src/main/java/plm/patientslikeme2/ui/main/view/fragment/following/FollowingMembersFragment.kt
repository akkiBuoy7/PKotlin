package plm.patientslikeme2.ui.main.view.fragment.following

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.api.network.Status
import plm.patientslikeme2.data.model.following.FollowingMemberResponse
import plm.patientslikeme2.data.model.following.MemberModel
import plm.patientslikeme2.databinding.FragmentFollowingMembersBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowFollowingMembersBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyBindingInterface
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyListAdapter
import plm.patientslikeme2.ui.main.viewmodel.following.FollowingViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.ActionDialogListener
import plm.patientslikeme2.utils.extensions.loadAvatarImage
import plm.patientslikeme2.utils.extensions.openUserProfile
import plm.patientslikeme2.utils.extensions.showActionDialog

@AndroidEntryPoint
class FollowingMembersFragment : BaseFragment<FragmentFollowingMembersBinding>() {

    private val viewModel: FollowingViewModel by activityViewModels()
    private lateinit var mAdapter: GenericLazyListAdapter<MemberModel, RowFollowingMembersBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFollowingMembersBinding =
        FragmentFollowingMembersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun getFollowingMembers() {
        viewModel.memberPageNo++
        if (isConnected()) {
            bindObservers()
        } else if (viewModel.memberPageNo == 1) {
            showEmptyView(getString(R.string.error_internet))
        }
    }

    private fun initView() {
        mAdapter = GenericLazyListAdapter(R.layout.row_following_members, object :
            GenericLazyBindingInterface<RowFollowingMembersBinding, MemberModel> {
            override fun bindData(
                binder: RowFollowingMembersBinding, model: MemberModel, position: Int
            ) {
                binder.model = model
                binder.ivUser.loadAvatarImage(model.avatar)

                if (model.showFollowed) {
                    binder.btnUnfollow.visibility = View.VISIBLE
                } else {
                    binder.btnUnfollow.visibility = View.GONE
                }

                binder.btnUnfollow.setOnClickListener {
                    showUnfollowDialog(model)
                }

                binder.cardView.setOnClickListener {
                    openUserProfile(Constants.FOLLOWING_MEMBER, model.id.toString())
                }
                binder.executePendingBindings()
            }
        })
        binding?.rvMembers?.adapter = mAdapter

        binding?.parent?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                getFollowingMembers()
            }
        })

        viewModel.memberFragment.observe(viewLifecycleOwner) {
            when (it) {
                1 -> hideUnFollowIcons()
                2 -> showUnFollowIcons()
            }
        }

        viewModel.memberResponse.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    updateDataInList(it.data)
                }
            }
            checkDataStatus()
        }
    }

    private fun showUnfollowDialog(model: MemberModel) {
        activity?.let {
            showActionDialog(it,
                getString(R.string.unfollow),
                getString(R.string.are_you_sure_unfollow) + " " + model.login + "?",
                getString(R.string.yes),
                getString(R.string.no),
                object : ActionDialogListener {
                    override fun onPositiveButtonClick() {
                        removeMemberFromList(model)
                        callUnfollowMemberAPI(model)
                    }
                })
        }
    }

    private fun callUnfollowMemberAPI(model: MemberModel) {
        viewModel.memberUnFollow(model.id.toString()).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    hideProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }

    private fun hideUnFollowIcons() {
        val itemList: ArrayList<MemberModel> = arrayListOf()
        itemList.addAll(mAdapter.currentList)
        for ((index, model) in itemList.withIndex()) {
            model.showFollowed = false
            itemList[index] = model
        }
        mAdapter.submitList(itemList)
        mAdapter.notifyDataSetChanged()
    }

    private fun showUnFollowIcons() {
        val itemList: ArrayList<MemberModel> = arrayListOf()
        itemList.addAll(mAdapter.currentList)
        for ((index, model) in itemList.withIndex()) {
            model.showFollowed = true
            itemList[index] = model
        }
        mAdapter.submitList(itemList)
        mAdapter.notifyDataSetChanged()
    }

    private fun removeMemberFromList(item: MemberModel) {
        val itemList: ArrayList<MemberModel> = arrayListOf()
        itemList.addAll(mAdapter.currentList)
        itemList.remove(item)
        mAdapter.submitList(itemList)
        mAdapter.notifyDataSetChanged()

        checkResourceIsEmpty()
    }

    private fun checkResourceIsEmpty() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (mAdapter.itemCount <= 0) {
                showEmptyView(getString(R.string.no_following_members))
            }
        }, 200)
    }

    private fun bindObservers() {
        viewModel.getFollowingMembers(viewModel.memberPageNo).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    if (viewModel.memberPageNo == 1) {
                        showProgress()
                    }
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    updateDataInList(it.data)
                }
                it.status.isError() -> {
                    hideProgress()
                    showEmptyView(getString(R.string.error_api))
                }
            }
        }
    }

    private fun updateDataInList(data: FollowingMemberResponse?) {
        if ((data?.data?.members?.size ?: 0) > 0) {
            if (viewModel.memberPageNo == 1) {
                val list = data?.data?.members
                mAdapter.submitList(list)
            } else if ((data?.data?.members?.size ?: 0) > 0) {
                val itemList: ArrayList<MemberModel> = arrayListOf()
                itemList.addAll(mAdapter.currentList)
                itemList.addAll(data?.data?.members ?: arrayListOf())
                mAdapter.submitList(itemList)
            }
            binding?.parent?.visibility = View.VISIBLE
        } else if (viewModel.memberPageNo == 1) {
            showEmptyView(getString(R.string.no_following_members))
        }
    }

    private fun checkDataStatus() {
        when (viewModel.memberResponse.value?.status) {
            Status.LOADING -> {
                showProgress()
            }
            Status.SUCCESS -> {
                hideProgress()
            }
            else -> {
                hideProgress()
                showEmptyView(getString(R.string.error_api))
            }
        }
    }

    private fun showEmptyView(error: String) {
        if (binding?.vsNoResult?.viewStub?.parent != null) {
            binding?.vsNoResult?.setOnInflateListener { _, _ ->
                val binding = binding?.vsNoResult?.binding as LayoutNoResultBinding
                binding.tvMessage.text = error
                binding.clEmpty.visibility = View.VISIBLE
            }
            binding?.vsNoResult?.viewStub?.inflate()
            binding?.parent?.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()

        toolbarBinding?.btnAction?.text = getString(R.string.edit)
        if (mAdapter.itemCount > 0) {
            hideUnFollowIcons()
        }
    }
}