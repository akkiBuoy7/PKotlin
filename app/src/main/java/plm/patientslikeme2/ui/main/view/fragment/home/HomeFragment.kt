package plm.patientslikeme2.ui.main.view.fragment.home

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.community.groups.mygroups.MyGroup
import plm.patientslikeme2.data.model.home.AnnouncementModel
import plm.patientslikeme2.data.model.home.PromoCardModel
import plm.patientslikeme2.data.model.home.RemovePromoCard
import plm.patientslikeme2.databinding.FragmentHomeBinding
import plm.patientslikeme2.databinding.RowCommunityMyGroupsBinding
import plm.patientslikeme2.databinding.RowHomePromoCardBinding
import plm.patientslikeme2.ui.base.BaseActivity.Companion.featureFlag
import plm.patientslikeme2.ui.base.BaseFragment
import plm.patientslikeme2.ui.main.adapter.base.GenericHorizontalRecyclerBindingInterface
import plm.patientslikeme2.ui.main.adapter.base.GenericListAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericListHorizontalAdapter
import plm.patientslikeme2.ui.main.adapter.base.GenericRecyclerBindingInterface
import plm.patientslikeme2.ui.main.viewmodel.home.HomeViewModel
import plm.patientslikeme2.utils.AppAndroidUtils.getMobileThemeColor
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Preferences
import plm.patientslikeme2.utils.enum.ContextPromoCard
import plm.patientslikeme2.utils.extensions.openGroupDetails
import plm.patientslikeme2.utils.extensions.openGroupDiscussionDetails
import plm.patientslikeme2.utils.extensions.setHtmlString


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var promoCardAdapter: GenericListAdapter<PromoCardModel, RowHomePromoCardBinding>
    private lateinit var promoCardTwoAdapter: GenericListAdapter<PromoCardModel, RowHomePromoCardBinding>
    private lateinit var myGroupAdapter: GenericListHorizontalAdapter<MyGroup, RowCommunityMyGroupsBinding>

    private var dotsCount = 0
    private lateinit var dots: Array<ImageView?>
    private val maxDotCount = 7
    private var myGroupsSize = 0

    override fun onCreateBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        bindObservers()
    }

    private fun bindObservers() {
        viewModel.getMyGroupList().observe(viewLifecycleOwner) {
            when {
                it.status.isLoading() -> {
                    showProgress()
                }
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data?.myGroups?.size == 0) {
                        binding?.rvGroups?.visibility = View.GONE
                        binding?.llGroupDot?.visibility = View.GONE
                        binding?.layoutFindAGroup?.root?.visibility = View.VISIBLE
                        binding?.layoutFindAGroup?.tvUserName?.text =
                            getString(R.string.hi_username, Preferences.getValue(Constants.LOGIN_USER_NAME, ""))
                    } else {
                        myGroupAdapter.submitList(it.data?.data?.myGroups)
                        binding?.rvGroups?.visibility = View.VISIBLE
                        binding?.llGroupDot?.visibility = View.VISIBLE
                        binding?.layoutFindAGroup?.root?.visibility = View.GONE
                        updateMyGroupDots(it.data?.data?.myGroups)
                        myGroupsSize = it.data?.data?.myGroups?.size!!
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    binding?.rvGroups?.visibility = View.GONE
                }
            }
        }

        viewModel.getHomePromoCardList().observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data?.promo_cards?.size == 0) {
                        binding?.rvPromoCard?.visibility = View.GONE
                    } else {
                        it.data?.data?.promo_cards?.let { list ->
                            if (list.size > 2) {
                                promoCardAdapter.submitList(list.subList(0, 2))
                                promoCardTwoAdapter.submitList(list.subList(2, list.size))
                                if (featureFlag.android_mobile_promo) {
                                    binding?.rvPromoCardTwo?.visibility = View.VISIBLE
                                }
                            } else {
                                promoCardAdapter.submitList(it.data?.data?.promo_cards)
                            }
                        }
                        if (featureFlag.android_mobile_promo) {
                            binding?.rvPromoCard?.visibility = View.VISIBLE
                        }
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                    binding?.rvPromoCard?.visibility = View.GONE
                }
            }
        }

        viewModel.getHomeAnnouncements().observe(viewLifecycleOwner) {
            when {
                it.status.isSuccessful() -> {
                    hideProgress()
                    if (it.data?.data?.announcements?.size == 0) {
                        //No announcement
                    } else {
                        if (featureFlag.android_announcements) {
                            updateAnnouncementDialog(it.data?.data?.announcements?.get(0))
                        }
                    }
                }
                it.status.isError() -> {
                    hideProgress()
                }
            }
        }
    }

    private fun initView() {
        promoCardAdapter =
            GenericListAdapter(R.layout.row_home_promo_card, object : GenericRecyclerBindingInterface<RowHomePromoCardBinding, PromoCardModel> {
                override fun bindData(
                    binder: RowHomePromoCardBinding, model: PromoCardModel, position: Int
                ) {
                    binder.model = model
                    val cardColor = getMobileThemeColor(model.mobile_theme)
                    binder.cardView.setCardBackgroundColor(Color.parseColor(cardColor))
                    binder.btnDetails.setOnClickListener {
                        openPromoCardDetails(model)
                    }
                    binder.ivClose.setOnClickListener {
                        removePromoCardFromList(model)
                        removePromoCard(model)
                    }
                    binder.executePendingBindings()
                }
            })

        binding?.rvPromoCard?.adapter = promoCardAdapter

        promoCardTwoAdapter =
            GenericListAdapter(R.layout.row_home_promo_card, object : GenericRecyclerBindingInterface<RowHomePromoCardBinding, PromoCardModel> {
                override fun bindData(
                    binder: RowHomePromoCardBinding, model: PromoCardModel, position: Int
                ) {
                    binder.model = model
                    val cardColor = getMobileThemeColor(model.mobile_theme)
                    binder.cardView.setCardBackgroundColor(Color.parseColor(cardColor))
                    binder.btnDetails.setOnClickListener {
                        openPromoCardDetails(model)
                    }
                    binder.ivClose.setOnClickListener {
                        removePromoCardTwoFromList(model)
                        removePromoCard(model)
                    }
                    binder.executePendingBindings()
                }
            })

        binding?.rvPromoCardTwo?.adapter = promoCardTwoAdapter

        myGroupAdapter = GenericListHorizontalAdapter(
            R.layout.row_community_my_groups, object : GenericHorizontalRecyclerBindingInterface<RowCommunityMyGroupsBinding, MyGroup> {
                override fun bindData(
                    binder: RowCommunityMyGroupsBinding, model: MyGroup, position: Int
                ) {
                    binder.model = model
                    binder.root.setOnClickListener {
                        openGroupDetails(Constants.HOME_MY_GROUP, model.id.toString(), model.restricted)
                    }
                    binder.executePendingBindings()
                }
            }, (Resources.getSystem().displayMetrics.widthPixels * 0.8F)
        )

        binding?.rvGroups?.adapter = myGroupAdapter

        val snapHelper: LinearSnapHelper = object : LinearSnapHelper() {
            override fun findTargetSnapPosition(
                layoutManager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int
            ): Int {
                val centerView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
                val position = layoutManager.getPosition(centerView)
                var targetPosition = -1
                if (layoutManager.canScrollHorizontally()) {
                    targetPosition = if (velocityX < 0) {
                        position - 1
                    } else {
                        position + 1
                    }
                }
                if (layoutManager.canScrollVertically()) {
                    targetPosition = if (velocityY < 0) {
                        position - 1
                    } else {
                        position + 1
                    }
                }
                val firstItem = 0
                val lastItem = layoutManager.itemCount - 1
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem))
                updateCurrentActiveDot(targetPosition)
                return targetPosition
            }
        }
        snapHelper.attachToRecyclerView(binding?.rvGroups)

        if (featureFlag.android_mobile_promo) {
            binding?.rvPromoCard?.visibility = View.GONE
            binding?.rvPromoCardTwo?.visibility = View.GONE
        }

        binding?.layoutFindAGroup?.btnFindAGroup?.setOnClickListener {
            findNavController().navigate(R.id.navigation_home_to_community_all_groups)
        }
    }

    private fun openPromoCardDetails(model: PromoCardModel) {
        when (model.context.name) {
            ContextPromoCard.group_overview.name -> {
                openGroupDetails(
                    Constants.HOME_PROMO_CARD_GROUP_OVERVIEW, model.context.group_id.toString(), false
                )
            }
            ContextPromoCard.show_discussion.name -> {
                openGroupDiscussionDetails(
                    Constants.HOME_PROMO_CARD_GROUP_DISCUSSION, model.context.discussion_id.toString(), isMember = false
                )
            }
        }
    }

    private fun removePromoCard(model: PromoCardModel) {
        viewModel.postExcludePromoCard(RemovePromoCard(model.id)).observe(viewLifecycleOwner) {
            if (isAdded) {
                hideProgress()
            }
        }
    }

    private fun addPromoCardFromList(item: PromoCardModel) {
        val itemList: ArrayList<PromoCardModel> = arrayListOf()
        itemList.addAll(promoCardAdapter.currentList)
        itemList.add(item)
        promoCardAdapter.submitList(itemList)
        promoCardAdapter.notifyDataSetChanged()
    }

    private fun removePromoCardFromList(item: PromoCardModel) {
        val itemList: ArrayList<PromoCardModel> = arrayListOf()
        itemList.addAll(promoCardAdapter.currentList)
        itemList.remove(item)
        promoCardAdapter.submitList(itemList)
        promoCardAdapter.notifyDataSetChanged()

        updatePromoCardBothList()
    }

    private fun updatePromoCardBothList() {
        if (promoCardTwoAdapter.itemCount > 0) {
            Handler(Looper.getMainLooper()).postDelayed({
                val itemList: ArrayList<PromoCardModel> = arrayListOf()
                itemList.addAll(promoCardTwoAdapter.currentList)
                val firstItem = itemList[0]
                addPromoCardFromList(firstItem)
                removePromoCardTwoFromList(firstItem)
            }, 200)
        }
    }

    private fun removePromoCardTwoFromList(item: PromoCardModel) {
        val itemList: ArrayList<PromoCardModel> = arrayListOf()
        itemList.addAll(promoCardTwoAdapter.currentList)
        itemList.remove(item)
        promoCardTwoAdapter.submitList(itemList)
        promoCardTwoAdapter.notifyDataSetChanged()
    }

    private fun updateAnnouncementDialog(model: AnnouncementModel?) {
        binding?.announcement?.tvMessage?.setHtmlString(model?.message)

        binding?.announcement?.tvDismiss?.setOnClickListener {
            binding?.announcement?.cardView?.visibility = View.GONE
            callDismissAnnouncementAPI()
        }
        binding?.announcement?.cardView?.visibility = View.VISIBLE
    }

    private fun callDismissAnnouncementAPI() {
        viewModel.updateDismissAnnouncements().observe(viewLifecycleOwner) {
            if (isAdded) {
                hideProgress()
            }
        }
    }

    private fun updateCurrentActiveDot(position: Int) {

        if (dots.size >= maxDotCount) {
            updateMyGroupDotsMoreThanMaxDots(position)
        } else {
            for ((index, _) in dots.withIndex()) {
                if (index == position) {
                    dots[index]?.setImageResource(R.drawable.ic_dot_active)
                } else {
                    dots[index]?.setImageResource(R.drawable.ic_dot_inactive)
                }
            }
        }
    }

    private fun updateMyGroupDotsMoreThanMaxDots(position: Int) {

        if (position in 0 until 4) {
            for ((index, _) in dots.withIndex()) {
                if (index == position) {
                    dots[index]?.setImageResource(R.drawable.ic_dot_active)
                } else {
                    dots[index]?.setImageResource(R.drawable.ic_dot_inactive)
                }
            }
            dots[dotsCount - 1]?.setImageResource(R.drawable.ic_dot_fade)
            dots[0]?.setImageResource(R.drawable.ic_dot_fade)

            if (position == 0) {
                dots[dotsCount - 1]?.setImageResource(R.drawable.ic_dot_fade)
                dots[0]?.setImageResource(R.drawable.ic_dot_active)
            }
        }

        if (position == myGroupsSize - 2) {
            for ((index, _) in dots.withIndex()) {
                dots[index]?.setImageResource(R.drawable.ic_dot_inactive)
            }
            dots[dotsCount - 1]?.setImageResource(R.drawable.ic_dot_fade)
            dots[0]?.setImageResource(R.drawable.ic_dot_fade)
            dots[dotsCount - 2]?.setImageResource(R.drawable.ic_dot_active)
        }

        if (position == myGroupsSize - 3) {
            for ((index, _) in dots.withIndex()) {
                dots[index]?.setImageResource(R.drawable.ic_dot_inactive)
            }
            dots[dotsCount - 1]?.setImageResource(R.drawable.ic_dot_fade)
            dots[0]?.setImageResource(R.drawable.ic_dot_fade)
            dots[dotsCount - 3]?.setImageResource(R.drawable.ic_dot_active)
        }

        if (position == myGroupsSize - 4) {
            for ((index, _) in dots.withIndex()) {
                dots[index]?.setImageResource(R.drawable.ic_dot_inactive)
            }
            dots[dotsCount - 1]?.setImageResource(R.drawable.ic_dot_fade)
            dots[0]?.setImageResource(R.drawable.ic_dot_fade)
            dots[dotsCount - 4]?.setImageResource(R.drawable.ic_dot_active)
        }

        if (position == myGroupsSize - 1) {
            for ((index, _) in dots.withIndex()) {
                dots[index]?.setImageResource(R.drawable.ic_dot_inactive)
            }
            dots[dotsCount - 1]?.setImageResource(R.drawable.ic_dot_active)
            dots[0]?.setImageResource(R.drawable.ic_dot_fade)
        }
    }

    private fun updateMyGroupDots(myGroups: List<MyGroup>?) {
        myGroups?.let {
            if (it.size >= maxDotCount) {
                dotsCount = maxDotCount
            } else {
                dotsCount = it.size
            }
            dots = arrayOfNulls(dotsCount)
            for (i in 0 until dotsCount) {
                dots[i] = ImageView(activity)
                dots[i]?.setImageResource(R.drawable.ic_dot_inactive)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(5, 0, 5, 0)
                binding?.llGroupDot?.addView(dots[i], params)
            }
            if (it.size >= maxDotCount) {
                dots[0]?.setImageResource(R.drawable.ic_dot_active)
                dots[dotsCount - 1]?.setImageResource(R.drawable.ic_dot_fade)
            } else {
                dots[0]?.setImageResource(R.drawable.ic_dot_active)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar(getString(R.string.empty), false)
    }
}