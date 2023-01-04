package plm.patientslikeme2.ui.main.view.dialog.community.groups.members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import plm.patientslikeme2.databinding.DialogCommunityMemberFilterBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.viewmodel.community.groups.GroupViewModel
import plm.patientslikeme2.utils.Constants

class FilterGroupMembersDialog(private val applyFilter: () -> Unit) : BaseBottomSheetFragment<DialogCommunityMemberFilterBinding>() {

    private val viewModel: GroupViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): DialogCommunityMemberFilterBinding = DialogCommunityMemberFilterBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        setListeners()
    }

    private fun initView() {
        binding?.cbAll?.isChecked = (viewModel.allMemberFilterMyConditions.not() && viewModel.allMemberFilterMyFollowings.not())
        binding?.cbMyConditions?.isChecked = viewModel.allMemberFilterMyConditions
        binding?.cbFollowing?.isChecked = viewModel.allMemberFilterMyFollowings

        binding?.radioNewest?.isChecked = viewModel.allMemberSortBy == "newest"
        binding?.radioRecent?.isChecked = viewModel.allMemberSortBy == "recent_activity"

    }

    private fun setListeners() {

        binding?.cbAll?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding?.cbFollowing?.isChecked = isChecked.not()
                binding?.cbMyConditions?.isChecked = isChecked.not()
            }
        }

        binding?.cbFollowing?.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.allMemberFilterMyFollowings = isChecked
            if (isChecked) {
                binding?.cbAll?.isChecked = false
            }
        }

        binding?.cbMyConditions?.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.allMemberFilterMyConditions = isChecked
            if (isChecked) {
                binding?.cbAll?.isChecked = false
            }
        }

        binding?.radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding?.radioNewest?.id -> {
                    viewModel.allMemberSortBy = Constants.NEWEST
                }
                binding?.radioRecent?.id -> {
                    viewModel.allMemberSortBy = Constants.RECENT_ACTIVITY
                }
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