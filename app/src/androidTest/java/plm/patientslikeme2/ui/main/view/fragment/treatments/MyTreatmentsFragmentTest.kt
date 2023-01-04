package plm.patientslikeme2.ui.main.view.fragment.treatments

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
import plm.patientslikeme2.utils.launchFragmentInHiltContainer

@RunWith(AndroidJUnit4ClassRunner::class)
internal class MyTreatmentsFragmentTest : TestCase() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        launchFragmentInHiltContainer<MyTreatmentsFragment>(themeResId =R.style.Theme_PatientsLikeMe)
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
    fun isChronicKidneyDiseaseListDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_treatment))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}