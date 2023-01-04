package plm.patientslikeme2.ui.main.view.fragment.community

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import plm.patientslikeme2.R

class CommunityFragmentTest {

    private lateinit var scenario: FragmentScenario<CommunityFragment>

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_PatientsLikeMe)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun isTabLayoutDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.ll_tab_layout)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun isViewPagerDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.pager)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

}