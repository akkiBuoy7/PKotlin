package plm.patientslikeme2.ui.main.view.fragment.login

import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
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

class ForgetPasswordFragmentTest {

    private lateinit var scenario: FragmentScenario<ForgetPasswordFragment>

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_PatientsLikeMe)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun forgetPasswordFragmentTest() {
        val userEditText = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.et_email),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    4
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userEditText.perform(
            ViewActions.replaceText("sambhaji.k@westagile.com"),
            ViewActions.closeSoftKeyboard()
        )

        val userEditText2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.et_email),
                ViewMatchers.withText("sambhaji.k@westagile.com"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    4
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userEditText2.perform(ViewActions.pressImeActionButton())

        val userButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.btn_submit), ViewMatchers.withText("Submit"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    6
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userButton.perform(ViewActions.click())

        val userButton2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.btn_back), ViewMatchers.withText("Back"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.nav_host_fragment),
                        0
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userButton2.perform(ViewActions.click())

        val userTextView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_forget_password),
                ViewMatchers.withText("Forgot password?"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        3
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userTextView2.perform(ViewActions.click())

        val imageView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.imgLogo),
                ViewMatchers.withContentDescription("PatientsLikeMe"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        imageView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Reset password"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("Reset password")))

        val textView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Please enter the email address you used when you joined. We’ll send you instructions to reset your password."),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView2.check(ViewAssertions.matches(ViewMatchers.withText("Please enter the email address you used when you joined. We’ll send you instructions to reset your password.")))

        val textView3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_confirm_password), ViewMatchers.withText("Email"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView3.check(ViewAssertions.matches(ViewMatchers.withText("Email")))

        val editText = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.et_email),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        editText.check(ViewAssertions.matches(ViewMatchers.withText("sambhaji.k@westagilelabs.com")))

        val button = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.btn_submit), ViewMatchers.withText("Submit"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        button.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val button2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.btn_back), ViewMatchers.withText("Back"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.nav_host_fragment))),
                ViewMatchers.isDisplayed()
            )
        )
        button2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
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