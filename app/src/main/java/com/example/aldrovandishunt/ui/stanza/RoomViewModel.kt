package com.example.aldrovandishunt.ui.stanza

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aldrovandishunt.data.database.Card
import com.example.aldrovandishunt.data.database.HuntRepository
import com.example.aldrovandishunt.ui.myCollection.MyCollectionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val huntRepository: HuntRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val roomId: Int = savedStateHandle.get<Int>("roomId") ?: 0

    val cardFlow = huntRepository.getCards(roomId)

    private var _bottomSheetUiState: MutableStateFlow<MyCollectionViewModel.BottomSheetState> = MutableStateFlow(
        MyCollectionViewModel.BottomSheetState()
    )
    val bottomSheetUiState: StateFlow<MyCollectionViewModel.BottomSheetState> = _bottomSheetUiState.asStateFlow()

    private val _roomUiState: MutableStateFlow<RoomUiState> = MutableStateFlow(
        RoomUiState()
    )
    val roomUiState: StateFlow<RoomUiState> = _roomUiState.asStateFlow()

    init {
        viewModelScope.launch {
            cardFlow.collect { cards ->
                _roomUiState.value = _roomUiState.value.copy(cards = cards)
            }
        }
        viewModelScope.launch {
            huntRepository.getRoomName(roomId).collect { room ->
                _roomUiState.value = _roomUiState.value.copy(roomName = room)
            }
        }
    }

    fun closeBottomSheet() {
        _bottomSheetUiState.value = _bottomSheetUiState.value.copy(
            isBottomSheetOpen = false,
            card = null
        )
    }

    fun onUnlockedCardClicked(card: Card){
        _bottomSheetUiState.value = _bottomSheetUiState.value.copy(
            isBottomSheetOpen = true,
            card = card
        )
    }


    data class RoomUiState(
        val roomName: String = "",
        val cards: List<Card> = emptyList()
    )

    data class BottomSheetState(
        val isBottomSheetOpen: Boolean = false,
        val card: Card? = null
    )
}