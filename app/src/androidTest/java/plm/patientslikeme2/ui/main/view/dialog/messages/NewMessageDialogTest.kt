package plm.patientslikeme2.ui.main.view.dialog.messages

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import plm.patientslikeme2.R

internal class NewMessageDialogTest : TestCase() {
    @Before
    override fun setUp() {
        launchFragmentInContainer<NewMessageDialog>()
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
    fun isReplyMessageViewDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_reply_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isHeaderToDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_to))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isUserSearchViewDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.ac_search_user))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isMessageInputHeaderDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isMessageInputViewDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.et_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isErrorLayoutDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.ll_error))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSendOrReplyButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_send))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isCancelButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_cancel))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}