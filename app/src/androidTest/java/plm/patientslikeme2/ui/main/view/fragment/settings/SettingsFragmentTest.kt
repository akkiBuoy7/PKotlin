package plm.patientslikeme2.ui.main.view.fragment.settings

import android.widget.ScrollView
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import dagger.hilt.android.testing.HiltAndroidRule
import org.hamcrest.Matchers
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import plm.patientslikeme2.R

class SettingsFragmentTest {

    private lateinit var scenario: FragmentScenario<SettingsFragment>

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_PatientsLikeMe)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun settingsFragmentTest() {
        val relativeLayout2 = Espresso.onView(
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
        relativeLayout2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.toolbar_title), ViewMatchers.withText("Settings"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.toolbar))),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("Settings")))

        val textView2 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_push_notification),
                ViewMatchers.withText("Push Notifications"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView2.check(ViewAssertions.matches(ViewMatchers.withText("Push Notifications")))

        val textView3 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_privacy_settings),
                ViewMatchers.withText("Privacy settings"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView3.check(ViewAssertions.matches(ViewMatchers.withText("Privacy settings")))

        val textView4 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_email_password),
                ViewMatchers.withText("Email address & password"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView4.check(ViewAssertions.matches(ViewMatchers.withText("Email address & password")))

        val textView5 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tv_blocked_members),
                ViewMatchers.withText("Blocked members"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(ScrollView::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView5.check(ViewAssertions.matches(ViewMatchers.withText("Blocked members")))
    }
}