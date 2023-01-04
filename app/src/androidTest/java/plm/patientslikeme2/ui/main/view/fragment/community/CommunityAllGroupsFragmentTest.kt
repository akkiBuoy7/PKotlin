package plm.patientslikeme2.ui.main.view.fragment.community

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import plm.patientslikeme2.R
import plm.patientslikeme2.ui.main.view.fragment.community.groups.CommunityAllGroupsFragment

class CommunityAllGroupsFragmentTest {

    private lateinit var scenario: FragmentScenario<CommunityAllGroupsFragment>
    private var itemCount: Int? = 0

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_PatientsLikeMe)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun isTvLookingForDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_looking_for)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun isAutoCompleteTextViewDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.ac_search_group)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun isTvAllGroupsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_all_groups)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun isRvAllGroupsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_all_groups)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }

    @Test
    fun allGroupsRecyclerviewActionTest() {
        Espresso.onView((ViewMatchers.withId(R.id.rv_all_groups))).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                2,
                ViewActions.click()
            )
        )
    }

    @Test
    fun allGroupsRecyclerScrollTest() {
        scenario.onFragment { it ->
            itemCount = it.binding?.rvAllGroups?.adapter?.itemCount
        }
        if (itemCount != null) {
            Espresso.onView(ViewMatchers.withId(R.id.rv_all_groups))
                .perform(
                    RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                        itemCount!!.minus(
                            1
                        )
                    )
                )
        }
    }
}
