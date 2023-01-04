package plm.patientslikeme2.ui.main.view.fragment.profile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import plm.patientslikeme2.R

internal class MemberConditionsFragmentTest : TestCase() {
    @Before
    override fun setUp() {
        launchFragmentInContainer<MemberConditionsFragment>()
    }

    @Test
    fun isNoResultViewDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.vs_no_result))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isConditionsListDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_conditions))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}