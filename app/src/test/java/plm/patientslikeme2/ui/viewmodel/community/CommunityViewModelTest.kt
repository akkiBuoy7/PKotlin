package plm.patientslikeme2.ui.viewmodel.community

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import plm.patientslikeme2.data.api.MockApiService
import plm.patientslikeme2.data.repository.community.CommunityGroupRepository
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@ExperimentalCoroutinesApi
class CommunityViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: CommunityGroupRepository

    @BindValue
    @JvmField
    val mockApiService: MockApiService = MockApiService()

    @Mock
    private lateinit var communityViewModel: CommunityViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        communityViewModel = CommunityViewModel(repository)
    }
}