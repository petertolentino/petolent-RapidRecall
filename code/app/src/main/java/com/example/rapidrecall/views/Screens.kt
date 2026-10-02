package com.example.rapidrecall.views

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.rapidrecall.models.GameLog
import com.example.rapidrecall.models.SequenceHolder
import com.example.rapidrecall.controllers.GameController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

interface ViewObserver<M> {
    fun update ( model: M )
}

// Start Screen
@Composable
fun StartScreen(
    onGameSetup: () -> Unit = {},
    onViewLogs: () -> Unit = {},
    onViewSummary: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Title
        Text(
            text = "Rapid Recall",
            fontSize = 42.sp,
            modifier = Modifier.padding(vertical = 30.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Options:
        // 1. Play Game
        Button(
            onClick = { onGameSetup() }
        ) {
            Text("Play Game")
        }
        Spacer(modifier = Modifier.height(10.dp))

        // 2. View Game Logs
        Button(
            onClick = { onViewLogs() }
        ) {
            Text("Game Logs")
        }
        Spacer(modifier = Modifier.height(10.dp))

        // 3. View Summary
        Button(
            onClick = { onViewSummary() }
        ) {
            Text("Summary")
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview
@Composable
fun StartScreenPreview() {
    StartScreen()
}


// Setup Screen
@Composable
fun SetupScreen(
    gameController: GameController,
    onGameStarted: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Initialize variables to receive sequence length from input
        var sequenceLength by remember { mutableStateOf("") }
        val isLengthValid = sequenceLength.isNotEmpty() &&
                (sequenceLength.toIntOrNull() ?: 0) in 1..10

        // Text field to input sequence length
        OutlinedTextField(
            value = sequenceLength,
            onValueChange = { sequenceLength = it },
            isError = !isLengthValid,
            label = { Text("Enter Sequence Length (1-10): ") }
        )

        // Display message if input is invalid
        if (!isLengthValid) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Please enter a valid length.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // Start Button
        Button(
            onClick = {
                gameController.startGame(sequenceLength.toInt())
                onGameStarted()
            },
            enabled = isLengthValid, // only enable when input is valid
        ) {
            Text("Start Game")
        }
    }
}

@Preview
@Composable
fun SetupScreenPreview() {
    SetupScreen(
        GameController(
            GameLog(),
            SequenceHolder()
        )
    )
}
