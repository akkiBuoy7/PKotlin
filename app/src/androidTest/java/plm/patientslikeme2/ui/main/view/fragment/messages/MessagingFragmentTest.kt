package plm.patientslikeme2.ui.main.view.fragment.messages

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import plm.patientslikeme2.R


internal class MessagingFragmentTest : TestCase() {
    @Before
    override fun setUp() {
        launchFragmentInContainer<MessagingFragment>()
    }

    @Test
    fun isNoResultViewDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.vs_no_result))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isParentViewDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.parent))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSearchUserViewDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.ac_search_user))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isConversationsListDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_conversations))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isNewMessageButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_new_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}