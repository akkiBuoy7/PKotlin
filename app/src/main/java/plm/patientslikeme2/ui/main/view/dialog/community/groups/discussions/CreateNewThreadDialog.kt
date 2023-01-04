package plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.data.model.community.groups.discussions.CreateDiscussion
import plm.patientslikeme2.data.model.community.groups.discussions.TagsItem
import plm.patientslikeme2.databinding.DialogNewThreadBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.viewmodel.community.groups.GroupViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.addHintWithArrayList
import plm.patientslikeme2.utils.extensions.fromJson
import plm.patientslikeme2.utils.file.URIPathHelper
import plm.patientslikeme2.utils.usercontrol.HtmlEditor
import java.io.File


class CreateNewThreadDialog : BaseBottomSheetFragment<DialogNewThreadBinding>() {

    private val viewModel: GroupViewModel by activityViewModels()
    private var selectedTags = ArrayList<String>()

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): DialogNewThreadBinding = DialogNewThreadBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    companion object {
        var onPostDiscussion: ((shouldRefresh: Boolean, msg: String) -> Unit)? = null
        fun newInstance(type: String, onPostDiscussion: ((shouldRefresh: Boolean, msg: String) -> Unit)?): CreateNewThreadDialog {
            this.onPostDiscussion = onPostDiscussion
            return CreateNewThreadDialog().apply {
                val bundle = Bundle().apply {
                    putString(Constants.TYPE, type)
                }
                arguments = bundle
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        setListeners()
    }

    private fun setListeners() {

        binding?.editor?.htmlText?.mHTMLUpdatesListener = object : HtmlEditor.HTMLUpdatesListener {
            override fun onAfterInitialLoad(isReady: Boolean) {
                validateData()
                if (isReady) binding?.editor?.htmlText?.setPlaceholder(requireContext().getString(R.string.new_discussion_hint))
            }

            override fun onTextChange(text: String?) {
                validateData()
            }

            override fun onStateChangeListener(text: String?, types: List<HtmlEditor.Type?>?) {

            }
        }

        binding?.editor?.htmlText?.setPlaceholder(requireContext().getString(R.string.new_discussion_hint))

        binding?.ivClose?.setOnClickListener {
            dismiss()
        }

        binding?.btnCancel?.setOnClickListener {
            dismiss()
        }

        binding?.btnSave?.setOnClickListener {
            createDiscussion()
        }

        binding?.editor?.icBoldText?.setOnClickListener {
            binding?.editor?.htmlText?.focusEditor()
            binding?.editor?.htmlText?.setBold()
        }

        binding?.editor?.icItalicText?.setOnClickListener {
            binding?.editor?.htmlText?.focusEditor()
            binding?.editor?.htmlText?.setItalic()
        }

        binding?.editor?.icUnderlineText?.setOnClickListener {
            binding?.editor?.htmlText?.focusEditor()
            binding?.editor?.htmlText?.setUnderline()
        }

        binding?.editor?.icBulletList?.setOnClickListener {
            binding?.editor?.htmlText?.focusEditor()
            binding?.editor?.htmlText?.setBullets()
        }

        binding?.editor?.icNumberList?.setOnClickListener {
            binding?.editor?.htmlText?.focusEditor()
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

        binding?.edtTitle?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validateData()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding?.acSearchTags?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length > 1) {
                    searchTags(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding?.acSearchTags?.onItemClickListener = AdapterView.OnItemClickListener { parent, arg1, pos, id ->
            dismissDropDown()
            val item = parent?.getItemAtPosition(pos) as TagsItem
            setSelectedChips(item)
            binding?.acSearchTags?.text = null
        }

        bindObservers()
    }

    private fun createDiscussion() {

        val discussion = CreateDiscussion()

        if (binding?.cgSelectedTags?.children?.count()!! > 0) {
            binding?.cgSelectedTags?.children?.toList()?.forEach { it ->
                val text = (it as Chip).text
                selectedTags.add(text.toString())
            }
        } else {
            selectedTags = ArrayList()
        }

        discussion.apply {
            this.featured = false
            this.title = binding?.edtTitle?.text.toString()
            this.body = binding?.editor?.htmlText?.getHtml()
            this.tags = selectedTags
        }

        viewModel.discussionRequest.discussion = discussion
        Log.e("discussion request", Gson().toJson(viewModel.discussionRequest))
        postDiscussion()
    }

    private fun validateData() {
        val valid = !(binding?.editor?.htmlText?.getHtml().isNullOrEmpty() || binding?.edtTitle?.text.isNullOrEmpty())
        binding?.btnSave?.isClickable = valid
        binding?.btnSave?.isEnabled = valid
        binding?.btnSave?.alpha = if (valid) 1F else 0.5F
    }

    private fun searchTags(query: String) {
        viewModel.searchTags(query).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        updateSearchSymptom(it.data?.data?.tags)
                    }
                }
            }
        }
    }

    private fun updateSearchSymptom(data: ArrayList<TagsItem>?) {
        activity?.let { it1 ->
            if (!data.isNullOrEmpty()) {
                binding?.acSearchTags?.addHintWithArrayList(it1, data)
            }
        }
    }

    private fun dismissDropDown() {
        if (isAdded) {
            binding?.acSearchTags?.post { binding?.acSearchTags?.dismissDropDown() }
        }
    }

    private fun bindObservers() {
        viewModel.recommendedTags.observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    it.data?.let {
                        it.data?.tags?.let {
                            setRecommendedChips(it)
                        }
                    }
                }
            }
        }
    }

    private fun setRecommendedChips(chipsList: List<TagsItem?>) {
        if (binding?.cgRecommendedTags?.childCount!! > 0) {
            binding?.cgRecommendedTags?.removeAllViews()
        }
        for (chipObj in chipsList) {
            val chip = layoutInflater.inflate(R.layout.layout_user_chip, binding?.cgRecommendedTags, false) as Chip
            chip.text = chipObj?.text
            chip.id = chipObj?.id!!
            chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
            binding?.cgRecommendedTags?.addView(chip)
        }
        for (i in 0 until binding?.cgRecommendedTags?.childCount!!) {
            val chip = binding?.cgRecommendedTags?.getChildAt(i) as Chip
            chip.setOnClickListener {
                val chipObj = TagsItem(chip.id, chip.text.toString())
                setSelectedChips(chipObj)
                binding?.cgRecommendedTags?.removeView(chip)
            }
        }
    }

    private fun setSelectedChips(chipObj: TagsItem) {
        val chip = layoutInflater.inflate(
            R.layout.layout_user_chip, binding?.cgSelectedTags, false
        ) as Chip
        chip.text = chipObj.text
        chip.id = chipObj.id!!

        chip.textAlignment = View.TEXT_ALIGNMENT_CENTER
        chip.isCloseIconVisible = true
        binding?.cgSelectedTags?.addView(chip)
        chip.setOnCloseIconClickListener { view ->
            binding?.cgSelectedTags?.removeView(view)
        }
    }

    fun postDiscussion() {
        viewModel.createDiscussion().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        onPostDiscussion?.invoke(true, it.data?.message.toString())
//                        showSuccessErrorLayout(binding?.llErrorSuccess, it.data?.success!!, it.data?.message.toString())
                        dismiss()
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val error = Gson().fromJson<ErrorResponse>(it.errorMessage.toString()) as ErrorResponse
                    showSuccessErrorLayout(binding?.llErrorSuccess, error.success!!, error.message.toString())
                }
            }
        }
    }
}