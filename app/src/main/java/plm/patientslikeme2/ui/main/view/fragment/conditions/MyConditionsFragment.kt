package plm.patientslikeme2.ui.main.view.fragment.conditions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.action.ErrorResponse
import plm.patientslikeme2.data.model.conditions.AddConditionRequest
import plm.patientslikeme2.data.model.conditions.ConditionInfo
import plm.patientslikeme2.data.model.conditions.ConditionInfosItem
import plm.patientslikeme2.databinding.FragmentMyConditionsBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.databinding.RowMyConditionBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.view.dialog.conditions.AddConditionDialog
import plm.patientslikeme2.ui.main.viewmodel.conditions.ConditionsViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.fromJson

@AndroidEntryPoint
class MyConditionsFragment : BaseFragment<FragmentMyConditionsBinding>() {

    private val viewModel: ConditionsViewModel by activityViewModels()
    private lateinit var myConditionsAdapter: GenericListAdapter<ConditionInfosItem, RowMyConditionBinding>

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentMyConditionsBinding = FragmentMyConditionsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.navigation_my_conditions
        initView()
        if (isConnected()) bindObservers() else showEmptyView(getString(R.string.error_internet))
    }

    private fun initView() {
        toolbarBinding?.btnAction?.setOnClickListener {
            AddConditionDialog.newInstance(ConditionInfo(), Constants.ADD)
                .show(childFragmentManager, getString(R.string.add_a_conditions))
        }

        myConditionsAdapter = GenericListAdapter(
            R.layout.row_my_condition,
            object : GenericRecyclerBindingInterface<RowMyConditionBinding, ConditionInfosItem> {
                override fun bindData(
                    binder: RowMyConditionBinding, model: ConditionInfosItem, position: Int
                ) {
                    binder.model = model
                    binder.root.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putString(Constants.CONDITION_ID, model.id.toString())
                        bundle.putString(Constants.CONDITION_NAME, model.name)
                        findNavController().navigate(
                            R.id.action_my_conditions_to_condition_details,
                            bundle
                        )
                    }
                    binder.executePendingBindings()
                }

            })

        binding?.rvMyConditions?.adapter = myConditionsAdapter
    }

    private fun bindObservers() {
        viewModel.getMyConditions().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data?.conditionInfos?.size!! > 0) {
                        myConditionsAdapter.submitList(it.data?.data!!.conditionInfos)
                        binding?.rvMyConditions?.visibility = View.VISIBLE
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


    fun addCondition(model: AddConditionRequest) {
        viewModel.postAddCondition(model).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.success == true) {
                        it.data?.data?.let { bindObservers() }
                        showSuccessErrorLayout(
                            it.data?.success!!, it.data?.message.toString()
                        )
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    val error =
                        Gson().fromJson<ErrorResponse>(it.errorMessage.toString()) as ErrorResponse
                    showSuccessErrorLayout(error.success!!, error.message.toString())
                }
            }
        }
    }

    fun addItemInList(model: ConditionInfosItem) {
        val list: MutableList<ConditionInfosItem> = mutableListOf()
        list.addAll(myConditionsAdapter.currentList)
        list.add(model)
        myConditionsAdapter.submitList(list)
    }

    private fun showEmptyView(error: String) {
        if (binding?.vsNoResult?.viewStub?.parent != null) {
            binding?.vsNoResult?.setOnInflateListener { _, _ ->
                val binding = binding?.vsNoResult?.binding as LayoutNoResultBinding
                binding.tvMessage.text = error
                binding.clEmpty.visibility = View.VISIBLE
            }
            binding?.vsNoResult?.viewStub?.inflate()
            binding?.rvMyConditions?.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(getString(R.string.my_conditions), false, true, true, true, "Add")
    }
}