package plm.patientslikeme2.ui.main.view.fragment.community

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import plm.patientslikeme2.R
import plm.patientslikeme2.ui.main.view.fragment.community.groups.CommunityGroupsFragment

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CommunityGroupsFragmentTest {

    private lateinit var scenario: FragmentScenario<CommunityGroupsFragment>
    private var itemCount: Int? = 0

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_PatientsLikeMe)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun isTvMyGroupsDisplayed() {
        onView(ViewMatchers.withId(R.id.tv_my_groups)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun isButtonBrowseAllGroupsDisplayed() {
        onView(ViewMatchers.withId(R.id.button_browse_all_groups)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun isTvSuggestedGroupsDisplayed() {
        onView(ViewMatchers.withId(R.id.tv_suggested_groups)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun isMyGroupsRecyclerDisplayed() {
        onView(ViewMatchers.withId(R.id.rv_my_groups)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun isSuggestedGroupsRecyclerDisplayed() {
        onView(ViewMatchers.withId(R.id.rv_suggested_groups)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun myGroupsRecyclerviewActionTest() {
        onView((ViewMatchers.withId(R.id.rv_my_groups))).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                2,
                click()
            )
        )
    }

    @Test
    fun myGroupsRecyclerScrollTest() {
        scenario.onFragment { it ->
            itemCount = it.binding?.rvMyGroups?.adapter?.itemCount
        }
        if (itemCount != null) {
            onView(ViewMatchers.withId(R.id.rv_my_groups))
                .perform(
                    RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                        itemCount!!.minus(
                            1
                        )
                    )
                )
        }
    }

    @Test
    fun suggestedGroupsRecyclerviewActionTest() {
        onView((ViewMatchers.withId(R.id.rv_suggested_groups))).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                2,
                click()
            )
        )
    }

    @Test
    fun suggestedGroupsRecyclerScrollTest() {
        scenario.onFragment { it ->
            itemCount = it.binding?.rvSuggestedGroups?.adapter?.itemCount
        }
        if (itemCount != null) {
            onView(ViewMatchers.withId(R.id.rv_suggested_groups))
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