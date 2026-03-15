package net.maiatoday.moodsnap.domain

import net.maiatoday.moodsnap.data.MoodEntryWithTags

fun MoodEntryWithTags.toDomain(): MoodEntryDomain {
    val moodDescription = when (moodEntry.moodScore) {
        1 -> "Bad"
        2 -> "Meh"
        3 -> "OK"
        4 -> "Good"
        5 -> "Great"
        else -> ""
    }
    return MoodEntryDomain(
        moodEntry = this.moodEntry,
        tags = this.tags,
        moodDescription = moodDescription
    )
}