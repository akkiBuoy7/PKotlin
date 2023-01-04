package plm.patientslikeme2.ui.main.view.dialog.community.members

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.community.members.Users
import plm.patientslikeme2.databinding.DialogCommunityMemberSearchBinding
import plm.patientslikeme2.ui.base.BaseBottomSheetFragment
import plm.patientslikeme2.ui.main.adapter.community.UserAutoCompleteAdapter
import plm.patientslikeme2.ui.main.viewmodel.community.CommunityViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.OnAutoCompleteItemClickListener
import plm.patientslikeme2.utils.extensions.openUserProfile

class SearchCommunityMembersDialog : BaseBottomSheetFragment<DialogCommunityMemberSearchBinding>() {

    private val viewModel: CommunityViewModel by activityViewModels()
    private val onItemClick = object : OnAutoCompleteItemClickListener {
        override fun onItemClick(item: Any?) {
            dismiss()
            openUserProfile(Constants.COMMUNITY_MEMBER, (item as Users).id.toString())
        }
    }

    var adapter: UserAutoCompleteAdapter? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                it.setBackgroundResource(R.color.bottom_sheet_translucent_bg)
            }
        }
        return dialog
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): DialogCommunityMemberSearchBinding = DialogCommunityMemberSearchBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        binding?.ivClose?.setOnClickListener {
            dismiss()
        }

        adapter = UserAutoCompleteAdapter(requireContext(), R.layout.row_user_drop_down, onItemClick)
        binding?.acSearchMember?.setAdapter(adapter)
        binding?.acSearchMember?.setDropDownBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bg_autocomplete_dropdown))
        binding?.acSearchMember?.addTextChangedListener(object : TextWatcher {
            private val handler = Handler(Looper.getMainLooper())
            private val apiRunnable = Runnable {
                binding?.acSearchMember?.text?.let{
                    if(it.isNotEmpty()){
                        searchUsers(it.toString())
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val sleep = when(s.length) {
                    1 -> 1000L
                    2,3 -> 700L
                    4,5 -> 500L
                    else -> 300L
                }
                handler.postDelayed(apiRunnable,sleep)
            }
        })
    }

    private fun searchUsers(query: String) {
        viewModel.searchUsers(query).observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    if (it.data?.success == true) {
                        it.data?.data?.users?.let{
                            adapter?.setData(it)
                            adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }
}