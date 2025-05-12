package com.example.jtt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jtt.repository.FirebaseRepository
import kotlinx.coroutines.launch

class ConfessionViewModel : ViewModel() {
    private val repo = FirebaseRepository()
    var roast by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    fun postConfession(text: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                roast = repo.postConfession(text).toString()
            } catch (e: Exception) {
                roast = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
