package net.maiatoday.moodsnap.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import net.maiatoday.moodsnap.domain.GetWeeklySummaryUseCase
import net.maiatoday.moodsnap.domain.WeeklySummary
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getWeeklySummaryUseCase: GetWeeklySummaryUseCase
) : ViewModel() {

    val summary: StateFlow<WeeklySummary?> = getWeeklySummaryUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
