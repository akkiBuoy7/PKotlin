package plm.patientslikeme2.ui.main.view.fragment.profile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import plm.patientslikeme2.R

internal class MemberAboutFragmentTest : TestCase() {
    @Before
    override fun setUp() {
        launchFragmentInContainer<MemberAboutFragment>()
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
    fun isHeadingDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_heading))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isDescriptionDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_description))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isConditionsHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_conditions))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isConditionsListDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_conditions))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isCommunityBadgesDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_community_badges))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isQuickStatsHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_quick_stats))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isQuickStats1Displayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_quick_stats_1))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isQuickStats2Displayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_quick_stats_2))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isQuickStats3Displayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_quick_stats_3))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isQuickStats4Displayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_quick_stats_4))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isQuickStats5Displayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_quick_stats_5))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isInterestHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_interests))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isPrimaryInterestsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_primary_interest))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSecondaryInterestsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_secondary_interests))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}