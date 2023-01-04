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
import plm.patientslikeme2.data.model.settings.HiddenPost
import plm.patientslikeme2.data.model.settings.HiddenUnHiddenPostRequest
import plm.patientslikeme2.databinding.FragmentHiddenPostsBinding
import plm.patientslikeme2.databinding.RowHiddenPostBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyBindingInterface
import plm.patientslikeme2.ui.main.adapter.base.GenericLazyListAdapter
import plm.patientslikeme2.ui.main.viewmodel.settings.SettingsViewModel

@AndroidEntryPoint
class HiddenPostsFragment : BaseFragment<FragmentHiddenPostsBinding>() {

    private val viewModel: SettingsViewModel by activityViewModels()

    private lateinit var mAdapter: GenericLazyListAdapter<HiddenPost, RowHiddenPostBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHiddenPostsBinding = FragmentHiddenPostsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.hiddenPostsFragment
        initView()
        getHiddenPostUsers()
    }

    private fun getHiddenPostUsers() {
        mAdapter.pageNo++
        if (isConnected()) bindObservers() else showEmptyView(getString(R.string.error_internet))
    }

    private fun initView() {
        mAdapter = GenericLazyListAdapter(R.layout.row_hidden_post, object :
            GenericLazyBindingInterface<RowHiddenPostBinding, HiddenPost> {
            override fun bindData(binder: RowHiddenPostBinding, model: HiddenPost, position: Int) {
                binder.model = model
                binder.tvHideUnhide.setOnClickListener {
                    if (binder.tvHideUnhide.text.toString() == getString(R.string.hide)) {
                        hideUser(model.id, position)
                    } else if (binder.tvHideUnhide.text.toString() == getString(R.string.unhide)) {
                        unHideUser(model.id, position)
                    }
                }
                if (position == mAdapter.itemCount - 1) {
                    binder.viewDivider.isVisible = false
                }
                binder.executePendingBindings()
            }
        })
        binding?.rvPosts?.adapter = mAdapter

        binding?.parent?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                getHiddenPostUsers()
            }
        })
    }

    private fun hideUser(id: Int, position: Int) {
        viewModel.hiddenPosts(HiddenUnHiddenPostRequest(id)).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    if (mAdapter.pageNo == 1) {
                        showProgress()
                    }
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        showSuccessErrorLayout(true, it.data?.message.toString())
                        val listItem = mAdapter.currentList[position]
                        listItem.hidden = false
                        mAdapter.notifyItemChanged(position)
                    } else {
                        showSuccessErrorLayout(false, it.data?.message.toString())
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    var errorMessage = apiErrorMessage(it.errorMessage)
                    if (errorMessage.isEmpty()) {
                        errorMessage = getString(R.string.failed_hidden_user)
                    }
                    showSuccessErrorLayout(false, errorMessage)
                }
            }
        }
    }

    private fun unHideUser(id: Int, position: Int) {
        viewModel.unHiddenPosts(HiddenUnHiddenPostRequest(id))
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
                            listItem.hidden = true
                            mAdapter.notifyItemChanged(position)
                        } else {
                            showSuccessErrorLayout(false, it.data?.message.toString())
                        }
                    }
                    it.status.isError() -> {
                        hideProgress()
                        var errorMessage = apiErrorMessage(it.errorMessage)
                        if (errorMessage.isEmpty()) {
                            errorMessage = getString(R.string.failed_unhidden_user)
                        }
                        showSuccessErrorLayout(false, errorMessage)
                    }
                }
            }
    }

    private fun bindObservers() {
        viewModel.getHiddenPosts(mAdapter.pageNo).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    if (mAdapter.pageNo == 1) {
                        showProgress()
                    }
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if ((it.data?.data?.hidden_users?.size ?: 0) > 0) {
                        if (mAdapter.pageNo == 1) {
                            val list = it.data?.data?.hidden_users
                            mAdapter.submitList(list)
                        } else if ((it.data?.data?.hidden_users?.size ?: 0) > 0) {
                            val itemList: ArrayList<HiddenPost> = arrayListOf()
                            itemList.addAll(mAdapter.currentList)
                            itemList.addAll(it.data?.data?.hidden_users ?: arrayListOf())
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
            getString(R.string.hidden_posts),
            showBackArrow = true,
            hideRightIcon = true
        )
    }
}