package plm.patientslikeme2.ui.main.view.fragment.settings

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import plm.patientslikeme2.R

internal class PrivacySettingsFragmentTest : TestCase() {

    @Before
    override fun setUp() {
        launchFragmentInContainer<PrivacySettingsFragment>()
    }

    @Test
    fun isVSNoResultDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.vs_no_result))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isParentDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.parent))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_header))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSubHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_sub_header))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isOptionsRadioGroupDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.radio_group))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isVisibleToNonMembersRadioButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rbVisibleToNonMembers))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isVisibleToCommunityRadioButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rbVisibleToCommunity))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isControlDataRadioButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rbControlData))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isButtonUpdateDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_update))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isButtonCancelDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_cancel))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isLastUpdateSeenDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_last_seen))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isControlDataLayoutDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.lL_Control_Data))
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
    fun isSymptomsHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_symptoms))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSymptomsListDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_symptoms))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isTreatmentsHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_treatments))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isTreatmentsListDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_treatments))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isLabsHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_vitals_and_labs))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isLabsListDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_vitals_and_labs))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}