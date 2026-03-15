package net.maiatoday.moodsnap.domain

import net.maiatoday.moodsnap.data.MoodEntry
import net.maiatoday.moodsnap.data.Tag

data class MoodEntryDomain(
    val moodEntry: MoodEntry,
    val tags: List<Tag>,
    val moodDescription: String
)