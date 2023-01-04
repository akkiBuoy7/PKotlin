package plm.patientslikeme2.ui.main.view.fragment.signup

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

class SignupFragmentTest {

    private lateinit var scenario: FragmentScenario<SignupFragment>

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_PatientsLikeMe)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun signupFragmentTest() {
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
                ViewMatchers.withText("Create an account"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("Create an account")))

        val textView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_already_member),
                ViewMatchers.withText("Already a member? Sign in"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView2.check(ViewAssertions.matches(ViewMatchers.withText("Already a member? Sign in")))

        val textView3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Email"),
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
        editText.check(ViewAssertions.matches(ViewMatchers.withText("")))

        val textView4 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_email_details),
                ViewMatchers.withText("Your email address will not be shared with anyone"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView4.check(ViewAssertions.matches(ViewMatchers.withText("Your email address will not be shared with anyone")))

        val textView5 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Username"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView5.check(ViewAssertions.matches(ViewMatchers.withText("Username")))

        val textView6 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_username_details),
                ViewMatchers.withText("This is visible to other PatientsLikeMe members. You may want to use an anonymous name."),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView6.check(ViewAssertions.matches(ViewMatchers.withText("This is visible to other PatientsLikeMe members. You may want to use an anonymous name.")))

        val textView7 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Password"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView7.check(ViewAssertions.matches(ViewMatchers.withText("Password")))

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

        val textView8 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_password_details),
                ViewMatchers.withText("Must be at least 8 characters and include at least two of the following: uppercase letter, lowercase letter, number, or special character"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView8.check(ViewAssertions.matches(ViewMatchers.withText("Must be at least 8 characters and include at least two of the following: uppercase letter, lowercase letter, number, or special character")))

        val textView9 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Date of Birth"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView9.check(ViewAssertions.matches(ViewMatchers.withText("Date of Birth")))

        val textView10 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Month"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView10.check(ViewAssertions.matches(ViewMatchers.withText("Month")))

        val textView11 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Day"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView11.check(ViewAssertions.matches(ViewMatchers.withText("Day")))

        val textView12 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Year"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView12.check(ViewAssertions.matches(ViewMatchers.withText("Year")))

        val textView13 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_month),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView13.check(ViewAssertions.matches(ViewMatchers.withText("")))

        val textView14 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_day),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView14.check(ViewAssertions.matches(ViewMatchers.withText("")))

        val textView15 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_year),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView15.check(ViewAssertions.matches(ViewMatchers.withText("")))

        val textView16 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_dob_details),
                ViewMatchers.withText("You must be over 13 to join. Your date of birth is only visible to other PatientsLikeMe community members."),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView16.check(ViewAssertions.matches(ViewMatchers.withText("You must be over 13 to join. Your date of birth is only visible to other PatientsLikeMe community members.")))

        val checkBox = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cb_joining_caregiver),
                ViewMatchers.withText("I am joining as a caregiver for someone else."),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        checkBox.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val checkBox2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cb_terms_conditions),
                ViewMatchers.withText("I agree to the PatientsLikeMe terms & conditions of use and privacy policy"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        checkBox2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val imageView3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.ll_bottom))),
                ViewMatchers.isDisplayed()
            )
        )
        imageView3.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val imageView4 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.ll_bottom))),
                ViewMatchers.isDisplayed()
            )
        )
        imageView4.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val imageView5 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.ll_bottom))),
                ViewMatchers.isDisplayed()
            )
        )
        imageView5.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val button = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.btn_back), ViewMatchers.withText("Back"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.ll_bottom))),
                ViewMatchers.isDisplayed()
            )
        )
        button.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val button2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.btn_create_account),
                ViewMatchers.withText("Create an account"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.ll_bottom))),
                ViewMatchers.isDisplayed()
            )
        )
        button2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val userTextView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_already_member),
                ViewMatchers.withText("Already a member? Sign in"),
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
        userTextView2.perform(ViewActions.click())

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
            ViewActions.replaceText("sambhaji.k@westaiglelabs.com"),
            ViewActions.closeSoftKeyboard()
        )

        val userEditText2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.et_username),
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
        userEditText2.perform(
            ViewActions.replaceText("sambhaji.k"),
            ViewActions.closeSoftKeyboard()
        )

        val userEditText3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.et_password),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        10
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userEditText3.perform(ViewActions.replaceText("admin@123"), ViewActions.closeSoftKeyboard())

        val appCompatImageView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.iv_visible_password),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        10
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
                        10
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatImageView2.perform(ViewActions.click())

        val userTextView4 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_month),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        0
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userTextView4.perform(ViewActions.click())

        val materialTextView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withClassName(Matchers.`is`("com.google.android.material.textview.MaterialTextView")),
                ViewMatchers.withText("2022"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                        0
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialTextView.perform(ViewActions.click())

        val materialTextView2 = Espresso.onData(Matchers.anything())
            .inAdapterView(
                Matchers.allOf(
                    ViewMatchers.withClassName(Matchers.`is`("android.widget.YearPickerView")),
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("com.android.internal.widget.DialogViewAnimator")),
                        1
                    )
                )
            )
            .atPosition(82)
        materialTextView2.perform(ViewActions.scrollTo(), ViewActions.click())

        val materialButton = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(android.R.id.button1), ViewMatchers.withText("OK"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        materialButton.perform(ViewActions.scrollTo(), ViewActions.click())

        val userCheckBox = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cb_joining_caregiver),
                ViewMatchers.withText("I am joining as a caregiver for someone else."),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    16
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userCheckBox.perform(ViewActions.click())

        val userCheckBox2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cb_terms_conditions),
                ViewMatchers.withText("I agree to the PatientsLikeMe terms & conditions of use and privacy policy"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    17
                ),
                ViewMatchers.isDisplayed()
            )
        )
        userCheckBox2.perform(ViewActions.click())
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