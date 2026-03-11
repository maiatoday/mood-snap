package net.maiatoday.moodsnap.data

import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class OfflineMoodRepository @Inject constructor(
    private val moodEntryDao: MoodEntryDao
) : MoodRepository {
    override fun getAllEntries(): Flow<List<MoodEntry>> = moodEntryDao.getAllEntries()

    override fun getEntryById(id: Int): Flow<MoodEntry?> = moodEntryDao.getEntryById(id)

    override fun getEntriesFromDate(startDate: Date): Flow<List<MoodEntry>> = moodEntryDao.getEntriesFromDate(startDate)

    override suspend fun insert(entry: MoodEntry): Long = moodEntryDao.insert(entry)

    override suspend fun update(entry: MoodEntry) = moodEntryDao.update(entry)

    override suspend fun delete(entry: MoodEntry) = moodEntryDao.delete(entry)
}
