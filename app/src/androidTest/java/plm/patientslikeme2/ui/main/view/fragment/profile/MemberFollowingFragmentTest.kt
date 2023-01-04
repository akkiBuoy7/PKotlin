package plm.patientslikeme2.ui.main.view.fragment.profile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import plm.patientslikeme2.R

internal class MemberFollowingFragmentTest : TestCase() {
    @Before
    override fun setUp() {
        launchFragmentInContainer<MemberFollowingFragment>()
    }

    @Test
    fun isNoResultViewDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.vs_no_result))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isParentViewDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.parent))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isHeaderCountDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_heading))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isFollowingsListDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_followings))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}