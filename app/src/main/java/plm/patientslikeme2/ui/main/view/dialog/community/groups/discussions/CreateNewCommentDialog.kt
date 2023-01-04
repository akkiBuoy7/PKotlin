package plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.DialogNewCommentBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.viewmodel.community.groups.DiscussionThreadViewModel
import plm.patientslikeme2.utils.file.URIPathHelper
import plm.patientslikeme2.utils.usercontrol.HtmlEditor
import java.io.File


class CreateNewCommentDialog(private val discussionTitle: String, private val discussionId: String, private val shouldRefresh: () -> Unit) : BaseBottomSheetFragment<DialogNewCommentBinding>() {

    private val viewModel: DiscussionThreadViewModel by activityViewModels()

    private var pickMedia = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            result.data?.data?.let { selectedImageUri ->
                val filePath = URIPathHelper().getPath(requireContext(), selectedImageUri)
                if (filePath.isNullOrEmpty().not()) {
                    val file = File(filePath)
                    if (file.exists()) {
                        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                        val multiPartBody = MultipartBody.Part.createFormData("uploaded_image[image]", file.name, requestFile)
                        viewModel.uploadImage(multiPartBody).observe(viewLifecycleOwner) {
                            when {
                                it.status.isLoading() -> {
                                    showProgress()
                                }
                                it.status.isSuccessful() -> {
                                    hideProgress()
                                    it.data?.data?.uploadedImage?.imageUrl?.let {
                                        binding?.editor?.htmlText?.focusEditor()
                                        binding?.editor?.htmlText?.insertImage(it, "Image")
                                    }
                                }
                                it.status.isError() -> {
                                    hideProgress()
                                    it.errorMessage?.let {
                                        showSuccessErrorLayout(binding?.llErrorSuccess, false, it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): DialogNewCommentBinding = DialogNewCommentBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding?.tvDiscussionHeader?.text = "Q: $discussionTitle"
        setListeners()
    }

    private fun setListeners() {
        binding?.editor?.htmlText?.mHTMLUpdatesListener = object : HtmlEditor.HTMLUpdatesListener {
            override fun onAfterInitialLoad(isReady: Boolean) {
                validateData()
                if (isReady) binding?.editor?.htmlText?.setPlaceholder(requireContext().getString(R.string.leave_comment))
            }

            override fun onTextChange(text: String?) {
                validateData()
            }

            override fun onStateChangeListener(text: String?, types: List<HtmlEditor.Type?>?) {

            }
        }

        binding?.editor?.htmlText?.setPlaceholder(requireContext().getString(R.string.leave_comment))

        binding?.ivClose?.setOnClickListener {
            dismiss()
        }

        binding?.btnCancel?.setOnClickListener {
            dismiss()
        }

        binding?.btnSave?.setOnClickListener {
            createComment()
        }

        binding?.editor?.icBoldText?.setOnClickListener {
            binding?.editor?.htmlText?.setBold()
        }

        binding?.editor?.icItalicText?.setOnClickListener {
            binding?.editor?.htmlText?.setItalic()
        }

        binding?.editor?.icUnderlineText?.setOnClickListener {
            binding?.editor?.htmlText?.setUnderline()
        }

        binding?.editor?.icBulletList?.setOnClickListener {
            binding?.editor?.htmlText?.setBullets()
        }

        binding?.editor?.icNumberList?.setOnClickListener {
            binding?.editor?.htmlText?.setNumbers()
        }

        binding?.editor?.icAddLink?.setOnClickListener {
            AddLinkDialog { title, link ->
                binding?.editor?.htmlText?.focusEditor()
                binding?.editor?.htmlText?.insertLink(link, title)
            }.show(childFragmentManager, getString(R.string.add_a_link))
        }

        binding?.editor?.icAddGallery?.setOnClickListener {
            val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            pickImageIntent.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            pickImageIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            pickMedia.launch(pickImageIntent)
        }

    }

    private fun validateData() {
        val valid = !(binding?.editor?.htmlText?.getHtml().isNullOrEmpty())
        binding?.btnSave?.isClickable = valid
        binding?.btnSave?.isEnabled = valid
        binding?.btnSave?.alpha = if (valid) 1F else 0.5F
    }

    private fun createComment() {
        binding?.editor?.htmlText?.getHtml()?.let {
            viewModel.addCommentsOnDiscussion(discussionId, it).observe(viewLifecycleOwner) {
                when {
                    it.status.isLoading() -> {
                        showProgress()
                    }
                    it.status.isSuccessful() -> {
                        hideProgress()
                        shouldRefresh()
                        dismiss()
                    }
                    it.status.isError() -> {
                        hideProgress()
                        it.errorMessage?.let {
                            showSuccessErrorLayout(binding?.llErrorSuccess, false, it)
                        }
                    }
                }
            }
        }
    }
}