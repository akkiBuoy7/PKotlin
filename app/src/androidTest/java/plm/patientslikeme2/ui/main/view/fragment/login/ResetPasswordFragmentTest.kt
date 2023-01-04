package plm.patientslikeme2.ui.main.view.fragment.login

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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

class ResetPasswordFragmentTest {

    private lateinit var scenario: FragmentScenario<ResetPasswordFragment>

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_PatientsLikeMe)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun resetPasswordFragmentTest() {
        val userEditText = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.et_password),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.ll_reset),
                        1
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userEditText.perform(ViewActions.replaceText("admin@123"), ViewActions.closeSoftKeyboard())

        val userEditText2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.et_password), ViewMatchers.withText("admin@123"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.ll_reset),
                        1
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userEditText2.perform(ViewActions.click())

        val userEditText3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.et_password), ViewMatchers.withText("admin@123"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.ll_reset),
                        1
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userEditText3.perform(ViewActions.click())

        val userButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.btn_submit), ViewMatchers.withText("Submit"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.ll_reset),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                            2
                        )
                    ),
                    3
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userButton.perform(ViewActions.click())

        Espresso.pressBack()

        val imageView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.imgLogo),
                ViewMatchers.withContentDescription("PatientsLikeMe"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        imageView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Reset password"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ViewGroup::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("Reset password")))

        val editText = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.et_password), ViewMatchers.withText("•••••••••"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.ll_reset))),
                ViewMatchers.isDisplayed()
            )
        )
        editText.check(ViewAssertions.matches(ViewMatchers.withText("")))

        val imageView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.iv_visible_password),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.ll_reset))),
                ViewMatchers.isDisplayed()
            )
        )
        imageView2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_error),
                ViewMatchers.withText("Must be at least 8 characters and include at least two of the following: uppercase letter, lowercase letter, number, or special character"),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.ll_reset),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        textView2.check(ViewAssertions.matches(ViewMatchers.withText("Must be at least 8 characters and include at least two of the following: uppercase letter, lowercase letter, number, or special character")))

        val button = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.btn_submit), ViewMatchers.withText("Submit"),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.ll_reset),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        button.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
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