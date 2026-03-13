package net.maiatoday.moodsnap.ui.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import net.maiatoday.moodsnap.data.FakeMoodRepository
import net.maiatoday.moodsnap.domain.GetWeeklySummaryUseCase
import net.maiatoday.moodsnap.domain.ResonanceEngine
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: FakeMoodRepository
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var weeklySummaryUseCase: GetWeeklySummaryUseCase

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeMoodRepository()
    }

    @Test
    fun `initial state is null for new entry`() = runTest {
        weeklySummaryUseCase = GetWeeklySummaryUseCase(repository, ResonanceEngine())
        viewModel = HomeViewModel(weeklySummaryUseCase)
        
        // Start collection in the background to activate WhileSubscribed
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.summary.collect()
        }

        val state = viewModel.summary.value
        assertTrue(state == null, "State should be null")
        collectJob.cancel()
    }

    @Test
    fun `summary state with a week of data`() = runTest {
        repository.generateTestData(7)
        weeklySummaryUseCase = GetWeeklySummaryUseCase(repository, ResonanceEngine())
        viewModel = HomeViewModel(weeklySummaryUseCase)

        // Start collection in the background to activate WhileSubscribed
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.summary.collect()
        }
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.summary.value
        assertTrue(state != null, "State should not be null")
        assertEquals(8, state!!.dailyMoods.size, "Expected 8 days in weekly summary (7 days ago to today)")
    }
}
