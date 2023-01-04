package plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import plm.patientslikeme2.databinding.DialogCommunityDiscussionCommentsSortBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.viewmodel.community.groups.DiscussionThreadViewModel
import plm.patientslikeme2.utils.Constants

class SortCommentsDialog(private val applyFilter:() -> Unit) : BaseBottomSheetFragment<DialogCommunityDiscussionCommentsSortBinding>() {

    private val viewModel: DiscussionThreadViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): DialogCommunityDiscussionCommentsSortBinding = DialogCommunityDiscussionCommentsSortBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        setListeners()
    }

    private fun initView() {
        binding?.radioMostHelpful?.isChecked = viewModel.groupDiscussionsSortBy == Constants.MOST_HELPFUL
        binding?.radioRecent?.isChecked = viewModel.groupDiscussionsSortBy == Constants.RECENT_ACTIVITY
        binding?.radioOldest?.isChecked = viewModel.groupDiscussionsSortBy != Constants.RECENT_ACTIVITY && viewModel.groupDiscussionsSortBy != Constants.MOST_HELPFUL
    }

    private fun setListeners() {

        binding?.radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding?.radioMostHelpful?.id -> {
                    viewModel.groupDiscussionsSortBy = Constants.MOST_HELPFUL
                }
                binding?.radioRecent?.id -> {
                    viewModel.groupDiscussionsSortBy = Constants.RECENT_ACTIVITY
                }
                binding?.radioOldest?.id -> {
                    viewModel.groupDiscussionsSortBy = Constants.EMPTY
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