package plm.patientslikeme2.ui.main.view.fragment.settings

import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import dagger.hilt.android.testing.HiltAndroidRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import plm.patientslikeme2.R

class BlockedMembersFragmentTest {

    private lateinit var scenario: FragmentScenario<BlockedMembersFragment>

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_PatientsLikeMe)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun blockedMembersFragmentTest() {
        val imageView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.iv_back),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.ll_left),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(RelativeLayout::class.java))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        imageView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.toolbar_title), ViewMatchers.withText("Blocked members"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.toolbar))),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("Blocked members")))

        val textView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_hidden_post), ViewMatchers.withText("Hidden Posts"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.nav_host_fragment_activity_main))),
                ViewMatchers.isDisplayed()
            )
        )
        textView2.check(ViewAssertions.matches(ViewMatchers.withText("Hidden Posts")))

        val textView3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_blocked_private_messages),
                ViewMatchers.withText("Blocked Private Messages"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.nav_host_fragment_activity_main))),
                ViewMatchers.isDisplayed()
            )
        )
        textView3.check(ViewAssertions.matches(ViewMatchers.withText("Blocked Private Messages")))

        val textView4 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_blocked_private_messages),
                ViewMatchers.withText("Blocked Private Messages"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.nav_host_fragment_activity_main))),
                ViewMatchers.isDisplayed()
            )
        )
        textView4.check(ViewAssertions.matches(ViewMatchers.withText("Blocked Private Messages")))

        val userTextView3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_hidden_post), ViewMatchers.withText("Hidden Posts"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.nav_host_fragment_activity_main),
                        0
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userTextView3.perform(ViewActions.click())

        val userTextView4 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_blocked_private_messages),
                ViewMatchers.withText("Blocked Private Messages"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.nav_host_fragment_activity_main),
                        0
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userTextView4.perform(ViewActions.click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}