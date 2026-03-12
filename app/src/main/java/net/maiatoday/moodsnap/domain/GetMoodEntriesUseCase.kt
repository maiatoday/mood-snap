package net.maiatoday.moodsnap.domain

import kotlinx.coroutines.flow.Flow
import net.maiatoday.moodsnap.data.MoodRepository
import net.maiatoday.moodsnap.data.MoodEntryWithTags
import javax.inject.Inject

class GetMoodEntriesUseCase @Inject constructor(
    private val moodRepository: MoodRepository
) {
    operator fun invoke(): Flow<List<MoodEntryWithTags>> = moodRepository.getAllEntriesWithTags()
}
