package plm.patientslikeme2.ui.main.view.fragment.profile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import plm.patientslikeme2.R

internal class UserProfileFragmentTest : TestCase() {
    @Before
    override fun setUp() {
        launchFragmentInContainer<UserProfileFragment>()
    }

    @Test
    fun isCloseIconDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.iv_close))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isUserProfileDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.iv_user))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isUserNameDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_name))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isDescriptionDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_description))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isAddressDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_address))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isCommentsCountDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_comment_count))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isLikesCountDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_like_count))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isMemberOptionsViewDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.ll_follow_private_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isFollowButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_follow))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isPrivateMessageButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_private_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isTabLayoutDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tab_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isViewPagerDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.pager))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}