package plm.patientslikeme2.ui.main.view.fragment.messages

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.data.model.messages.Messages
import plm.patientslikeme2.data.model.signup.User
import plm.patientslikeme2.databinding.FragmentMessagingBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowMessageThreadBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.view.dialog.messages.NewMessageDialog
import plm.patientslikeme2.ui.main.viewmodel.messages.MessagesViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.extensions.getUpperFirstChar
import plm.patientslikeme2.utils.extensions.loadImage
import plm.patientslikeme2.utils.extensions.openPrivateMessageConversation
import plm.patientslikeme2.utils.extensions.setTextHighlight

class MessagingFragment : BaseFragment<FragmentMessagingBinding>() {

    private val viewModel: MessagesViewModel by activityViewModels()
    private lateinit var messagesAdapter: GenericListAdapter<Messages, RowMessageThreadBinding>
    var messagesList: ArrayList<Messages> = ArrayList()
    var lastListSize = 0
    var isDataLoad = true
    var searchMessages = false
    var lastPage = false

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): FragmentMessagingBinding =
        FragmentMessagingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.navigation_messaging
        binding?.handlers = this
        initViews()
        if (isConnected()) {
            clearData()
            bindViewMessagesObservers("", true)
        } else showEmptyView(getString(R.string.error_internet))
    }

    private fun clearData() {
        viewModel.messagesPage = 1
        lastPage = false
        isDataLoad = false
        lastListSize = 0
    }

    private fun initViews() {
        messagesAdapter =
            GenericListAdapter(R.layout.row_message_thread, object :
                GenericRecyclerBindingInterface<RowMessageThreadBinding, Messages> {
                override fun bindData(
                    binder: RowMessageThreadBinding, model: Messages, position: Int,
                ) {
                    binder.model = model

                    val user = User().stringToObject(Preferences.getValue(Constants.USER, ""))
                    if (model.sender_id?.equals(user.id) == true) {
                        model.other_participant?.login?.let { binder.tvName.getUpperFirstChar(it) }
                        binder.ivUser.loadImage(
                            model.other_participant?.login,
                            model.other_participant?.avatar_url
                        )
                        model.other_participant?.login?.let {
                            binder.tvName.setTextHighlight(
                                it, binding?.acSearchUser?.text.toString())
                        }
                    } else {
                        model.sender?.login?.let { binder.tvName.getUpperFirstChar(it) }
                        binder.ivUser.loadImage(model.sender?.login, model.sender?.avatar_url)
                        model.sender?.login?.let {
                            binder.tvName.setTextHighlight(
                                it, binding?.acSearchUser?.text.toString())
                        }
                    }

                    model.body_markdown?.let {
                        binder.tvMessage.setTextHighlight(it,
                            binding?.acSearchUser?.text.toString())
                    }

                    if (model.body_html?.contains("img src") == true) {
                        binder.image.visibility = View.VISIBLE
                    } else {
                        binder.image.visibility = View.GONE
                    }

                    binder.root.setOnClickListener {
                        openConversationFragment(model)
                    }
                    if (position == messagesAdapter.itemCount - 1) {
                        binder.viewDivider.isVisible = false
                    }
                    binder.executePendingBindings()
                }
            })
        binding?.rvConversations?.adapter = messagesAdapter
        messagesAdapter.submitList(messagesList)

        binding?.acSearchUser?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    searchMessages = true
                }
                if (searchMessages) {
                    clearData()
                    bindViewMessagesObservers(s.toString(), false)
                    if (s.isEmpty()) {
                        searchMessages = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding?.rvConversations?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if ((linearLayoutManager?.findLastCompletelyVisibleItemPosition() == messagesList.size - 1) && !lastPage) {
                    viewModel.messagesPage += 1
                    isDataLoad = true
                    bindViewMessagesObservers("", true)
                }
            }
        })
    }

    private fun openConversationFragment(model: Messages) {
        val user = User().stringToObject(Preferences.getValue(Constants.USER, ""))
        viewModel.messageId = model.id.toString()
        if (model.sender_id?.equals(user.id) == true) {
            viewModel.otherParticipantId = model.other_participant?.id.toString()
            viewModel.otherParticipantName =
                model.other_participant?.login.toString()
        } else {
            viewModel.otherParticipantId = model.sender?.id.toString()
            viewModel.otherParticipantName = model.sender?.login.toString()
        }
        viewModel.blockedByOtherParticipant = model.blocked_by_other_participant == true
        if (model.read == false) {
            markAsRead()
        }
        binding?.acSearchUser?.text?.clear()
        messagesList.clear()
        clearData()
        hideProgress()
        findNavController().navigate(R.id.navigation_messaging_to_messaging_conversation)
    }

    private fun markAsRead() {
        viewModel.markAsRead().observe(viewLifecycleOwner) { }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindViewMessagesObservers(query: String, progress: Boolean) {
        val smoothScroller: RecyclerView.SmoothScroller =
            object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
        viewModel.getViewAllMessages(query).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    if (progress)
                        showProgress()
                }
                it.status.isSuccessful() -> {
                    if (progress)
                        hideProgress()
                    if (!isDataLoad)
                        messagesList.clear()
                    if (it.data != null) {
                        it?.data?.data?.messages.let { messages ->
                            if (messages?.size!! > 0) {
                                binding?.parent?.visibility = View.VISIBLE

                                it.data?.data?.messages?.let { list -> messagesList.addAll(list) }

                                it.data?.data?.messages?.size.let { size ->
                                    if (size != null) {
                                        lastListSize = size
                                    }
                                }

                                messagesList.sortByDescending { m -> m.created_at }
                                messagesAdapter.notifyDataSetChanged()

                                lastPage = messagesList.size == it?.data?.meta?.total_entries
                                if (messagesList.size > Constants.MESSAGES_PAGE_COUNT) {
                                    val linearLayoutManager =
                                        binding?.rvConversations?.layoutManager as LinearLayoutManager?
                                    smoothScroller.targetPosition =
                                        (messagesList.size - lastListSize)
                                    linearLayoutManager?.startSmoothScroll(smoothScroller)
                                }
                                binding?.rvConversations?.visibility = View.VISIBLE
                                binding?.tvNoResult?.visibility = View.GONE
                            } else {
                                binding?.rvConversations?.visibility = View.GONE
                                binding?.tvNoResult?.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                it.status.isError() -> {
                    if (progress)
                        hideProgress()
                    val errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    showEmptyView(errorMessage)
                }
            }
        }
    }

    fun onItemClicked(view: View?) {
        if (!isAdded) {
            return
        }
        when (view?.id) {
            R.id.btn_new_message -> {
                openPrivateMessageConversation(Constants.NEW_MESSAGE)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(
            getString(R.string.messaging), showBackArrow = true,
            hideRightIcon = true, showCloseRightIcon = true
        )
        hideBottomNavigationMenu(true)
    }

    fun showSuccessErrorView(message: String?, status: Boolean?) {
        if (status != null) {
            if (message != null) {
                showSuccessErrorLayout(status, message)
                clearData()
                bindViewMessagesObservers("", true)
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

    override fun onDestroy() {
        super.onDestroy()
        hideBottomNavigationMenu(false)
    }
}