package com.example.jtt.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jtt.viewmodel.ConfessionViewModel
import kotlinx.coroutines.launch

@Composable
fun ConfessionScreen(viewModel: ConfessionViewModel = hiltViewModel()) {
    var text by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Your confession") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { scope.launch { viewModel.postConfession(text) } },
            enabled = text.isNotBlank() && uiState !is ConfessionViewModel.UiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState is ConfessionViewModel.UiState.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp))
            } else {
                Text("Get Roasted 🔥")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is ConfessionViewModel.UiState.Success -> {
                Text("🔥 ${state.roast}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp))
            }
            is ConfessionViewModel.UiState.Error -> {
                LaunchedEffect(state){
                    Toast.makeText(
                        context,
                        state.message ?: "Unknown error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else -> {}
        }
    }
}

