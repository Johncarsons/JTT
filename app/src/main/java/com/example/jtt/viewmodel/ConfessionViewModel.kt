package com.example.jtt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jtt.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//class ConfessionViewModel : ViewModel() {
//    private val repo = FirebaseRepository()
//    var roast by mutableStateOf("")
//    var isLoading by mutableStateOf(false)
//
//    fun postConfession(text: String) {
//        viewModelScope.launch {
//            isLoading = true
//            try {
//                roast = repo.postConfession(text).toString()
//            } catch (e: Exception) {
//                roast = "Error: ${e.message}"
//            } finally {
//                isLoading = false
//            }
//        }
//    }
//}
@HiltViewModel
class ConfessionViewModel @Inject constructor(
    private val repo: FirebaseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun postConfession(text: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = when (val result = repo.postConfession(text)) {
                is Result.Success -> UiState.Success(result.value)
                is Result.Failure -> UiState.Error(result.exception.message)
            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val roast: String) : UiState()
        data class Error(val message: String?) : UiState()
    }
}
