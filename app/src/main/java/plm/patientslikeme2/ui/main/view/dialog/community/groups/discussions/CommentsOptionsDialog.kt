package plm.patientslikeme2.ui.main.view.dialog.community.groups.discussions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import plm.patientslikeme2.databinding.DialogDiscussionOptionBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.viewmodel.community.groups.GroupViewModel

class CommentsOptionsDialog(private val discussionId: Int, private val userId: Int, private val shouldRefresh: () -> Unit) : BaseBottomSheetFragment<DialogDiscussionOptionBinding>() {

    private val viewModel: GroupViewModel by activityViewModels()

    override fun onCreateBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): DialogDiscussionOptionBinding =
        DialogDiscussionOptionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()

    }

    private fun setListeners() {
        binding?.ivClose?.setOnClickListener {
            dismiss()
        }

        binding?.tvBlockMember?.setOnClickListener {
            viewModel.blockUser(userId).observe(viewLifecycleOwner) {
                when {
                    it.status.isLoading() -> {
                        showProgress()
                    }
                    it.status.isError() -> {
                        hideProgress()
                    }
                    it.status.isSuccessful() -> {
                        hideProgress()
                        dismiss()
                        shouldRefresh()
                    }
                }
            }

        }

        binding?.tvFlag?.setOnClickListener {
            viewModel.flagCommentReply(discussionId).observe(viewLifecycleOwner) {
                when {
                    it.status.isLoading() -> {
                        showProgress()
                    }
                    it.status.isError() -> {
                        hideProgress()
                    }
                    it.status.isSuccessful() -> {
                        hideProgress()
                        dismiss()
                        shouldRefresh()
                    }
                }
            }
        }
    }
}