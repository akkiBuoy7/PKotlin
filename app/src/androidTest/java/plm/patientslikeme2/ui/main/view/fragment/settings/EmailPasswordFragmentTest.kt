package plm.patientslikeme2.ui.main.view.fragment.settings

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import plm.patientslikeme2.R

internal class EmailPasswordFragmentTest : TestCase() {
    @Before
    override fun setUp() {
        launchFragmentInContainer<EmailPasswordFragment>()
    }

    @Test
    fun isSuccessErrorViewDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.ic_success_error))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isChangeEmailLayoutDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.ll_email))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isChangeEmailDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_email))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isChangePasswordLayoutDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.ll_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isChangePasswordDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_change_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}