package plm.patientslikeme2.ui.main.view.fragment.community.resource

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.FragmentResourceDetailsBinding
import plm.patientslikeme2.databinding.LayoutNoResultBinding
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.viewmodel.community.resource.ResourceViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.extensions.loadImage


@AndroidEntryPoint
class ResourceDetailsFragment : BaseFragment<FragmentResourceDetailsBinding>() {

    private lateinit var resourceId: String
    private lateinit var resourceName: String
    private val viewModel: ResourceViewModel by activityViewModels()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentResourceDetailsBinding =
        FragmentResourceDetailsBinding.inflate(inflater, container, false)

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        args?.let {
            resourceId = args.getInt(Constants.ID).toString()
            resourceName = args.getString(Constants.NAME).toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentFragment = R.id.resource_details

        if (isConnected()) {
            bindObservers()
        } else {
            showEmptyView(getString(R.string.error_internet))
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun bindObservers() {
        viewModel.getResourceOverview(resourceId).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                    binding?.parent?.visibility = View.GONE
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data?.resource?.headline.isNullOrEmpty()) {
                        showEmptyView(getString(R.string.no_resources_found))
                    } else {
                        val item = it.data?.data?.resource
                        binding?.model = item
                        if (!TextUtils.isEmpty(item?.user?.name)) {
                            binding?.ivModerator?.loadImage(item?.user?.name, item?.user?.avatar)
                            if (!item?.user?.badges.isNullOrEmpty()) {
                                binding?.tvBadges?.text = item?.user?.badges?.get(0)?.badge
                                binding?.tvBadges?.visibility = View.VISIBLE
                            }
                            binding?.llModerate?.visibility = View.VISIBLE
                        } else {
                            binding?.llModerate?.visibility = View.GONE
                        }
                        if (TextUtils.isEmpty(item?.resource_url)) {
                            binding?.webview?.visibility = View.GONE
                        } else {
                            binding?.webview?.visibility = View.VISIBLE
                            val webViewSettings = binding?.webview?.settings
                            webViewSettings?.javaScriptCanOpenWindowsAutomatically = true
                            webViewSettings?.javaScriptEnabled = true
                            binding?.webview?.loadData(
                                item?.resource_url.toString(),
                                "text/html",
                                "utf-8"
                            )
                        }
                        if (TextUtils.isEmpty(item?.resource_url) && TextUtils.isEmpty(item?.promo_image)) {
                            binding?.llImage?.visibility = View.GONE
                        }
                        updateBookmarkIcon(it.data?.data?.resource?.followed)
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

    private fun callFollowResourceAPI() {
        viewModel.postFollowResource(resourceId).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    showSuccessErrorLayout(true, it.data?.message.toString())
                    updateBookmarkIcon(true)
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }

    private fun callUnFollowResourceAPI() {
        viewModel.postUnFollowResource(resourceId).observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    showSuccessErrorLayout(true, it.data?.message.toString())
                    updateBookmarkIcon(false)
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }

    private fun updateBookmarkIcon(followed: Boolean?) {
        followed?.let { follow ->
            if (follow) {
                toolbarBinding?.ivClose?.setImageResource(R.drawable.ic_bookmark_added)
            } else {
                toolbarBinding?.ivClose?.setImageResource(R.drawable.ic_bookmark_add)
            }
            toolbarBinding?.ivMessaging?.visibility = View.GONE
            toolbarBinding?.ivNotification?.visibility = View.GONE
            toolbarBinding?.ivClose?.visibility = View.VISIBLE
            toolbarBinding?.llRight?.visibility = View.VISIBLE

            toolbarBinding?.ivClose?.setOnClickListener {
                if (follow) {
                    callUnFollowResourceAPI()
                } else {
                    callFollowResourceAPI()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        removeOptionMoreIcon()
    }

    override fun onStop() {
        super.onStop()
        removeOptionMoreIcon()
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
        updateToolbar(resourceName, showBackArrow = true, hideRightIcon = true)
    }
}