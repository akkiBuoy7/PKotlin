package plm.patientslikeme2.ui.main.view.dialog.messages

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.data.model.community.members.Users
import plm.patientslikeme2.data.model.messages.NewMessageRequest
import plm.patientslikeme2.data.model.messages.ReplyMessageRequest
import plm.patientslikeme2.data.model.signup.User
import plm.patientslikeme2.databinding.DialogNewMessageBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.adapter.messages.MessageSearchUserAdapter
import plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions.AddLinkDialog
import plm.patientslikeme2.ui.main.view.fragment.community.groups.groupDetails.GroupMembersFragment
import plm.patientslikeme2.ui.main.view.fragment.community.members.CommunityMembersFragment
import plm.patientslikeme2.ui.main.view.fragment.messages.MessageConversationFragment
import plm.patientslikeme2.ui.main.view.fragment.messages.MessagingFragment
import plm.patientslikeme2.ui.main.view.fragment.profile.UserProfileFragment
import plm.patientslikeme2.ui.main.viewmodel.messages.MessagesViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.file.URIPathHelper
import java.io.File

class NewMessageDialog : BaseBottomSheetFragment<DialogNewMessageBinding>() {

    private val viewModel: MessagesViewModel by activityViewModels()
    var adapter: MessageSearchUserAdapter? = null
    private lateinit var type: String
    private lateinit var selectedUser: Users
    private var pickMedia =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { selectedImageUri ->
                    val filePath = URIPathHelper().getPath(requireContext(), selectedImageUri)
                    if (filePath.isNullOrEmpty().not()) {
                        val file = filePath?.let { File(it) }
                        if (file?.exists() == true) {
                            val requestFile: RequestBody =
                                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val multiPartBody =
                                MultipartBody.Part.createFormData("uploaded_image[image]",
                                    file.name, requestFile)
                            viewModel.uploadImage(multiPartBody).observe(viewLifecycleOwner) {
                                when {
                                    it.status.isLoading() -> showProgress()
                                    it.status.isSuccessful() -> {
                                        it.data?.data?.uploadedImage?.imageUrl?.let { img ->
                                            binding?.htmlText?.focusEditor()
                                            binding?.htmlText?.insertImage(img, "Image")
                                        }
                                        hideProgress()
                                    }
                                    it.status.isError() -> {
                                        hideProgress()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    companion object {
        fun newInstance(type: String): NewMessageDialog {
            return NewMessageDialog().apply {
                val bundle = Bundle().apply {
                    putString(Constants.TYPE, type)
                }
                arguments = bundle
            }
        }
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        args?.let {
            type = args.getString(Constants.TYPE).toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): DialogNewMessageBinding = DialogNewMessageBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.handlers = this
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        binding?.htmlText?.setPlaceholder("")

        when (type) {
            Constants.NEW_MESSAGE -> {
                binding?.tvReplyMessage?.visibility = View.GONE
                binding?.btnSend?.text = Constants.SEND
            }
            Constants.NEW_MESSAGE_FROM_PROFILE, Constants.NEW_MESSAGE_FROM_GROUP, Constants.NEW_MESSAGE_FROM_COMMUNITY -> {
                binding?.tvReplyMessage?.visibility = View.GONE
                binding?.btnSend?.text = Constants.SEND
                binding?.tvTo?.visibility = View.GONE
                binding?.tvToName?.visibility = View.VISIBLE
                binding?.tvToName?.text = viewModel.otherParticipantName
                binding?.acSearchUser?.visibility = View.GONE
                binding?.tvHeader?.text = getString(R.string.new_message)
            }
            else -> {
                binding?.tvTo?.visibility = View.GONE
                binding?.acSearchUser?.visibility = View.GONE
                binding?.btnSend?.text = Constants.REPLY_CAP_FIRST_LETTER
                val replyText =
                    ("<html><head><style img{display: inline;height: auto;max-width: 100%;} type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/Poppins-Regular.ttf\")}body{font-family: MyFont;color: #697171}"
                            + "</style></head><body>" + viewModel.replyMessageText + "</body></html>")
                binding?.tvReplyMessage?.loadDataWithBaseURL(null,
                    replyText, "text/html", "UTF-8", null)
                binding?.tvHeader?.text =
                    getString(R.string.replyto) + " " + viewModel.otherParticipantName
            }
        }

        adapter = MessageSearchUserAdapter(requireContext(), R.layout.row_user_drop_down)
        binding?.acSearchUser?.setAdapter(adapter)
        binding?.acSearchUser?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length > 1) {
                    searchUserObserver(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding?.acSearchUser?.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, pos, _ ->
                selectedUser = parent?.getItemAtPosition(pos) as Users
            }
    }

    @SuppressLint("IntentReset")
    fun onItemClicked(view: View?) {
        if (!isAdded) {
            return
        }
        when (view?.id) {
            R.id.iv_close -> dialog?.dismiss()
            R.id.btn_send -> validatedDetails()
            R.id.btn_cancel -> dialog?.dismiss()
            R.id.ic_bold_text -> binding?.htmlText?.setBold()
            R.id.ic_italic_text -> binding?.htmlText?.setItalic()
            R.id.ic_underline_text -> binding?.htmlText?.setUnderline()
            R.id.ic_bullet_list -> binding?.htmlText?.setBullets()
            R.id.ic_number_list -> binding?.htmlText?.setNumbers()
            R.id.ic_add_link -> AddLinkDialog { title, link ->
                binding?.htmlText?.insertLink(link, title)
            }.show(childFragmentManager, getString(R.string.add_a_link))
            R.id.ic_add_gallery -> {
                val pickImageIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                pickImageIntent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                pickImageIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                pickMedia.launch(pickImageIntent)
            }
        }
    }

    private fun validatedDetails() {
        val message = binding?.htmlText?.getHtml().toString()
        if (type == Constants.NEW_MESSAGE) {
            if (!this::selectedUser.isInitialized) {
                binding?.acSearchUser?.setBackgroundResource(R.drawable.bg_edittext_round_red)
                showError(getString(R.string.user_not_empty))
                return
            }
            hideError()
            binding?.acSearchUser?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        }

        if (message.isNullOrEmpty() || message == "null") {
            binding?.htmlText?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            showError(getString(R.string.message_not_empty))
            return
        }
        hideError()
        binding?.htmlText?.setBackgroundResource(R.drawable.bg_edittext_round_gray)

        if (type == Constants.REPLY_MESSAGE) {
            replyMessageObserver()
        } else {
            sendNewMessageObserver()
        }
    }

    private fun showError(message: String?) {
        binding?.tvEmailError?.text = message
        binding?.llError?.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding?.llError?.visibility = View.GONE
    }

    private fun searchUserObserver(query: String) {
        viewModel.getSearchMembers(query).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        it.data?.data?.users?.let { usersList ->
                            adapter?.setData(usersList)
                            adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    private fun sendNewMessageObserver() {
        viewModel.sendNewMessage(sendMessageRequest()).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> showProgress()
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        hideProgress()

                        viewModel.messageId = it?.data?.data?.message?.id.toString()
                        markAsRead()

                        dismiss()
                        when (type) {
                            Constants.NEW_MESSAGE -> {
                                (parentFragment as MessagingFragment).showSuccessErrorView(it.data?.message.toString(),
                                    it.data?.success)
                            }
                            Constants.NEW_MESSAGE_FROM_PROFILE -> {
                                (parentFragment as UserProfileFragment).showSuccessErrorView(it.data?.message.toString(),
                                    it.data?.success)
                            }
                            Constants.NEW_MESSAGE_FROM_GROUP -> {
                                (parentFragment as GroupMembersFragment).showSuccessErrorView(it.data?.message.toString(),
                                    it.data?.success)
                            }
                            Constants.NEW_MESSAGE_FROM_COMMUNITY -> {
                                (parentFragment as CommunityMembersFragment).showSuccessErrorView(it.data?.message.toString(),
                                    it.data?.success)
                            }
                        }
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    when (type) {
                        Constants.NEW_MESSAGE -> {
                            (parentFragment as MessagingFragment).showSuccessErrorView(errorMessage,
                                false)
                        }
                        Constants.NEW_MESSAGE_FROM_PROFILE -> {
                            (parentFragment as UserProfileFragment).showSuccessErrorView(
                                errorMessage,
                                false)
                        }
                        Constants.NEW_MESSAGE_FROM_GROUP -> {
                            (parentFragment as GroupMembersFragment).showSuccessErrorView(
                                errorMessage,
                                false)
                        }
                        Constants.NEW_MESSAGE_FROM_COMMUNITY -> {
                            (parentFragment as CommunityMembersFragment).showSuccessErrorView(
                                errorMessage,
                                false)
                        }
                    }
                }
            }
        }
    }

    private fun sendMessageRequest(): NewMessageRequest {
        val message = binding?.htmlText?.getHtml().toString()
        val user = User().stringToObject(Preferences.getValue(Constants.USER, ""))
        val request = NewMessageRequest()
        request.body_html = message
        request.sender_id = user.id.toString()
        if (type == Constants.NEW_MESSAGE) {
            request.recipient_id = selectedUser.id.toString()
        } else {
            request.recipient_id = viewModel.otherParticipantId
        }
        request.image_urls = arrayListOf()
        return request
    }

    private fun replyMessageObserver() {
        viewModel.replyMessage(replyMessageRequest()).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> showProgress()
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        hideProgress()
                        viewModel.replyMessageId = it?.data?.data?.message?.id.toString()
                        viewModel.replyMessageText = it?.data?.data?.message?.body_html.toString()

                        viewModel.messageId = it?.data?.data?.message?.id.toString()
                        markAsRead()

                        dismiss()
                        (parentFragment as MessageConversationFragment).showSuccessErrorView(
                            it.data?.message.toString(), it.data?.success)
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    (parentFragment as MessageConversationFragment).showSuccessErrorView(
                        errorMessage, false)
                }
            }
        }
    }

    private fun markAsRead() {
        viewModel.markAsRead().observe(viewLifecycleOwner) { }
    }

    private fun replyMessageRequest(): ReplyMessageRequest {
        val message = binding?.htmlText?.getHtml().toString()
        val user = User().stringToObject(Preferences.getValue(Constants.USER, ""))
        val request = ReplyMessageRequest()
        request.body_html = message
        request.sender_id = user.id.toString()
        request.reply_to_message_id = viewModel.replyMessageId
        request.recipient_id = viewModel.otherParticipantId
        request.image_urls = arrayListOf()
        return request
    }
}