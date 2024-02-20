package com.example.aldrovandishunt.ui.caccia

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aldrovandishunt.data.SettingsRepository
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
    private val savedStateHandle: SavedStateHandle,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val cardId = savedStateHandle.get<Int>("cardId") ?: 0
    private val hintFlow = huntRepository.getHint(cardId)
    private val CAPTURE_REWARD: Int = 2

    private val _talk = mutableStateOf(true)
    val startTalking
        get() = _talk


    private var _captureUiState: MutableStateFlow<CaptureUiState> =
        MutableStateFlow(CaptureUiState())
    val captureUiState: StateFlow<CaptureUiState> = _captureUiState.asStateFlow()

    init {
        viewModelScope.launch {
            hintFlow.collect { h ->
                _captureUiState.value = _captureUiState.value.copy(hint = h[0], hintList = h)
            }
        }
        viewModelScope.launch {
            huntRepository.getCard(cardId).collect { c ->
                _captureUiState.value =
                    _captureUiState.value.copy(card = c.copy(isUnlocked = true))
            }
        }
        viewModelScope.launch {
            _captureUiState.value =
                _captureUiState.value.copy(hintCoins = settingsRepository.getHintCoins())
        }
    }



    data class CaptureUiState(
        val hint: CaptureHint = CaptureHint(0, 0, "", 0),
        val hintSelected: Int = 0,
        val hintList: List<CaptureHint> = emptyList(),
        val unlocked: Boolean = false,
        val card: Card = Card(0, "", "", Rarity.UNCOMMON, 0),
        val hintCoins: Int = 0,
        val insufficientCoins: Boolean = false
    )

    fun onCapture() {
        _captureUiState.value = _captureUiState.value.copy(unlocked = true)
        viewModelScope.launch {
            huntRepository.unlockCard(cardId)
            settingsRepository.setHintCoins(_captureUiState.value.hintCoins + CAPTURE_REWARD)
        }
    }

    fun onHintSelected(hintNumber: Int) {
        _captureUiState.value = _captureUiState.value.copy(
            hintSelected = hintNumber,
            hint = _captureUiState.value.hintList[hintNumber],
            insufficientCoins = false
        )
        _talk.value = _captureUiState.value.hint.isUnlocked
    }

    fun buyHint() {
        if (_captureUiState.value.hintCoins < _captureUiState.value.hint.cost) {
            _captureUiState.value = _captureUiState.value.copy(insufficientCoins = true)
            return
        }
        viewModelScope.launch {
            settingsRepository.setHintCoins(_captureUiState.value.hintCoins - _captureUiState.value.hint.cost)
            huntRepository.unlockHint(_captureUiState.value.hint.ID)
        }
        _captureUiState.value = _captureUiState.value.copy(
            hintCoins = _captureUiState.value.hintCoins - _captureUiState.value.hint.cost,
            hint = _captureUiState.value.hint.copy(isUnlocked = true)
        )
        if(_captureUiState.value.hint.isUnlocked){
            _talk.value = true
        }
    }

    fun startTalking(){
        _talk.value = true
    }

    fun stopTalking(){
        _talk.value= false
    }



}