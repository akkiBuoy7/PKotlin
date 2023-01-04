package plm.patientslikeme2.ui.main.view.fragment.settings

import android.widget.LinearLayout
import android.widget.RelativeLayout
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

class PushNotificationsFragmentTest {

    private lateinit var scenario: FragmentScenario<PushNotificationsFragment>

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_PatientsLikeMe)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun pushNotificationsFragmentTest() {
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

        val imageView2 = Espresso.onView(
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
        imageView2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textView6 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.toolbar_title),
                ViewMatchers.withText("Push Notifications"),
                ViewMatchers.withParent(ViewMatchers.withParent(ViewMatchers.withId(R.id.toolbar))),
                ViewMatchers.isDisplayed()
            )
        )
        textView6.check(ViewAssertions.matches(ViewMatchers.withText("Push Notifications")))

        val textViewTR = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Treatment reminders"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textViewTR.check(ViewAssertions.matches(ViewMatchers.withText("Treatment reminders")))

        val switchTR = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.sc_treatment_reminder),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        switchTR.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textViewGR = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Goal reminders"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textViewGR.check(ViewAssertions.matches(ViewMatchers.withText("Goal reminders")))

        val switchGR = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.sc_goal_reminder),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        switchGR.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textViewPM = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Private messages"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textViewPM.check(ViewAssertions.matches(ViewMatchers.withText("Private messages")))

        val switchPM = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.sc_private_messages),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        switchPM.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textView8 = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Community updates"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textView8.check(ViewAssertions.matches(ViewMatchers.withText("Community updates")))

        val textViewRR = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Replies or reactions to discussions I started"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textViewRR.check(ViewAssertions.matches(ViewMatchers.withText("Replies or reactions to discussions I started")))

        val switchRR = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.sc_replies_reactions),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        switchRR.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textViewDF = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Discussions I’m following"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textViewDF.check(ViewAssertions.matches(ViewMatchers.withText("Discussions I’m following")))

        val switchDF = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.sc_discussion),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        switchDF.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textViewSM = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Someone mentions me"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textViewSM.check(ViewAssertions.matches(ViewMatchers.withText("Someone mentions me")))

        val switchSM = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.sc_someone_mentions),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        switchSM.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textViewSS = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Someone started following me"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textViewSS.check(ViewAssertions.matches(ViewMatchers.withText("Someone started following me")))

        val switchSS = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.sc_someone_started),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        switchSS.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val textViewDS = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("Discussions started by people I follow"),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        textViewDS.check(ViewAssertions.matches(ViewMatchers.withText("Discussions started by people I follow")))

        val switchDS = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.sc_replies_reactions),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        )
        switchDS.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}