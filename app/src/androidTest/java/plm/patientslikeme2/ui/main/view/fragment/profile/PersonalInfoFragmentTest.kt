package plm.patientslikeme2.ui.main.view.fragment.profile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import plm.patientslikeme2.R

internal class PersonalInfoFragmentTest : TestCase() {
    @Before
    override fun setUp() {
        launchFragmentInContainer<PersonalInfoFragment>()
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
    fun isProfileHeadlineHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_profile_headline_header))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isProfileHeadlineDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_profile_headline))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isDemographicsHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_demographics))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isBirthDateDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_birthdate))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSexDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_sex))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isGenderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_gender))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isEthnicityDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_ethnicity))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isHighestLevelOfEducationDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_highest_level_of_education))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isInsuranceDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_insurance))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isMilitaryStatusDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_military_status))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isLocationDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_location))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isMyStoryHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_my_story_heading))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isMyStoryDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_my_story))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isInterestHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_interests))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isPrimaryInterestDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_primary_interest))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSecondaryInterestsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_secondary_interests))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isViewMyProfileButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_view_my_profile))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}