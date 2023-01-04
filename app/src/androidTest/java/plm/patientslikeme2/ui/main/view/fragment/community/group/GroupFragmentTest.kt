package plm.patientslikeme2.ui.main.view.fragment.community.group

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import plm.patientslikeme2.R
import plm.patientslikeme2.ui.main.view.fragment.community.groups.groupDetails.GroupFragment
import plm.patientslikeme2.utils.Constants
import plm.patientslikeme2.utils.launchFragmentInHiltContainer


@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class GroupFragmentTest {

    private lateinit var scenario: FragmentScenario<GroupFragment>

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun firstTest() {
        val bundle = Bundle()
        bundle.putString(Constants.Arguments.GROUP_ID, "17")
        launchFragmentInHiltContainer<GroupFragment>(bundle, R.style.Theme_PatientsLikeMe)

        Espresso.onView(withId(R.id.parent)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.vs_no_result)).check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))

        Espresso.onView(withId(R.id.tv_group_name)).check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        Espresso.onView(withId(R.id.tv_moderated_name)).check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))


//        Espresso.onView(withId(R.id.)).perform(ViewActions.clearText())
//        Espresso.onView(withId(R.id.)).perform(ViewActions.clearText())
//        Espresso.onView(withId(R.id.)).perform(ViewActions.click())
    }
}