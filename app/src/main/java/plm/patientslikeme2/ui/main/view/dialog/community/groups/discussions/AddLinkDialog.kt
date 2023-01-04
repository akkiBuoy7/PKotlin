package plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.DialogAddLinkBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment

class AddLinkDialog(private val addLink: (title: String, link: String) -> Unit) : BaseBottomSheetFragment<DialogAddLinkBinding>() {

    override fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): DialogAddLinkBinding = DialogAddLinkBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {

        binding?.ivClose?.setOnClickListener {
            dismiss()
        }

        binding?.btnCancel?.setOnClickListener {
            dismiss()
        }

        binding?.btnSave?.setOnClickListener {
            val title = binding?.etTitle?.text?.toString()
            val link = binding?.etLink?.text?.toString()
            if (title.isNullOrEmpty().not() && link.isNullOrEmpty().not() && Patterns.WEB_URL.matcher(link).matches()) {
                addLink(title!!, link!!)
                dismiss()
            } else {
                showSuccessErrorLayout(binding?.llErrorSuccess, false, getString(R.string.error_link))
            }
        }
    }
}
