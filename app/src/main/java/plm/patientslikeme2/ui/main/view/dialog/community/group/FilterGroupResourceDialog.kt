package plm.patientslikeme2.ui.main.view.dialog.community.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import plm.patientslikeme2.databinding.DialogGroupResourceFilterBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.viewmodel.community.groups.GroupViewModel

class FilterGroupResourceDialog(private val applyFilter: () -> Unit) :
    BaseBottomSheetFragment<DialogGroupResourceFilterBinding>() {

    private val viewModel: GroupViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): DialogGroupResourceFilterBinding =
        DialogGroupResourceFilterBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        setListeners()
    }

    private fun initView() {
        binding?.cbAll?.isChecked = viewModel.filterAllResources
        binding?.cbFollowing?.isChecked = viewModel.filterResourcesImFollowing
    }

    private fun setListeners() {
        binding?.cbAll?.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.filterAllResources = isChecked
            if (isChecked) {
                binding?.cbFollowing?.isChecked = isChecked.not()
            }
        }

        binding?.cbFollowing?.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.filterResourcesImFollowing = isChecked
            if (isChecked) {
                binding?.cbAll?.isChecked = false
            }
        }

        binding?.ivClose?.setOnClickListener {
            dismiss()
        }

        binding?.btnApply?.setOnClickListener {
            applyFilter()
            dismiss()
        }
    }
}