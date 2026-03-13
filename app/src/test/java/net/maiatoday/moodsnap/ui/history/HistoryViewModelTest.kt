package net.maiatoday.moodsnap.ui.history

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import net.maiatoday.moodsnap.data.FakeMoodRepository
import net.maiatoday.moodsnap.data.MoodEntry
import net.maiatoday.moodsnap.domain.GetMoodEntriesUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

    private lateinit var viewModel: HistoryViewModel
    private lateinit var repository: FakeMoodRepository
    private lateinit var getMoodEntriesUseCase: GetMoodEntriesUseCase
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeMoodRepository()
        getMoodEntriesUseCase = GetMoodEntriesUseCase(repository)
        viewModel = HistoryViewModel(getMoodEntriesUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `entries flow emits data from repository`() = runTest {
        val entry1 = MoodEntry(moodScore = 4, notes = "Good", movement = false, sunlight = true, sleep = true, energy = 0, timestamp = Date())
        val entry2 = MoodEntry(moodScore = 2, notes = "Bad", movement = false, sunlight = false, sleep = false, energy = 0, timestamp = Date())
        repository.insert(entry1)
        repository.insert(entry2)
        
        val job = launch {
            viewModel.entries.collect {}
        }
        
        testDispatcher.scheduler.advanceUntilIdle()

        val entries = viewModel.entries.value
        assertEquals(2, entries.size)
        assertEquals(4, entries[0].moodEntry.moodScore)
        assertEquals(2, entries[1].moodEntry.moodScore)
        
        job.cancel()
    }
}
