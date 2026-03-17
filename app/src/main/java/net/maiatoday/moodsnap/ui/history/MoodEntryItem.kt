package net.maiatoday.moodsnap.ui.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.maiatoday.moodsnap.domain.Mood
import net.maiatoday.moodsnap.domain.MoodEntryDomain
import net.maiatoday.moodsnap.ui.theme.MoodSnapTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MoodEntryItem(entryDomain: MoodEntryDomain, onClick: () -> Unit) {
    val mood = entryDomain.mood
    val timestamp = entryDomain.timestamp.atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
                    .background(mood.color)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = mood.emoji,
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = mood.score.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = timestamp,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Text(text = mood.description, style = MaterialTheme.typography.headlineSmall)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Label,
                            contentDescription = "Tags"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = entryDomain.tags.joinToString(),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Notes"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(entryDomain.notes)
                    }
                    Text("Energy: ${entryDomain.energy}/15")
                    LinearProgressIndicator(
                        progress = { entryDomain.energy / 15f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BooleanChip(label = "Sleep", value = entryDomain.sleep)
                        BooleanChip(label = "Movement", value = entryDomain.movement)
                        BooleanChip(label = "Sunlight", value = entryDomain.sunlight)
                    }
                }
            }
        }
    }
}

@Composable
fun BooleanChip(label: String, value: Boolean, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = if (value) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        contentColor = if (value) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
        border = if (value) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MoodEntryItemPreview() {
    MoodSnapTheme {
        val sampleMoodEntry = MoodEntryDomain(
            id = 1,
            mood = Mood.GREAT,
            notes = "Had a fantastic day!",
            movement = true,
            sunlight = false,
            sleep = true,
            energy = 5,
            timestamp = Instant.now(),
            tags = listOf("happy", "productive", "energetic", "focused", "joyful", "active", "inspired")
        )
        MoodEntryItem(entryDomain = sampleMoodEntry, onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun BooleanChipPreview() {
    MoodSnapTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            BooleanChip(label = "True Chip", value = true)
            BooleanChip(label = "False Chip", value = false)
        }
    }
}
