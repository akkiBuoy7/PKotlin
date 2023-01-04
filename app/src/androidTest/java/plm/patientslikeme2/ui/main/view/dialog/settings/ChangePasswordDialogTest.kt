package plm.patientslikeme2.ui.main.view.dialog.settings

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import plm.patientslikeme2.R


internal class ChangePasswordDialogTest : TestCase() {
    @Before
    override fun setUp() {
        launchFragmentInContainer<ChangePasswordDialog>()
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
    fun isHeaderNewPasswordDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_new_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isPasswordStrengthLayoutDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.llPasswordStrength))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isHeaderStrengthDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tvStrength))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isPasswordStrengthDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tvPasswordStrength))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isEditNewPasswordDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.et_new_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isHeaderConfirmPasswordDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_confirm_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isEditConfirmPasswordDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.et_confirm_password))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isErrorTextDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_password_error))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isButtonSaveDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_save))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}