package plm.patientslikeme2.ui.main.view.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.di.MyApplication
import plm.patientslikeme2.data.model.home.NotificationModel
import plm.patientslikeme2.databinding.FragmentNotificationBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.notification.NotificationAdapter
import plm.patientslikeme2.ui.main.viewmodel.home.HomeViewModel
import plm.patientslikeme2.utils.Constants.NOTIFICATION_LIST
import plm.patientslikeme2.utils.callback.CallBackListener
import plm.patientslikeme2.utils.enum.ContextType
import plm.patientslikeme2.utils.extensions.openGroupDiscussionDetails
import plm.patientslikeme2.utils.extensions.openTreatmentDetails
import plm.patientslikeme2.utils.extensions.openUserProfile
import plm.patientslikeme2.utils.widgets.RecyclerTouchListener

@AndroidEntryPoint
class
NotificationFragment : BaseFragment<FragmentNotificationBinding>(),
    CallBackListener<NotificationModel> {

    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var mAdapter: NotificationAdapter
    private var touchListener: RecyclerTouchListener? = null

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentNotificationBinding = FragmentNotificationBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.navigation_notification
        initView()
        getNotification()
    }

    private fun initView() {
        mAdapter = NotificationAdapter(arrayListOf(), this)
        binding?.rvNotification?.adapter = mAdapter

        touchListener = RecyclerTouchListener(requireActivity(), binding?.rvNotification!!)
        touchListener?.setClickable(object : RecyclerTouchListener.OnRowClickListener {
            override fun onRowClicked(position: Int) {}
            override fun onIndependentViewClicked(independentViewID: Int, position: Int) {}
        })
            ?.setSwipeOptionViews(R.id.btn_dismiss)
            ?.setSwipeable(R.id.rowFG, R.id.rowBG) { viewID, position ->
                when (viewID) {
                    R.id.btn_dismiss -> {
                        if (isConnected()) {
                            callDismissAPI(mAdapter.itemList[position])
                        } else {
                            showEmptyView(getString(R.string.error_internet))
                        }
                    }
                }
            }
        binding?.rvNotification?.addOnItemTouchListener(touchListener!!)

        binding?.parent?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                getNotification()
            }
        })
    }

    private fun updateNotificationList(model: NotificationModel?) {
        model?.let {
            it.seen = true
            mAdapter.updateItem(it)
        }
    }

    private fun callDismissAPI(model: NotificationModel?) {
        mAdapter.removeItem(model)
        viewModel.putDismissNotification(model?.id).observe(viewLifecycleOwner) {
            if (mAdapter.itemList.size == 0) {
                showEmptyView(getString(R.string.no_notification))
            }
        }
    }

    private fun getNotification() {
        mAdapter.pageNo++
        if (isConnected()) {
            bindObservers()
        } else {
            if (mAdapter.pageNo == 1) {
                showEmptyView(getString(R.string.error_internet))
            }
        }
    }

    private fun bindObservers() {
        viewModel.getNotificationList(mAdapter.pageNo).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    if (mAdapter.pageNo == 1) {
                        showProgress()
                    }
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data?.mobile_notifications?.size == 0 && mAdapter.pageNo == 1) {
                        showEmptyView(getString(R.string.no_notification))
                    } else {
                        mAdapter.addItemList(it.data?.data?.mobile_notifications)
                        binding?.rvNotification?.visibility = View.VISIBLE
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    if (mAdapter.pageNo == 1) {
                        showEmptyView(getString(R.string.error_api))
                    }
                }
            }
        }
    }

    override fun onClick(data: NotificationModel, type: String) {
        when (data.context_type) {
            ContextType.Topic.name -> openGroupDiscussionDetails(NOTIFICATION_LIST, data.context_id)
            ContextType.Post.name -> openGroupDiscussionDetails(NOTIFICATION_LIST, data.context_id)
            ContextType.User.name -> openUserProfile(NOTIFICATION_LIST, data.context_id)
            ContextType.TreatmentHistory.name -> openTreatmentDetails(
                NOTIFICATION_LIST,
                data.context_id
            )
        }
        viewModel.putMarkAsReadNotification(data.id).observe(viewLifecycleOwner) {}
        updateNotificationList(data)
    }

    private fun showEmptyView(error: String) {
        if (binding?.vsNoResult?.viewStub?.parent != null) {
            binding?.vsNoResult?.setOnInflateListener { _, _ ->
                val binding = binding?.vsNoResult?.binding as LayoutNoResultBinding
                binding.tvMessage.text = error
                binding.clEmpty.visibility = View.VISIBLE
            }
            binding?.vsNoResult?.viewStub?.inflate()
            binding?.rvNotification?.visibility = View.GONE
        }
    }

    private fun bingNotificationCountObserver() {
        viewModel.getUnreadNotificationCount().observe(this) {
            when {
                it.status.isSuccessful() -> {
                    if (isAdded) {
                        MyApplication.unreadNotificationCount = it.data?.data?.notificationCount!!
                        toolbarBinding?.ivNotification?.setImageDrawable(
                            if (MyApplication.unreadNotificationCount == 0) {
                                ContextCompat.getDrawable(requireContext(),
                                    R.drawable.ic_notification)
                            } else {
                                ContextCompat.getDrawable(requireContext(),
                                    R.drawable.ic_notification_badge)
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bingNotificationCountObserver()
        hideBottomNavigationMenu(true)
        updateToolbar(
            getString(R.string.notification),
            showBackArrow = true,
            hideRightIcon = true,
            showCloseRightIcon = true
        )
        touchListener?.let { binding?.rvNotification?.addOnItemTouchListener(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        hideBottomNavigationMenu(false)
        updateToolbar(getString(R.string.empty), false)
    }
}