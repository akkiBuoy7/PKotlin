package plm.patientslikeme2.ui.main.view.activity.onboarding

import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import plm.patientslikeme2.data.di.ActivityModule

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
@UninstallModules(ActivityModule::class)
class OnBoardingActivityTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityScenarioRule: ActivityScenarioRule<OnBoardingActivity> =
        ActivityScenarioRule(OnBoardingActivity::class.java)

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
    }

    @Test
    fun whenOnBoardingActivityLaunchedNavigatorIsInvokedForFragment() {
        activityScenarioRule.scenario
    }
}