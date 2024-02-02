package com.example.aldrovandishunt.ui.myCollection

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aldrovandishunt.data.database.Carte
import com.example.aldrovandishunt.data.database.HuntRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCollectionViewModel @Inject constructor(
    private val huntRepository: HuntRepository
) : ViewModel() {
    private val _unlockedCardsFlow = huntRepository.getUnlockedCards()
    var unlockedCards: List<Carte> = listOf()
        private set


    //FIXME Utilizzatao solo per testare le card
    val cardList: List<Carte> = listOf(
        Carte(0, "T-Rex", "descrizione", Rarity.UNCOMMON, "Boh", true),
        Carte(0, "Velociraptor", "descrizione", Rarity.RARE, "Boh", true),
        Carte(0, "Stegosauros", "descrizione", Rarity.EPIC, "Boh", true),
    )


    init {
        viewModelScope.launch {
            _unlockedCardsFlow.collect { unlockedCards = it }
        }
    }

    private var _bottomSheetUiState : MutableStateFlow<BottomSheetState> = MutableStateFlow(
        BottomSheetState()
    )
    val bottomSheetUiState: StateFlow<BottomSheetState> = _bottomSheetUiState.asStateFlow()


    fun onCardClicked(card: Carte) {
        _bottomSheetUiState.value = _bottomSheetUiState.value.copy(
            isBottomSheetOpen = true,
            card = card
        )
    }

    fun closeBottomSheet() {
        _bottomSheetUiState.value = _bottomSheetUiState.value.copy(
            isBottomSheetOpen = false,
            card = null
        )
    }

    data class BottomSheetState(
        val isBottomSheetOpen: Boolean = false,
        val card: Carte? = null
    )
}