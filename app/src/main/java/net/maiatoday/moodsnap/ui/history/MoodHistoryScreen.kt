package net.maiatoday.moodsnap.ui.history

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import net.maiatoday.moodsnap.domain.Mood
import net.maiatoday.moodsnap.domain.MoodEntryDomain
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodHistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onEntryClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val entries by viewModel.entries.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mood History") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            items(entries) { entryDomain ->
                MoodEntryItem(entryDomain = entryDomain, onClick = { onEntryClick(entryDomain.id) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MoodHistoryScreenPreview() {
    val sampleEntryDomain = MoodEntryDomain(
        id = 1,
        mood = Mood.GOOD,
        notes = "Great day!",
        movement = true,
        sunlight = true,
        sleep = true,
        energy = 5,
        timestamp = Instant.now(),
        tags = listOf("Happy", "Work")
    )
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mood History") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            items(listOf(sampleEntryDomain)) { entry ->
                MoodEntryItem(entryDomain = entry, onClick = {})
            }
        }
    }
}
