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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.maiatoday.moodsnap.R
import net.maiatoday.moodsnap.domain.Mood
import net.maiatoday.moodsnap.domain.MoodEntryDomain
import net.maiatoday.moodsnap.ui.theme.MoodSnapTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a")

@Composable
fun MoodEntryItem(
    entryDomain: MoodEntryDomain,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val mood = entryDomain.mood
    val timestamp = remember(entryDomain.timestamp) {
        entryDomain.timestamp.atZone(ZoneId.systemDefault()).format(dateTimeFormatter)
    }
    
    Card(
        modifier = modifier
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
            MoodColorBar(color = mood.color)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MoodScoreDisplay(mood = mood)
                    MoodDetails(
                        entryDomain = entryDomain,
                        timestamp = timestamp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MoodColorBar(color: Color) {
    Spacer(
        modifier = Modifier
            .width(16.dp)
            .fillMaxHeight()
            .background(color)
    )
}

@Composable
private fun MoodScoreDisplay(mood: Mood) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(end = 24.dp)
    ) {
        Text(
            text = mood.emoji,
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = mood.score.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MoodDetails(
    entryDomain: MoodEntryDomain,
    timestamp: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entryDomain.mood.description,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = timestamp,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        TagsRow(tags = entryDomain.tags)
        Spacer(modifier = Modifier.height(8.dp))
        NotesRow(notes = entryDomain.notes)
        Spacer(modifier = Modifier.height(16.dp))
        EnergyIndicator(energy = entryDomain.energy)
        Spacer(modifier = Modifier.height(16.dp))
        ActivitiesRow(entryDomain = entryDomain)
    }
}

@Composable
private fun TagsRow(tags: List<String>) {
    val tagsString = remember(tags) { tags.joinToString() }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Label,
            contentDescription = stringResource(R.string.tags),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = tagsString,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun NotesRow(notes: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = stringResource(R.string.notes),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = notes,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun EnergyIndicator(energy: Int) {
    Text(
        text = "Energy: $energy/15",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(4.dp))
    LinearProgressIndicator(
        progress = { energy / 15f },
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp),
        strokeCap = StrokeCap.Round
    )
}

@Composable
private fun ActivitiesRow(entryDomain: MoodEntryDomain) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BooleanChip(label = "Sleep", value = entryDomain.sleep)
        BooleanChip(label = "Movement", value = entryDomain.movement)
        BooleanChip(label = "Sunlight", value = entryDomain.sunlight)
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
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
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

@Preview(showBackground = true)
@Composable
fun TagsRowPreview() {
    MoodSnapTheme {
        TagsRow(tags = listOf("happy", "productive", "energetic", "focused", "joyful", "active", "inspired"))
    }
}

@Preview(showBackground = true)
@Composable
fun NotesRowPreview() {
    MoodSnapTheme {
        NotesRow(notes = "Had a fantastic day!")
    }
}

@Preview(showBackground = true)
@Composable
fun EnergyIndicatorPreview() {
    MoodSnapTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EnergyIndicator(energy = 0)
            EnergyIndicator(energy = 7)
            EnergyIndicator(energy = 15)
        }
    }
}
