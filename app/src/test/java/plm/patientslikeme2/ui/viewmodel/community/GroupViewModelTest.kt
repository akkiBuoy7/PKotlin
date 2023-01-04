package plm.patientslikeme2.ui.viewmodel.community

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import plm.patientslikeme2.data.api.MockApiService
import plm.patientslikeme2.data.repository.community.MockCommunityGroupRepository
import javax.inject.Inject


@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@ExperimentalCoroutinesApi
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GroupViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: MockCommunityGroupRepository

    @BindValue
    @JvmField
    val mockApiService: MockApiService = MockApiService()

    @Before
    fun setup() {
        hiltRule.inject()
        repository = MockCommunityGroupRepository(mockApiService)
    }

    @Test
    fun `0 Test groupOverview API success`() = runTest {
        val application = ApplicationProvider.getApplicationContext<Context>()
        Assert.assertNotNull(application)

        mockApiService.forceApiFailure = false
        mockApiService.forceInvalidResponse = false

        repository.getGroupOverview("17").observeForever { resp ->
            Assert.assertNotNull("response is null", resp)
            Assert.assertTrue("invalid response status", resp.status.isSuccessful())
            Assert.assertNotNull("response data missing",resp.data)
        }
    }

    @Test
    fun `1 Test groupOverview API failure`() = runTest {

        mockApiService.forceApiFailure = true
        mockApiService.forceInvalidResponse = false

        repository.getGroupOverview("17").observeForever { resp ->
            Assert.assertNotNull("response is null", resp)
            Assert.assertTrue("invalid response status", resp.status.isError())
            Assert.assertNull(resp.data)
        }
    }

    @Test
    fun `2 Test groupOverview API invalid response`() = runTest {

        mockApiService.forceApiFailure = false
        mockApiService.forceInvalidResponse = true

        repository.getGroupOverview("17").observeForever { resp ->
            Assert.assertNotNull("response is null", resp)
            Assert.assertTrue("invalid response status", resp.status.isSuccessful())
            Assert.assertNull(resp.data)
        }
    }

    @Test
    fun `3 Test getAboutGroup API success`() = runTest {
        val application = ApplicationProvider.getApplicationContext<Context>()
        Assert.assertNotNull(application)

        mockApiService.forceApiFailure = false
        mockApiService.forceInvalidResponse = false

        repository.getAboutGroup("17").observeForever { resp ->
            Assert.assertNotNull("response is null", resp)
            Assert.assertTrue("invalid response status", resp.status.isSuccessful())
            Assert.assertNotNull("response data missing",resp.data)
        }
    }

    @Test
    fun `4 Test getAboutGroup API failure`() = runTest {

        mockApiService.forceApiFailure = true
        mockApiService.forceInvalidResponse = false

        repository.getAboutGroup("17").observeForever { resp ->
            Assert.assertNotNull("response is null", resp)
            Assert.assertTrue("invalid response status", resp.status.isError())
            Assert.assertNull(resp.data)
        }
    }

    @Test
    fun `5 Test getAboutGroup API invalid response`() = runTest {

        mockApiService.forceApiFailure = false
        mockApiService.forceInvalidResponse = true

        repository.getAboutGroup("17").observeForever { resp ->
            Assert.assertNotNull("response is null", resp)
            Assert.assertTrue("invalid response status", resp.status.isSuccessful())
            Assert.assertNull(resp.data)
        }
    }
}