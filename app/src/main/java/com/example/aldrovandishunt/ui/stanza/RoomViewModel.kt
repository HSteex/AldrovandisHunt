package com.example.aldrovandishunt.ui.stanza

import android.util.Log
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

    val roomName: String = savedStateHandle.get<String>("roomName") ?: "Room"

    val cardFlow = huntRepository.getCards(roomName)

    private var _bottomSheetUiState: MutableStateFlow<MyCollectionViewModel.BottomSheetState> = MutableStateFlow(
        MyCollectionViewModel.BottomSheetState()
    )
    val bottomSheetUiState: StateFlow<MyCollectionViewModel.BottomSheetState> = _bottomSheetUiState.asStateFlow()

    private val _roomUiState: MutableStateFlow<RoomUiState> = MutableStateFlow(
        RoomUiState()
    )
    val roomUiState: StateFlow<RoomUiState> = _roomUiState.asStateFlow()

    init {
        Log.v("DELETE", roomName)
        viewModelScope.launch {
            cardFlow.collect { cards ->
                _roomUiState.value = _roomUiState.value.copy(cards = cards)
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
        val cards: List<Card> = emptyList()
    )

    data class BottomSheetState(
        val isBottomSheetOpen: Boolean = false,
        val card: Card? = null
    )
}