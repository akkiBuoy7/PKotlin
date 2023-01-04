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

class BlockedPrivateMessagesFragmentTest {

    private lateinit var scenario: FragmentScenario<BlockedPrivateMessagesFragment>

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_PatientsLikeMe)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun blockedPrivateMessagesFragmentTest() {
        val userButton2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_block_unblock), ViewMatchers.withText("Unblock"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.rl_top),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userButton2.perform(ViewActions.click())

        val userButton3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_block_unblock), ViewMatchers.withText("Block"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.rl_top),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userButton3.perform(ViewActions.click())

        val appCompatImageView3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.iv_close),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.rl_success),
                        childAtPosition(
                            ViewMatchers.withId(R.id.ic_success_error),
                            0
                        )
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatImageView3.perform(ViewActions.click())

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
                ViewMatchers.withId(R.id.toolbar_title), ViewMatchers.withText("Blocked Private Messages"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.toolbar))),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("Blocked Private Messages")))

        val relativeLayout = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.toolbar),
                        ViewMatchers.withParent(ViewMatchers.withId(R.id.container))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        relativeLayout.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_header),
                ViewMatchers.withText("When you block private messages from a member, you won’t be able to message each other, but you can still see your private message history."),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.parent))),
                ViewMatchers.isDisplayed()
            )
        )
        textView2.check(ViewAssertions.matches(ViewMatchers.withText("When you block private messages from a member, you won’t be able to message each other, but you can still see your private message history.")))

        val relativeLayout2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.rl_top),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.rv_posts))),
                ViewMatchers.isDisplayed()
            )
        )
        relativeLayout2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val imageView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.iv_user),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.fl_image),
                        ViewMatchers.withParent(ViewMatchers.withId(R.id.rl_top))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        imageView2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textView3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_title), ViewMatchers.withText("User925584"),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.rl_top),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        textView3.check(ViewAssertions.matches(ViewMatchers.withText("User925584")))

        val button = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_block_unblock), ViewMatchers.withText("Unblock"),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.rl_top),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        button.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val view = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.view_divider),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.rv_posts))),
                ViewMatchers.isDisplayed()
            )
        )
        view.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val rvBlockedMembers = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.rv_blocked_members),
                ViewMatchers.isDisplayed()
            )
        )
        rvBlockedMembers.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
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