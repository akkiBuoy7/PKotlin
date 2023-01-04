package plm.patientslikeme2.ui.main.view.fragment.login

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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

class LoginFragmentTest {

    private lateinit var scenario: FragmentScenario<LoginFragment>

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_PatientsLikeMe)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun loginFragmentTest() {
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
                ViewMatchers.withText("Username or email"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("Username or email")))

        val editText = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.et_email),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        editText.check(ViewAssertions.matches(ViewMatchers.withText("")))

        val textView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Password"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView2.check(ViewAssertions.matches(ViewMatchers.withText("Password")))

        val textView3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_forget_password),
                ViewMatchers.withText("Forgot password?"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView3.check(ViewAssertions.matches(ViewMatchers.withText("Forgot password?")))

        val editText2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.et_password),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        editText2.check(ViewAssertions.matches(ViewMatchers.withText("")))

        val imageView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.iv_visible_password),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        imageView2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val checkBox = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cb_remember_me), ViewMatchers.withText("Remember me"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        checkBox.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val button = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.btn_sign_in), ViewMatchers.withText("Sign in"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        button.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textView4 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_create_account),
                ViewMatchers.withText("Create an account"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.nav_host_fragment))),
                ViewMatchers.isDisplayed()
            )
        )
        textView4.check(ViewAssertions.matches(ViewMatchers.withText("Create an account")))

        val textView5 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_create_account),
                ViewMatchers.withText("Create an account"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.nav_host_fragment))),
                ViewMatchers.isDisplayed()
            )
        )
        textView5.check(ViewAssertions.matches(ViewMatchers.withText("Create an account")))

        val userEditText = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.et_email),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userEditText.perform(
            ViewActions.replaceText("sambhaji_35"),
            ViewActions.closeSoftKeyboard()
        )

        val userEditText2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.et_password),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        4
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userEditText2.perform(ViewActions.replaceText("admin@123"), ViewActions.closeSoftKeyboard())

        val userCheckBox = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cb_remember_me), ViewMatchers.withText("Remember me"),
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
        userCheckBox.perform(ViewActions.click())

        val userCheckBox2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cb_remember_me), ViewMatchers.withText("Remember me"),
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
        userCheckBox2.perform(ViewActions.click())

        val appCompatImageView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.iv_visible_password),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        4
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatImageView.perform(ViewActions.click())

        val appCompatImageView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.iv_visible_password),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        4
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatImageView2.perform(ViewActions.click())

        val userTextView = Espresso.onView(
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
        userTextView.perform(ViewActions.click())

        val userTextView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_create_account),
                ViewMatchers.withText("Create an account"),
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
        userTextView2.perform(ViewActions.click())

        val userButton3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.btn_sign_in), ViewMatchers.withText("Sign in"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    7
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userButton3.perform(ViewActions.click())
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