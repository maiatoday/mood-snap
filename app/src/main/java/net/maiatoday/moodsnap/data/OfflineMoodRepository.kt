package net.maiatoday.moodsnap.data

import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class OfflineMoodRepository @Inject constructor(
    private val moodEntryDao: MoodEntryDao,
    private val tagDao: TagDao
) : MoodRepository {
    override fun getAllEntries(): Flow<List<MoodEntry>> = moodEntryDao.getAllEntries()

    override fun getEntryById(id: Int): Flow<MoodEntry?> = moodEntryDao.getEntryById(id)

    override fun getEntriesFromDate(startDate: Date): Flow<List<MoodEntry>> = moodEntryDao.getEntriesFromDate(startDate)

    override suspend fun insert(entry: MoodEntry): Long = moodEntryDao.insert(entry)

    override suspend fun update(entry: MoodEntry) = moodEntryDao.update(entry)

    override suspend fun delete(entry: MoodEntry) = moodEntryDao.delete(entry)

    // Tag related methods
    override fun getAllTags(): Flow<List<Tag>> = tagDao.getAllTags()

    override fun getAllEntriesWithTags(): Flow<List<MoodEntryWithTags>> = moodEntryDao.getEntriesWithTags()

    override fun getEntryWithTagsById(id: Int): Flow<MoodEntryWithTags?> = moodEntryDao.getEntryWithTagsById(id)

    override fun getEntriesWithTagsFromDate(startDate: Date): Flow<List<MoodEntryWithTags>> = moodEntryDao.getEntriesWithTagsFromDate(startDate)

    override suspend fun insertTag(tag: Tag) = tagDao.insert(tag)

    override suspend fun addTagToEntry(entryId: Int, tagName: String) {
        val crossRef = MoodEntryTagCrossRef(moodEntryId = entryId, tagName = tagName)
        moodEntryDao.insertMoodEntryTagCrossRef(crossRef)
    }

    override suspend fun removeTagFromEntry(entryId: Int, tagName: String) {
        val crossRef = MoodEntryTagCrossRef(moodEntryId = entryId, tagName = tagName)
        moodEntryDao.deleteMoodEntryTagCrossRef(crossRef)
    }

    @Transaction
    override suspend fun updateTagsForEntry(entryId: Int, tags: List<String>) {
        // Clear existing tags for this entry
        moodEntryDao.deleteTagsForEntry(entryId)
        // Add new tags
        tags.forEach { tagName ->
            val crossRef = MoodEntryTagCrossRef(moodEntryId = entryId, tagName = tagName)
            moodEntryDao.insertMoodEntryTagCrossRef(crossRef)
        }
    }
}
