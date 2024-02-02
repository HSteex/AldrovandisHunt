package com.example.aldrovandishunt.ui.intro

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aldrovandishunt.R
import com.example.aldrovandishunt.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val SettingsRepository: SettingsRepository
) : ViewModel() {

    private var _intro = mutableStateOf(false)
    val intro
        get() = _intro
    val PAGES = 3
    private var actualPage: Int = 0

    //Soluzione un po spartana per farlo parlare (vuole un mutableStateOf)
    private val _talk = mutableStateOf(1)
    val talk
        get() = _talk


    private val _introUiState: MutableStateFlow<IntroUiState> = MutableStateFlow(
        IntroUiState()
    )
    val introUiState: StateFlow<IntroUiState> = _introUiState.asStateFlow()


    private val textList = mapOf(
        0 to R.string.introText_0,
        1 to R.string.introText_1,
        2 to R.string.introText_2,
    )
    private val secondsList = mapOf(
        0 to 3,
        1 to 6,
        2 to 3,
    )

    data class IntroUiState(
        val page: Int = 0,
        val textId: Int = 0,
        val seconds: Int = 0,
        val skipButtonVisible: Boolean = false,
        val permissionButtonVisible: Boolean = false,
    )

    init {
        getIntro()
        _introUiState.value = _introUiState.value.copy(
            page = actualPage,
            textId = textList[actualPage]!!,
            seconds = secondsList[actualPage]!!,
        )
        viewModelScope.launch {
            delay(_introUiState.value.seconds * 1000L)
            _introUiState.value = _introUiState.value.copy(
                skipButtonVisible = true,
            )
        }
    }

    fun nextPage() {
        actualPage++
        if (actualPage == PAGES) {
            endIntro()
            return
        }
        _talk.value = _talk.value + 1
        _introUiState.value = _introUiState.value.copy(
            page = actualPage,
            textId = textList[actualPage]!!,
            seconds = secondsList[actualPage]!!,
            skipButtonVisible = false,
        )

        viewModelScope.launch {
            delay(_introUiState.value.seconds * 1000L)
            if (actualPage == PAGES - 1) {
                _introUiState.value = _introUiState.value.copy(
                    permissionButtonVisible = true,
                )
            } else {
                _introUiState.value = _introUiState.value.copy(
                    skipButtonVisible = true,
                )
            }
        }
    }

    fun endIntro() {
        _intro.value=false
        viewModelScope.launch {
            SettingsRepository.setIntro(false)
            Log.v("DataStore", SettingsRepository.getIntro().toString())
        }
    }

    fun acceptedPermissions() {
        _introUiState.value = _introUiState.value.copy(
            permissionButtonVisible = false,
            skipButtonVisible = true,
        )
    }


    fun getIntro() {
        viewModelScope.launch {
            intro.value=SettingsRepository.getIntro()
        }
    }
}