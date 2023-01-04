package plm.patientslikeme2.ui.main.view.fragment.signup

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.SignupError
import plm.patientslikeme2.data.model.signup.*
import plm.patientslikeme2.databinding.FragmentSignupYourselfBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.viewmodel.signup.SignupViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Constants.IMAGE_PICK_CODE
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.extensions.addHintWithArrayList
import plm.patientslikeme2.utils.extensions.getFirstCharacter
import plm.patientslikeme2.utils.file.URIPathHelper
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

@AndroidEntryPoint
class SignupYourselfFragment : BaseFragment<FragmentSignupYourselfBinding>(),
    EasyPermissions.PermissionCallbacks {

    private val viewModel: SignupViewModel by activityViewModels()
    private var genderList: ArrayList<String> = arrayListOf()
    private var profileColor = Constants.BLUE
    private var selectedImageUri: Uri? = null

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentSignupYourselfBinding =
        FragmentSignupYourselfBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        genderList.add("Male")
        genderList.add("Female")
        genderList.add(getString(R.string.hint_please_select))
        binding?.tvShortName?.getFirstCharacter(
            Preferences.getValue(Constants.USER_NAME, "").toString()
        )

        activity?.let {
            binding?.spinnerGender?.addHintWithArrayList(it, genderList)
        }

        binding?.etZipCode?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length != 5) {
                    binding?.etZipCode?.setBackgroundResource(R.drawable.bg_edittext_round_red)
                    showZipError(getString(R.string.error_zip_code))
                }
                if (s.length > 4) {
                    getAddressFromZipCode(s.toString())
                }
                if (s.isEmpty()) {
                    binding?.tvZipCode?.text = ""
                    binding?.etZipCode?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
                    hideZipError()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding?.btnChangePhoto?.setOnClickListener {
            showPickerDialog()
        }

        binding?.etProfileHeadline?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updateProfileHeadline(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding?.radioGroup?.setOnCheckedChangeListener { group, _ ->
            val rbOne = group.findViewById<View>(R.id.rb_theme_one) as RadioButton
            val rbTwo = group.findViewById<View>(R.id.rb_theme_two) as RadioButton
            val rbThree = group.findViewById<View>(R.id.rb_theme_three) as RadioButton
            val rbFour = group.findViewById<View>(R.id.rb_theme_four) as RadioButton

            if (rbOne.isChecked) {
                profileColor = Constants.BLUE
                updateThemeColor(R.color.blue)
            }
            if (rbTwo.isChecked) {
                profileColor = Constants.MAGENTA
                updateThemeColor(R.color.purple)
            }
            if (rbThree.isChecked) {
                profileColor = Constants.TEAL
                updateThemeColor(R.color.pink)
            }
            if (rbFour.isChecked) {
                profileColor = Constants.ORANGE
                updateThemeColor(R.color.orange)
            }
        }

        binding?.btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.btnFinish?.setOnClickListener {
            if (isConnected()) {
                validatedEnteredData()
            } else {
                showSuccessErrorLayout(false, getString(R.string.error_internet))
            }
        }
    }

    private fun updateThemeColor(color: Int) {
        if (selectedImageUri == null) {
            binding?.ivUserPhoto?.setImageResource(color)
        }
    }

    private fun showPickerDialog() {
        val perms = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(requireContext(), *perms)) {
            selectImage()
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.msg_runtime_permission), 1, *perms
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateProfileHeadline(toString: String) {
        binding?.tvHeadlineCharacters?.text = "${toString.length}/160 characters"
    }

    private fun validatedEnteredData() {
        hideZipError()
        hideHeadlineError()
        val userUpdate = UserUpdate()
        val gender = binding?.spinnerGender?.selectedItem.toString()
        var selectedGender: String? = null
        if (gender.isNotEmpty()) {
            if (gender == "Male") {
                selectedGender = "M"
            } else if (gender == "Female") {
                selectedGender = "F"
            }
        }
        if (!TextUtils.isEmpty(selectedGender)) {
            userUpdate.sex = selectedGender
        }
        val zipcode = binding?.etZipCode?.text.toString()
        if (zipcode.isNotEmpty() && zipcode.length != 5) {
            binding?.etZipCode?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            showZipError(getString(R.string.error_zip_code))
            return
        }

        binding?.etZipCode?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        if (!TextUtils.isEmpty(zipcode)) {
            userUpdate.postcode = zipcode
        }

        val profileHeadline = binding?.etProfileHeadline?.text.toString()
        if (profileHeadline.isNotEmpty() && profileHeadline.length > 160) {
            binding?.etProfileHeadline?.setBackgroundResource(R.drawable.bg_edittext_round_red)
            showHeadlineError(getString(R.string.error_profile_header))
            return
        }

        if (!TextUtils.isEmpty(profileHeadline)) {
            val userBioAttrs = UserBioAttrs()
            userBioAttrs.brief_bio = profileHeadline
            userUpdate.bio_attrs = userBioAttrs
        }
        val userProfileAttrs = UserProfileAttrs()
        userProfileAttrs.profile_color = profileColor
        userUpdate.user_profile_attrs = userProfileAttrs

        viewModel.updateUserInfoRequest.user = userUpdate
        updateUserInfo()
    }

    private fun getAddressFromZipCode(query: String) {
        viewModel.getSearchZipCode(query).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        if (!it.data?.data.isNullOrEmpty()) {
                            updateCityState(it.data?.data?.get(0))
                        }
                    }
                }
            }
        }
    }

    private fun updateUserInfo() {
        viewModel.postUpdateUserInfo().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        updateUserUpdateInfoResponse(it.data?.data)
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val errorMessage = SignupError().stringToObject(it.errorMessage)
                    showZipError(errorMessage.message)
                }
            }
        }
    }

    private fun updateUserUpdateInfoResponse(signupModel: SignupData?) {
        activity?.let {
            Preferences.setValue(Constants.SIGNUP_STEP, signupModel?.user?.signup_step.toString())
            Preferences.setValue(Constants.USER, Gson().toJson(signupModel?.user))
            openMainActivity()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateCityState(data: ZipCode?) {
        binding?.etZipCode?.setBackgroundResource(R.drawable.bg_edittext_round_gray)
        hideZipError()
        if (data != null && isAdded) {
            binding?.tvZipCode?.text = "${data.city}, ${data.state}"
        }
    }

    private fun showHeadlineError(message: String?) {
        binding?.llHeadlineError?.visibility = View.VISIBLE
        binding?.tvHeadlineError?.text = message
    }

    private fun hideHeadlineError() {
        binding?.llHeadlineError?.visibility = View.GONE
    }

    private fun showZipError(message: String?) {
        binding?.llZipError?.visibility = View.VISIBLE
        binding?.tvZipError?.text = message
    }

    private fun hideZipError() {
        binding?.llZipError?.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            selectedImageUri = data?.data
            selectedImageUri?.let {
                binding?.tvShortName?.visibility = View.GONE
                binding?.ivUserPhoto?.setImageURI(it)
                uploadUserAvatar(it)
            }
        }
    }

    private fun uploadUserAvatar(selectedImageUri: Uri) {
        val filePath = URIPathHelper().getPath(requireContext(), selectedImageUri)
        if (filePath.isNullOrEmpty()) {
            return
        }
        val file = File(filePath)
        if (!file.exists()) {
            return
        }
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multiPartBody =
            MultipartBody.Part.createFormData("avatar[image]", file.name, requestFile)
        viewModel.postUserAvatars(multiPartBody).observe(viewLifecycleOwner) {}
    }

    private fun selectImage() {
        val pickImageIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageIntent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        pickImageIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(pickImageIntent, Constants.IMAGE_PICK_CODE)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        selectImage()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showSuccessErrorLayout(true, getString(R.string.msg_runtime_permission))
    }
}