package com.example.aldrovandishunt.ui.mappa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aldrovandishunt.data.database.HuntRepository
import com.example.aldrovandishunt.data.database.Rooms
import com.example.aldrovandishunt.ui.intro.IntroViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val huntRepository: HuntRepository,
) : ViewModel() {

    private val _mapUiState: MutableStateFlow<MapUiState> = MutableStateFlow(
        MapUiState()
    )
    val mapUiState: StateFlow<MapUiState> = _mapUiState.asStateFlow()

    init {
        viewModelScope.launch {
            huntRepository.getRooms().collect { rooms ->
                _mapUiState.value = _mapUiState.value.copy(rooms = rooms)
                rooms.forEach { room ->
                    viewModelScope.launch {
                        huntRepository.getCardCount(room.id).collect { count ->
                            _mapUiState.value = _mapUiState.value.copy(
                                cardToUnlock = _mapUiState.value.cardToUnlock + (room to count)
                            )
                        }
                    }
                    viewModelScope.launch {
                        huntRepository.getUnlockedCardCount(room.id).collect { count ->
                            _mapUiState.value = _mapUiState.value.copy(
                                unlockedCard = _mapUiState.value.unlockedCard + (room to count)
                            )
                        }
                    }
                }
            }
        }
        viewModelScope.launch {

        }

    }

    data class MapUiState(
        val rooms: List<Rooms> = emptyList(),
        val cardToUnlock: Map<Rooms,Int> = emptyMap(),
        val unlockedCard: Map<Rooms,Int> = emptyMap(),
    )



}