package net.maiatoday.moodsnap.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.maiatoday.moodsnap.domain.MoodEntryDomain

@Composable
fun MoodEntryItem(entryDomain: MoodEntryDomain, onClick: () -> Unit) {
    val entry = entryDomain.moodEntry
    val tags = entryDomain.tags.map { it.name }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Score: ${entry.moodScore}")
            Text("Description: ${entryDomain.moodDescription}")
            Text("Tags: ${tags.joinToString()}")
            Text("Notes: ${entry.notes}")
            Text("Energy: ${entry.energy}")
        }
    }
}