package plm.patientslikeme2.ui.main.view.dialog.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.databinding.DialogMessagingMoreOptionBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.view.fragment.messages.MessageConversationFragment
import plm.patientslikeme2.ui.main.viewmodel.messages.MessagesViewModel
import plm.patientslikeme2.utils.Constants

class MessagingMoreOptionDialog : BaseBottomSheetFragment<DialogMessagingMoreOptionBinding>() {

    private val viewModel: MessagesViewModel by activityViewModels()

    companion object {
        fun newInstance(): MessagingMoreOptionDialog {
            return MessagingMoreOptionDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): DialogMessagingMoreOptionBinding =
        DialogMessagingMoreOptionBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.handlers = this
        initViews()
    }

    private fun initViews() {
        if (viewModel.optionCallFrom == Constants.OTHERS_MESSAGE) {
            binding?.tvBlockUser?.visibility = View.GONE
            binding?.dividerLine1?.visibility = View.GONE
        }
        if (viewModel.optionCallFrom == Constants.WHOLE_THREAD) {
            binding?.tvReportAsSpam?.visibility = View.GONE
            binding?.dividerLine1?.visibility = View.GONE
        }
    }

    fun onItemClicked(view: View?) {
        if (!isAdded) {
            return
        }
        when (view?.id) {
            R.id.iv_close -> dialog?.dismiss()
            R.id.tv_block_user -> blockMemberObservers()
            R.id.tv_report_as_spam -> reportAsSpamObservers()
        }
    }

    private fun blockMemberObservers() {
        viewModel.blockMember().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    dialog?.dismiss()
                    (parentFragment as MessageConversationFragment).showSuccessErrorView(
                        it.data?.message, it.data?.success
                    )
                }
                it.status.isError() -> {
                    hideProgress()
                    dialog?.dismiss()
                    val errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    (parentFragment as MessageConversationFragment).showSuccessErrorView(
                        errorMessage, false
                    )
                }
            }
        }
    }

    private fun reportAsSpamObservers() {
        viewModel.reportAsSpam().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    dialog?.dismiss()
                    (parentFragment as MessageConversationFragment).showSuccessErrorView(
                        it.data?.message, it.data?.success
                    )
                }
                it.status.isError() -> {
                    hideProgress()
                    dialog?.dismiss()
                    val errorMessage =
                        ErrorResponse().stringToObject(it.errorMessage).message.toString()
                    (parentFragment as MessageConversationFragment).showSuccessErrorView(
                        errorMessage, false
                    )
                }
            }
        }
    }
}