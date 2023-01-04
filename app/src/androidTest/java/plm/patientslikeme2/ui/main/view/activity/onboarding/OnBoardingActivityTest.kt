package plm.patientslikeme2.ui.main.view.activity.onboarding


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import plm.patientslikeme2.R

@LargeTest
@RunWith(AndroidJUnit4::class)
class OnBoardingActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(OnBoardingActivity::class.java)

    @Test
    fun onBoardingActivityTest() {
        val textView = onView(
            allOf(
                withText("Month"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Month")))

        val textView2 = onView(
            allOf(
                withText("Day"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Day")))

        val textView3 = onView(
            allOf(
                withText("Year"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Year")))

        val textView4 = onView(
            allOf(
                withId(R.id.tv_month),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("")))

        val textView5 = onView(
            allOf(
                withId(R.id.tv_day),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("")))

        val textView6 = onView(
            allOf(
                withId(R.id.tv_year),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("")))

        val textView7 = onView(
            allOf(
                withId(R.id.tv_dob_details),
                withText("You must be over 13 to join. Your date of birth is only visible to other PatientsLikeMe community members."),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        textView7.check(matches(withText("You must be over 13 to join. Your date of birth is only visible to other PatientsLikeMe community members.")))

        val checkBox = onView(
            allOf(
                withId(R.id.cb_joining_caregiver),
                withText("I am joining as a caregiver for someone else."),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        checkBox.check(matches(isDisplayed()))

        val checkBox2 = onView(
            allOf(
                withId(R.id.cb_terms_conditions),
                withText("I agree to the PatientsLikeMe terms & conditions of use and privacy policy"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        checkBox2.check(matches(isDisplayed()))

        val imageView = onView(
            allOf(
                withParent(withParent(withId(R.id.ll_bottom))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val imageView2 = onView(
            allOf(
                withParent(withParent(withId(R.id.ll_bottom))),
                isDisplayed()
            )
        )
        imageView2.check(matches(isDisplayed()))

        val imageView3 = onView(
            allOf(
                withParent(withParent(withId(R.id.ll_bottom))),
                isDisplayed()
            )
        )
        imageView3.check(matches(isDisplayed()))

        val button = onView(
            allOf(
                withId(R.id.btn_back), withText("Back"),
                withParent(withParent(withId(R.id.ll_bottom))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val button2 = onView(
            allOf(
                withId(R.id.btn_create_account), withText("Create an account"),
                withParent(withParent(withId(R.id.ll_bottom))),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))
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
