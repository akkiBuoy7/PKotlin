package plm.patientslikeme2.ui.main.view.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.profile.Conditions
import plm.patientslikeme2.databinding.FragmentMembersConditionsBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowMemberConditionsBinding
import plm.patientslikeme2.databinding.RowSeverityBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.viewmodel.profile.ProfileViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.getUpperFirstChar
import plm.patientslikeme2.utils.extensions.getsSymptomSeverity
import plm.patientslikeme2.utils.extensions.getsTreatmentSeverity

@AndroidEntryPoint
class MemberConditionsFragment : BaseFragment<FragmentMembersConditionsBinding>() {

    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var memberConditionsAdapter: GenericListAdapter<Conditions, RowMemberConditionsBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMembersConditionsBinding =
        FragmentMembersConditionsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.user_profile

        initViews()
        if (isConnected()) bindMemberConditionsObserver() else showEmptyView(getString(R.string.error_internet))
    }

    private fun initViews() {
        memberConditionsAdapter = GenericListAdapter(R.layout.row_member_conditions, object :
            GenericRecyclerBindingInterface<RowMemberConditionsBinding, Conditions> {
            override fun bindData(
                binder: RowMemberConditionsBinding,
                model: Conditions,
                position: Int
            ) {
                binder.model = model

                binder.tvHeading.setOnClickListener {
                    if (binder.clMain.isShown) {
                        binder.clMain.visibility = View.GONE
                        binder.tvHeading.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_arrow_down, 0
                        )
                    } else {
                        binder.clMain.visibility = View.VISIBLE
                        binder.tvHeading.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0, R.drawable.ic_arrow_up_small, 0
                        )
                    }
                }

                binder.rvSymptoms.removeAllViews()
                val symptoms = model.symptoms
                if (symptoms != null) {
                    for (symptom in symptoms) {
                        val symptomBinding = RowSeverityBinding.inflate(
                            LayoutInflater.from(context), binder.clMain, false
                        )
                        symptomBinding.tvName.getUpperFirstChar(symptom.name.toString())
                        symptomBinding.tvSeverity.text =
                            if (symptom.severity != null) symptom.severity else "None"

                        if (symptom.severity != null)
                            symptomBinding.ivSeverity.setImageDrawable(symptom.severity?.let {
                                getsSymptomSeverity(it, symptomBinding.tvSeverity.context)
                            })
                        binder.rvSymptoms.addView(symptomBinding.root)
                    }
                }

                binder.rvMyTreatments.removeAllViews()
                val treatments = model.treatments
                if (treatments != null) {
                    for (treatment in treatments) {
                        val treatmentBinding = RowSeverityBinding.inflate(
                            LayoutInflater.from(context), binder.clMain, false
                        )
                        treatmentBinding.tvName.getUpperFirstChar(treatment.name.toString())
                        treatmentBinding.tvSeverity.text =
                            if (treatment.effectiveness != null) treatment.effectiveness else Constants.MODERATE

                        treatmentBinding.ivSeverity.setImageDrawable(activity?.let {
                            ContextCompat.getDrawable(it, R.drawable.ic_moderate_green)
                        })
                        if (treatment.effectiveness != null)
                            treatmentBinding.ivSeverity.setImageDrawable(treatment.effectiveness?.let {
                                getsTreatmentSeverity(it, treatmentBinding.tvSeverity.context)
                            })
                        binder.rvMyTreatments.addView(treatmentBinding.root)
                    }
                }
                binder.executePendingBindings()
            }
        })
        binding?.rvConditions?.adapter = memberConditionsAdapter
    }

    private fun bindMemberConditionsObserver() {
        viewModel.getMemberConditions().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data?.conditions != null) {
                        memberConditionsAdapter.submitList(it?.data?.data?.conditions)
                    } else {
                        showEmptyView(getString(R.string.error_result))
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    showEmptyView(getString(R.string.error_api))
                }
            }
        }
    }

    private fun showEmptyView(error: String) {
        if (binding?.vsNoResult?.viewStub?.parent != null) {
            binding?.vsNoResult?.setOnInflateListener { _, _ ->
                val binding = binding?.vsNoResult?.binding as LayoutNoResultBinding
                binding.tvMessage.text = error
                binding.clEmpty.visibility = View.VISIBLE
            }
            binding?.vsNoResult?.viewStub?.inflate()
            binding?.rvConditions?.visibility = View.GONE
        }
    }
}