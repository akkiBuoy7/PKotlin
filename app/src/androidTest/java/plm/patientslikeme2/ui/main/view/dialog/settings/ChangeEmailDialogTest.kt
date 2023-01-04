package plm.patientslikeme2.ui.main.view.dialog.settings

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import plm.patientslikeme2.R

internal class ChangeEmailDialogTest : TestCase() {

    @Before
    override fun setUp() {
        launchFragmentInContainer<ChangeEmailDialog>()
    }

    @Test
    fun isHeaderLayoutDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rl_header))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_header))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isCloseButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.iv_close))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isHeaderCurrentEmailDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_current_email))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isCurrentEmailDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_current_email_id))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isHeaderCurrentPasswordDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_current_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isEditCurrentPasswordDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.et_current_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isHeaderNewEmailDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_new_email))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isEditNewEmailDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.et_new_email))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isHeaderConfirmNewEmailDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_new_confirm_mail))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isEditConfirmNewEmailDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.et_confirm_new_mail))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isErrorTextDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_email_error))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isButtonSaveDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_save))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}