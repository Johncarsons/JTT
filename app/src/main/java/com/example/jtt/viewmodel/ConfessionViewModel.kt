package com.example.jtt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jtt.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ConfessionViewModel @Inject constructor(
    private val repo: FirebaseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun postConfession(text: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = repo.postConfession(text)

                // Check if the result was successful or not
                if (result.isSuccess) {
                    val value = result.getOrNull()
                    _uiState.value = UiState.Success(value ?: "No confession text returned.")
                } else {
                    val error = result.exceptionOrNull()?.message ?: "An unknown error occurred"
                    _uiState.value = UiState.Error(error)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An unknown error occurred")
            }

//            _uiState.value = when (val result = repo.postConfession(text)) {
//                is Result.Success -> UiState.Success(result.value)
//                else -> UiState.Error(result.exception.message)
//            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val roast: String) : UiState()
        data class Error(val message: String?) : UiState()
    }
}
