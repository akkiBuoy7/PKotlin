package plm.patientslikeme2.ui.main.view.fragment.conditions

import android.os.Bundle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import dagger.hilt.android.testing.HiltAndroidRule
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import plm.patientslikeme2.R
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.launchFragmentInHiltContainer

@RunWith(AndroidJUnit4ClassRunner::class)
internal class ConditionDetailsFragmentTest : TestCase() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        val bundle = Bundle()
        bundle.putString(Constants.Arguments.CONDITION_ID, "1403967")
        bundle.putString(Constants.Arguments.CONDITION_NAME, "alcohol use disorder")
        launchFragmentInHiltContainer<ConditionDetailsFragment>(bundle, R.style.Theme_PatientsLikeMe)
    }

    @Test
    fun isNoResultDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.vs_no_result))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isParentLayoutDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.parent))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isStageDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_stage))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isDiagnosedDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_diagnosed))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isFirstSymptomDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_first_symptoms))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isTreatmentsHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_treatments))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isAddTreatmentButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_treatments_add))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isTreatmentsListDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_treatments))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSymptomsHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_symptoms))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isAddSymptomButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_add_symptoms))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSymptomsListDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_symptoms))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isRemoveConditionButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_remove_condition))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}