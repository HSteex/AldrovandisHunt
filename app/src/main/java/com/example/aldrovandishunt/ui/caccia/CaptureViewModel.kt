package com.example.aldrovandishunt.ui.caccia

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aldrovandishunt.data.database.CaptureHint
import com.example.aldrovandishunt.data.database.Card
import com.example.aldrovandishunt.data.database.HuntRepository
import com.example.aldrovandishunt.data.database.Rarity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CaptureViewModel @Inject constructor(
    private val huntRepository: HuntRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    val cardId= savedStateHandle.get<Int>("cardId") ?: 0
    val hintFlow = huntRepository.getHint(cardId)

    private var _captureUiState: MutableStateFlow<CaptureUiState> = MutableStateFlow(CaptureUiState())
    val captureUiState: StateFlow<CaptureUiState> = _captureUiState.asStateFlow()

    init {
        viewModelScope.launch {
            hintFlow.collect{ h->
                _captureUiState.value = _captureUiState.value.copy(hint = h[0].hint, hintList = h)
            }
        }
        viewModelScope.launch {
            huntRepository.getCard(cardId).collect{c->
                _captureUiState.value = _captureUiState.value.copy(card = c.copy(isUnlocked = true))
            }
        }
    }


    data class CaptureUiState(
        val hint: String = "",
        val hintSelected: Int = 0,
        val hintList: List<CaptureHint> = emptyList(),
        val unlocked: Boolean=false,
        val card: Card=Card(0,"","",Rarity.UNCOMMON,"",)
    )

    fun onCapture() {
        _captureUiState.value = _captureUiState.value.copy(unlocked = true)
        viewModelScope.launch {
            huntRepository.unlockCard(cardId)
        }
    }

}