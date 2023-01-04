package plm.patientslikeme2.utils.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import plm.patientslikeme2.R
import plm.patientslikeme2.data.model.Discussions
import plm.patientslikeme2.ui.main.view.dialog.messages.NewMessageDialog
import plm.patientslikeme2.ui.main.viewmodel.messages.MessagesViewModel
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.Constants.ABOUT_GROUP
import plm.patientslikeme2.utils.Constants.COMMUNITY_ALL_GROUP
import plm.patientslikeme2.utils.Constants.COMMUNITY_GROUP
import plm.patientslikeme2.utils.Constants.COMMUNITY_MEMBER
import plm.patientslikeme2.utils.Constants.DISCUSSION_COMMENT
import plm.patientslikeme2.utils.Constants.DISCUSSION_REPLY
import plm.patientslikeme2.utils.Constants.FOLLOWING
import plm.patientslikeme2.utils.Constants.FOLLOWING_DISCUSSION
import plm.patientslikeme2.utils.Constants.FOLLOWING_MEMBER
import plm.patientslikeme2.utils.Constants.FOLLOWING_TAG
import plm.patientslikeme2.utils.Constants.GROUP_DISCUSSIONS
import plm.patientslikeme2.utils.Constants.GROUP_DISCUSSIONS_DETAILS
import plm.patientslikeme2.utils.Constants.GROUP_MEMBER
import plm.patientslikeme2.utils.Constants.HOME_MY_GROUP
import plm.patientslikeme2.utils.Constants.HOME_PROMO_CARD_GROUP_DISCUSSION
import plm.patientslikeme2.utils.Constants.HOME_PROMO_CARD_GROUP_OVERVIEW
import plm.patientslikeme2.utils.Constants.ID
import plm.patientslikeme2.utils.Constants.MODEL
import plm.patientslikeme2.utils.Constants.NAME
import plm.patientslikeme2.utils.Constants.NOTIFICATION_LIST
import plm.patientslikeme2.utils.Constants.PERSONAL_INFORMATION
import plm.patientslikeme2.utils.Constants.PRIVATE_MESSAGE
import plm.patientslikeme2.utils.Constants.PROFILE_PRIVATE_MESSAGE
import plm.patientslikeme2.utils.Constants.SEND_A_WELCOME
import plm.patientslikeme2.utils.Constants.TAG_DETAILS

fun Fragment.openUserProfile(from: String, userId: String) {
    val bundle = Bundle()
    bundle.putString(Constants.USER_ID, userId)
    when (from) {
        PERSONAL_INFORMATION -> {
            findNavController().navigate(R.id.action_navigation_to_user_profile, bundle)
        }
        FOLLOWING -> {
            findNavController().navigate(
                R.id.navigation_user_profile_to_navigation_user_profile,
                bundle
            )
        }
        COMMUNITY_MEMBER -> {
            findNavController().navigate(R.id.community_to_user_profile, bundle)
        }
        GROUP_MEMBER -> {
            findNavController().navigate(R.id.group_fragment_to_user_profile, bundle)
        }
        NOTIFICATION_LIST -> {
            findNavController().navigate(R.id.navigation_notification_to_user_profile, bundle)
        }
        FOLLOWING_MEMBER -> {
            findNavController().navigate(R.id.navigation_following_to_user_profile, bundle)
        }
        DISCUSSION_REPLY -> {
            findNavController().navigate(R.id.navigation_reply_to_user_profile, bundle)
        }
        DISCUSSION_COMMENT -> {
            findNavController().navigate(R.id.navigation_comment_to_user_profile, bundle)
        }
    }
}

fun Fragment.openGroupDetails(from: String, groupId: String, isPrivate: Boolean) {
    val bundle = Bundle()
    bundle.putString(Constants.GROUP_ID, groupId)
    bundle.putBoolean(Constants.GROUP_PRIVATE, isPrivate)
    when (from) {
        ABOUT_GROUP -> {
            findNavController().navigate(R.id.action_community_all_groups_to_group_fragment, bundle)
        }
        COMMUNITY_ALL_GROUP -> {
            findNavController().navigate(R.id.action_community_all_groups_to_group_fragment, bundle)
        }
        COMMUNITY_GROUP -> {
            findNavController().navigate(R.id.community_to_group_fragment, bundle)
        }
        HOME_MY_GROUP -> {
            findNavController().navigate(R.id.navigation_home_to_group_fragment, bundle)
        }
        HOME_PROMO_CARD_GROUP_OVERVIEW -> {
            findNavController().navigate(R.id.navigation_home_to_group_fragment, bundle)
        }
        NOTIFICATION_LIST -> {
            findNavController().navigate(R.id.navigation_notification_to_group_fragment, bundle)
        }
    }
}

fun Fragment.openTagDetails(from: String, name: String, id: Int) {
    val bundle = Bundle()
    bundle.putInt(ID, id)
    bundle.putString(NAME, name)
    when (from) {
        FOLLOWING_TAG -> {
            findNavController().navigate(R.id.navigation_following_to_tag_details_fragment, bundle)
        }
        GROUP_DISCUSSIONS -> {
            findNavController().navigate(R.id.group_fragment_to_tag_details_fragment, bundle)
        }
        GROUP_DISCUSSIONS_DETAILS -> {
            findNavController().navigate(R.id.navigation_comment_to_tag_details_fragment, bundle)
        }
    }
}

fun Fragment.openEventsDetails(from: String, model: String) {
    val bundle = Bundle()
    bundle.putString(MODEL, model)
    when (from) {
        Constants.COMMUNITY_EVENT -> {
            findNavController().navigate(R.id.action_navigation_community_to_event_details, bundle)
        }
        Constants.GROUP_EVENT -> {
            findNavController().navigate(R.id.group_fragment_to_event_details, bundle)
        }
    }
}

fun Fragment.openResourceDetails(from: String, id: Int, name: String) {
    val bundle = Bundle()
    bundle.putInt(ID, id)
    bundle.putString(NAME, name)
    when (from) {
        Constants.FOLLOWING_RESOURCE -> {
            findNavController().navigate(R.id.navigation_following_to_resource_details, bundle)
        }
        Constants.GROUP_RESOURCE -> {
            findNavController().navigate(R.id.group_fragment_to_resource_details, bundle)
        }
    }
}

fun Fragment.openTreatmentDetails(from: String, id: String) {
    val bundle = Bundle()
    bundle.putString(ID, id)
    when (from) {
        NOTIFICATION_LIST -> {
            findNavController().navigate(
                R.id.navigation_notification_to_navigation_my_treatments,
                bundle
            )
        }
    }
}

fun Fragment.openGroupDiscussionDetails(
    from: String,
    discussionId: String,
    groupTitle: String? = null,
    discussionDetails: Discussions? = null,
    isMember:Boolean = true
) {
    val bundle = Bundle()
    bundle.putString(Constants.DISCUSSION_ID, discussionId)
    bundle.putBoolean(Constants.IS_MEMBER, isMember)
    groupTitle?.let {
        bundle.putString(Constants.GROUP_TITLE, groupTitle)
    }
    discussionDetails?.let {
        bundle.putString(Constants.DISCUSSION_DETAILS, objectToString(discussionDetails))
    }
    when (from) {
        GROUP_DISCUSSIONS -> {
            findNavController().navigate(
                R.id.action_community_group_to_group_discussion_thread,
                bundle
            )
        }
        FOLLOWING_DISCUSSION -> {
            findNavController().navigate(
                R.id.navigation_following_to_group_discussion_thread,
                bundle
            )
        }
        TAG_DETAILS -> {
            findNavController().navigate(
                R.id.tag_details_fragment_to_group_discussion_thread,
                bundle
            )
        }
        HOME_PROMO_CARD_GROUP_DISCUSSION -> {
            findNavController().navigate(
                R.id.navigation_home_to_group_discussion_thread,
                bundle
            )
        }
        NOTIFICATION_LIST -> {
            findNavController().navigate(
                R.id.navigation_notification_to_group_discussion_thread,
                bundle
            )
        }
    }
}

fun Fragment.openDiscussionReplies(from: String, discussionId: String, commentId: String, isMember:Boolean = true) {
    val bundle = Bundle()
    bundle.putString(Constants.DISCUSSION_ID, discussionId)
    bundle.putString(Constants.COMMENT_ID, commentId)
    bundle.putBoolean(Constants.IS_MEMBER, isMember)
    when (from) {
        GROUP_DISCUSSIONS_DETAILS -> {
            findNavController().navigate(R.id.action_discussion_thread_to_reply, bundle)
        }
    }
}

fun Fragment.openPrivateMessageConversation(from: String) {
    NewMessageDialog.newInstance(from).show(childFragmentManager, getString(R.string.new_message))
}