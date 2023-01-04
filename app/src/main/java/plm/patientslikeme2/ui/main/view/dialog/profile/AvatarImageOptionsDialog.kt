package plm.patientslikeme2.ui.main.view.dialog.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.DialogAvatarImageOptionsBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment

class AvatarImageOptionsDialog : BaseBottomSheetFragment<DialogAvatarImageOptionsBinding>() {

    companion object {
        fun newInstance(): AvatarImageOptionsDialog {
            return AvatarImageOptionsDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): DialogAvatarImageOptionsBinding =
        DialogAvatarImageOptionsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.handlers = this
    }

    fun onItemClicked(view: View?) {
        if (!isAdded) {
            return
        }
        when (view?.id) {
            R.id.iv_close -> dialog?.dismiss()
            R.id.tv_gallery -> {
                (parentFragment as UpdatePersonalInformationDialog).showPickerDialog()
                dialog?.dismiss()
            }
            R.id.tv_remove_photo -> {
                (parentFragment as UpdatePersonalInformationDialog).removeUserAvatars()
                dialog?.dismiss()
            }
            R.id.tv_cancel -> dialog?.dismiss()
        }
    }
}