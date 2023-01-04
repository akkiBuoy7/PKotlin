package plm.patientslikeme2.ui.main.view.fragment.messages

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.data.model.messages.ThreadMessages
import plm.patientslikeme2.databinding.FragmentMessageConversationBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.messages.MessagesConversationAdapter
import plm.patientslikeme2.ui.main.view.dialog.messages.MessagingMoreOptionDialog
import plm.patientslikeme2.ui.main.view.dialog.messages.NewMessageDialog
import plm.patientslikeme2.ui.main.viewmodel.messages.MessagesViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.openPrivateMessageConversation

class MessageConversationFragment :
    BaseFragment<FragmentMessageConversationBinding>() {

    private val viewModel: MessagesViewModel by activityViewModels()
    var messagesList: ArrayList<ThreadMessages> = ArrayList()
    private lateinit var messagesAdapter: MessagesConversationAdapter
    lateinit var mLayoutManger: LinearLayoutManager
    private var isLoadMore = false
    var lastPage = false
    private val firstVisibleItemPosition: Int
        get() = mLayoutManger.findFirstCompletelyVisibleItemPosition()
    var isFirstLoad = false

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): FragmentMessageConversationBinding =
        FragmentMessageConversationBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.navigation_messaging_conversation
        initViews()
        if (isConnected()) {
            viewModel.messageConversationPage = 1
            bindObservers()
        } else showEmptyView(getString(R.string.error_internet), true)
    }

    private fun initRecyclerViewScrollListener() {
        binding?.rvConversations?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0 && firstVisibleItemPosition == 0 && !isLoadMore && !lastPage) {
                    bindObservers()
                }
            }
        })
    }

    private fun initViews() {
        if (viewModel.blockedByOtherParticipant) {
            setReplyButtonDisable()
        }
        binding?.btnReplyMessage?.setOnClickListener {
            openPrivateMessageConversation(Constants.REPLY_MESSAGE)
        }
        mLayoutManger = LinearLayoutManager(activity)
        messagesAdapter = MessagesConversationAdapter()
        binding?.rvConversations?.apply {
            layoutManager = mLayoutManger
            adapter = messagesAdapter
        }
        initRecyclerViewScrollListener()
        messagesAdapter.setData(messagesList)

        messagesAdapter.onItemClicked = { model, type ->
            when (type) {
                Constants.REPORT_AS_SPAM -> {
                    viewModel.messageId = model.id.toString()
                    viewModel.optionCallFrom = Constants.OTHERS_MESSAGE
                    MessagingMoreOptionDialog.newInstance()
                        .show(childFragmentManager, getString(R.string.more_option))
                }
            }
        }
    }

    private fun setReplyButtonDisable() {
        binding?.btnReplyMessage?.isEnabled = false
        binding?.btnReplyMessage?.alpha = 0.5F
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindObservers() {
        val smoothScroller: RecyclerView.SmoothScroller =
            object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
        viewModel.getMessageThreadConversation().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (!isLoadMore) {
                        isLoadMore = true
                        viewModel.messageConversationPage++
                        if (it.data != null) {
                            if (it.data?.data?.messages?.size!! > 0) {

                                it.data?.data?.messages?.let { list ->
                                    if (messagesList.size == 0) {
                                        isFirstLoad = true
                                        viewModel.replyMessageId = list[0].id.toString()
                                        viewModel.replyMessageText = list[0].body_html.toString()
                                    } else {
                                        isFirstLoad = false
                                    }
                                    messagesList.addAll(list)
                                }

                                val list = it.data?.data?.messages!!
                                list.sortBy { m -> m.created_at }

                                binding?.rvConversations?.post { messagesAdapter.setData(list) }

                                Handler(Looper.getMainLooper()).postDelayed({
                                    if (isFirstLoad) {
                                        smoothScroller.targetPosition =
                                            messagesAdapter.itemCount - 1
                                        mLayoutManger.startSmoothScroll(smoothScroller)
                                    }
                                }, 500)

                                isLoadMore = false
                                lastPage = it.data?.data?.last_page == true

                                binding?.btnReplyMessage?.text = getString(R.string.reply)
                                showEmptyView("", false)
                            } else {
                                binding?.btnReplyMessage?.text = getString(R.string.new_message)
                                showEmptyView(getString(R.string.no_messages_found), true)
                            }
                        }
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    showEmptyView(errorMessage, true)
                }
            }
        }
    }

    fun showSuccessErrorView(message: String?, status: Boolean?) {
        if (status != null) {
            if (message != null) {
                showSuccessErrorLayout(status, message)
                clearData()
                bindObservers()
            }
        }
    }

    private fun clearData() {
        messagesList.clear()
        messagesAdapter.clearData()
        viewModel.messageConversationPage = 1
        isLoadMore = false
        lastPage = false
        isFirstLoad = false
    }

    private fun showEmptyView(error: String, show: Boolean) {
        if (show) {
            binding?.tvNoResult?.text = error
            binding?.tvNoResult?.visibility = View.VISIBLE
            binding?.rvConversations?.visibility = View.GONE
        } else {
            binding?.tvNoResult?.visibility = View.GONE
            binding?.rvConversations?.visibility = View.VISIBLE
        }
    }

    private fun updateOptionMoreIcon() {
        toolbarBinding?.ivClose?.setImageResource(R.drawable.ic_more)
        toolbarBinding?.ivMessaging?.visibility = View.GONE
        toolbarBinding?.ivNotification?.visibility = View.GONE
        toolbarBinding?.ivClose?.visibility = View.VISIBLE
        toolbarBinding?.llRight?.visibility = View.VISIBLE

        toolbarBinding?.ivClose?.setOnClickListener {
            viewModel.optionCallFrom = Constants.WHOLE_THREAD
            MessagingMoreOptionDialog.newInstance()
                .show(childFragmentManager, getString(R.string.more_option))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeOptionMoreIcon()
        hideBottomNavigationMenu(false)
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigationMenu(true)
        updateToolbar(getString(R.string.messaging), showBackArrow = true, hideRightIcon = true)
        updateOptionMoreIcon()
    }
}