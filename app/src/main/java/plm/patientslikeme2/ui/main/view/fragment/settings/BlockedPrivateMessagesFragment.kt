package plm.patientslikeme2.ui.main.view.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.settings.BlockUnBlockUserRequest
import plm.patientslikeme2.data.model.settings.BlockerUser
import plm.patientslikeme2.databinding.FragmentBlockedPrivateMessagesBinding
import plm.patientslikeme2.databinding.RowBlockedPrivateMessagesBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyBindingInterface
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyListAdapter
import plm.patientslikeme2.ui.main.viewmodel.settings.SettingsViewModel

@AndroidEntryPoint
class BlockedPrivateMessagesFragment : BaseFragment<FragmentBlockedPrivateMessagesBinding>() {

    private val viewModel: SettingsViewModel by activityViewModels()

    private lateinit var mAdapter: GenericLazyListAdapter<BlockerUser, RowBlockedPrivateMessagesBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentBlockedPrivateMessagesBinding =
        FragmentBlockedPrivateMessagesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.blockedPrivateMessagesFragment
        initView()
        getBlockedUserList()
    }

    private fun getBlockedUserList() {
        mAdapter.pageNo++
        if (isConnected()) bindObservers() else showEmptyView(getString(R.string.error_internet))
    }

    private fun initView() {
        mAdapter = GenericLazyListAdapter(R.layout.row_blocked_private_messages, object :
            GenericLazyBindingInterface<RowBlockedPrivateMessagesBinding, BlockerUser> {
            override fun bindData(
                binder: RowBlockedPrivateMessagesBinding,
                model: BlockerUser,
                position: Int
            ) {
                binder.model = model
                binder.tvBlockUnblock.setOnClickListener {
                    if (binder.tvBlockUnblock.text.toString() == getString(R.string.block)) {
                        blockUser(model.id, position)
                    } else if (binder.tvBlockUnblock.text.toString() == getString(R.string.unblock)) {
                        unblockUser(model.id, position)
                    }
                }
                if (position == mAdapter.itemCount - 1) {
                    binder.viewDivider.isVisible = false
                }
                binder.executePendingBindings()
            }
        })
        binding?.rvBlockedMembers?.adapter = mAdapter
        binding?.parent?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                getBlockedUserList()
            }
        })
    }

    private fun blockUser(id: Int, position: Int) {
        viewModel.blockedPrivateMessages(BlockUnBlockUserRequest(id)).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        showSuccessErrorLayout(true, it.data?.message.toString())
                        val listItem = mAdapter.currentList[position]
                        listItem.blocked = false
                        mAdapter.notifyItemChanged(position)
                    } else {
                        showSuccessErrorLayout(false, it.data?.message.toString())
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    var errorMessage = apiErrorMessage(it.errorMessage)
                    if (errorMessage.isEmpty()) {
                        errorMessage = getString(R.string.failed_block_user)
                    }
                    showSuccessErrorLayout(false, errorMessage)
                }
            }
        }
    }

    private fun unblockUser(id: Int, position: Int) {
        viewModel.unBlockedPrivateMessages(BlockUnBlockUserRequest(id))
            .observe(viewLifecycleOwner) {
                when {
                    it.status.isLoading() -> {
                        showProgress()
                    }
                    it.status.isSuccessful() -> {
                        hideProgress()
                        if (it.data?.success == true) {
                            showSuccessErrorLayout(true, it.data?.message.toString())
                            val listItem = mAdapter.currentList[position]
                            listItem.blocked = true
                            mAdapter.notifyItemChanged(position)
                        } else {
                            showSuccessErrorLayout(false, it.data?.message.toString())
                        }
                    }
                    it.status.isError() -> {
                        hideProgress()
                        var errorMessage = apiErrorMessage(it.errorMessage)
                        if (errorMessage.isEmpty()) {
                            errorMessage = getString(R.string.failed_unblock_user)
                        }
                        showSuccessErrorLayout(false, errorMessage)
                    }
                }
            }
    }

    private fun bindObservers() {
        viewModel.getBlockedPrivateMessages(mAdapter.pageNo).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    if (mAdapter.pageNo == 1) {
                        showProgress()
                    }
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if ((it.data?.data?.blocked_users?.size ?: 0) > 0) {
                        if (mAdapter.pageNo == 1) {
                            val list = it.data?.data?.blocked_users
                            mAdapter.submitList(list)
                        } else if ((it.data?.data?.blocked_users?.size ?: 0) > 0) {
                            val itemList: ArrayList<BlockerUser> = arrayListOf()
                            itemList.addAll(mAdapter.currentList)
                            itemList.addAll(it.data?.data?.blocked_users ?: arrayListOf())
                            mAdapter.submitList(itemList)
                        }
                    } else if (mAdapter.pageNo == 1) {
                        showEmptyView(getString(R.string.no_blocked_members))
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    showEmptyView(getString(R.string.error_api))
                }
            }
        }
    }

    private fun showEmptyView(error: String) {
        binding?.tvNoResult?.text = error
        binding?.tvNoResult?.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            getString(R.string.blocked_private_messages),
            showBackArrow = true,
            hideRightIcon = true
        )
    }
}