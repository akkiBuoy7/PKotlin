package plm.patientslikeme2.ui.main.view.fragment.community.events

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.community.events.EventModel
import plm.patientslikeme2.databinding.FragmentEventDetailsBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.viewmodel.community.events.EventViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.loadModeratorImage
import plm.patientslikeme2.utils.extensions.openHyperLinks

@AndroidEntryPoint
class EventDetailsFragment : BaseFragment<FragmentEventDetailsBinding>() {

    private lateinit var eventModel: EventModel
    private val viewModel: EventViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentEventDetailsBinding =
        FragmentEventDetailsBinding.inflate(inflater, container, false)

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        args?.let {
            val event = args.getString(Constants.MODEL).toString()
            eventModel = Gson().fromJson(event, EventModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.event_details

        if (isConnected()) {
            bindObservers()
        } else {
            showEmptyView(getString(R.string.error_internet))
        }
        initView()
    }

    private fun initView() {
        if (TextUtils.isEmpty(eventModel.destination_url)) {
            binding?.btnJoinEvent?.visibility = View.GONE
        } else {
            binding?.btnJoinEvent?.visibility = View.VISIBLE
        }

        binding?.btnJoinEvent?.setOnClickListener {
            binding?.btnJoinEvent?.openHyperLinks(eventModel.destination_url)
        }
    }

    private fun bindObservers() {
        viewModel.getEventOverview(eventModel.id.toString()).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                    binding?.parent?.visibility = View.GONE
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data?.event?.title.isNullOrEmpty()) {
                        showEmptyView(getString(R.string.no_events_found))
                    } else {
                        binding?.model = it.data?.data?.event
                        if (!TextUtils.isEmpty(it.data?.data?.event?.moderator?.login)) {
                            it.data?.data?.event?.moderator?.let { it1 ->
                                binding?.ivModerator?.loadModeratorImage(
                                    it1
                                )
                            }
                        } else {
                            binding?.tvModerate?.visibility = View.GONE
                            binding?.llModerate?.visibility = View.GONE
                        }
                        binding?.executePendingBindings()
                        binding?.parent?.visibility = View.VISIBLE
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    binding?.parent?.visibility = View.GONE
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
            binding?.parent?.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(eventModel.event_type, showBackArrow = true, hideRightIcon = true)
    }
}