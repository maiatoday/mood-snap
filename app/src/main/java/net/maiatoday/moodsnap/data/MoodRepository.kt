package net.maiatoday.moodsnap.data

import kotlinx.coroutines.flow.Flow
import java.util.Date

interface MoodRepository {
    fun getAllEntries(): Flow<List<MoodEntry>>
    fun getEntryById(id: Int): Flow<MoodEntry?>
    fun getEntriesFromDate(startDate: Date): Flow<List<MoodEntry>>
    suspend fun insert(entry: MoodEntry): Long
    suspend fun update(entry: MoodEntry)
    suspend fun delete(entry: MoodEntry)
}
