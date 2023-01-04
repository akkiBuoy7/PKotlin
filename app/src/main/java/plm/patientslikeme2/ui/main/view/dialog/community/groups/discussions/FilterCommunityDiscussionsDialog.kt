package plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import plm.patientslikeme2.databinding.DialogCommunityDiscussionFilterBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.viewmodel.community.groups.GroupViewModel
import plm.patientslikeme2.utils.Constants

class FilterCommunityDiscussionsDialog(private val applyFilter:() -> Unit) : BaseBottomSheetFragment<DialogCommunityDiscussionFilterBinding>() {

    private val viewModel: GroupViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): DialogCommunityDiscussionFilterBinding = DialogCommunityDiscussionFilterBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        setListeners()
    }

    private fun initView() {
        binding?.cbAll?.isChecked = (viewModel.groupDiscussionsFilterTagsConditions.not() && viewModel.groupDiscussionsFilterMyFollowings.not())
        binding?.cbMyFollowings?.isChecked = viewModel.groupDiscussionsFilterMyFollowings
        binding?.cbTagFollowings?.isChecked = viewModel.groupDiscussionsFilterTagsConditions

        binding?.radioMostHelpful?.isChecked = viewModel.groupDiscussionsSortBy == Constants.MOST_HELPFUL
        binding?.radioRecent?.isChecked = viewModel.groupDiscussionsSortBy == Constants.RECENT_ACTIVITY

    }

    private fun setListeners() {

        binding?.cbAll?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding?.cbMyFollowings?.isChecked = isChecked.not()
                binding?.cbTagFollowings?.isChecked = isChecked.not()
            }
        }

        binding?.cbMyFollowings?.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.groupDiscussionsFilterMyFollowings = isChecked
            if(isChecked){
                binding?.cbAll?.isChecked = false
            }
        }

        binding?.cbTagFollowings?.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.groupDiscussionsFilterTagsConditions = isChecked
            if(isChecked){
                binding?.cbAll?.isChecked = false
            }
        }

        binding?.radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding?.radioMostHelpful?.id -> {
                    viewModel.groupDiscussionsSortBy = Constants.MOST_HELPFUL
                }
                binding?.radioRecent?.id -> {
                    viewModel.groupDiscussionsSortBy = Constants.RECENT_ACTIVITY
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