package com.example.aldrovandishunt.ui.myCollection

import android.util.Log
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aldrovandishunt.data.database.Card
import com.example.aldrovandishunt.data.database.HuntRepository
import com.example.aldrovandishunt.data.database.Rarity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCollectionViewModel @Inject constructor(
    private val huntRepository: HuntRepository
) : ViewModel() {

    private val _unlockedCardsFlow: Flow<List<Card>> = huntRepository.getUnlockedCards()
    private val _unlockedCardsUiState: MutableStateFlow<CardsUiState> =
        MutableStateFlow(CardsUiState())
    val unlockedCardsUiState: StateFlow<CardsUiState> = _unlockedCardsUiState.asStateFlow()

    var isARScreenOpen = mutableStateOf(false)
        private set

    private var _bottomSheetUiState: MutableStateFlow<BottomSheetState> = MutableStateFlow(
        BottomSheetState()
    )
    val bottomSheetUiState: StateFlow<BottomSheetState> = _bottomSheetUiState.asStateFlow()

    //METHODS
    init {
        viewModelScope.launch {
            _unlockedCardsFlow.collect { cards ->
                _unlockedCardsUiState.value =
                    _unlockedCardsUiState.value.copy(unlockedCards = cards)
                Log.v("MyCollectionViewModel", "init")
            }
        }
    }

    fun onCardClicked(card: Card) {
        _bottomSheetUiState.value = _bottomSheetUiState.value.copy(
            isBottomSheetOpen = true,
            card = card
        )
    }

    fun onARClick() {
        _bottomSheetUiState.value = _bottomSheetUiState.value.copy(
            isBottomSheetOpen = false,
        )
        isARScreenOpen.value = true
    }


    fun onARBackClick() {
        _bottomSheetUiState.value = _bottomSheetUiState.value.copy(
            isBottomSheetOpen = true,
        )
        isARScreenOpen.value = false
    }

    fun closeBottomSheet() {
        _bottomSheetUiState.value = _bottomSheetUiState.value.copy(
            isBottomSheetOpen = false,
            card = null
        )
    }

    //DATA CLASSES
    data class CardsUiState(
        val unlockedCards: List<Card> = listOf(),
    )

    data class BottomSheetState(
        val isBottomSheetOpen: Boolean = false,
        val card: Card? = null
    )
}