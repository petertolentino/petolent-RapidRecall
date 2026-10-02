package com.example.rapidrecall.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rapidrecall.controllers.GameController
import com.example.rapidrecall.models.GameLog
import com.example.rapidrecall.models.Sequence
import com.example.rapidrecall.models.SequenceHolder
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class GameView: ViewObserver<Sequence?> {

    // Initialize variable to hold a new sequence
    var newSequence by mutableStateOf<Sequence?>(null)

    override fun update(model: Sequence?) {
        // Update the sequence to the new sequence
        newSequence = model
    }

    @Composable
    fun Render(
        gameController: GameController,
        onGameFinished: () -> Unit = {}
    ) {
        // Update the current sequence to the new sequence
        val currentSequence = newSequence

        if (currentSequence != null) {
            // Display the game screen
            GameScreen(
                gameController = gameController,
                sequence = currentSequence,
                onGameFinished = onGameFinished
            )
        } else {
            // Placeholder UI until a Sequence model is observed
            Text(
                text = "Waiting for sequence...",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            )
        }
    }
}

@Composable
fun GameScreen(
    gameController: GameController,
    sequence: Sequence,
    onGameFinished: () -> Unit = {}
    ) {
    // Initialize variables/values for displaying digit
    var displayedDigit by remember { mutableStateOf<Int?>(null) }
    val duration = 1000L.milliseconds // 1000L = 1000ms = 1s

    // Initialize variable/values for user input
    var isInputEnabled by remember { mutableStateOf(false) }
    var userGuess by remember { mutableStateOf("") }
    val isGuessValid = (userGuess.length == sequence.digits.size)

    LaunchedEffect(sequence) {

        // Iterate through the digits in the sequence
        for (digit in sequence.digits) {
            displayedDigit = digit
            delay(duration) // brief pause to display the digit
            displayedDigit = null
            delay(duration) // brief pause to help distinguish transition between digits
        }

        displayedDigit = null // do not display anymore digits
        isInputEnabled = true // enable input
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Area for displaying sequence
        Box(
            modifier = Modifier.height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayedDigit?.toString() ?: "", // Fall back to empty string when null
                fontSize = 48.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Text field for user to input guess
        OutlinedTextField(
            value = userGuess,
            onValueChange = { newGuess ->
                // 1. Only allow numeric input
                // 2. Only accept input length if it doesn't exceed target sequence size
                if (newGuess.all { it.isDigit() } && newGuess.length <= sequence.digits.size) {
                    userGuess = newGuess
                }
            },
            isError = !isGuessValid,
            label = { Text("Enter Guess: ") },
            enabled = isInputEnabled
        )

        // Determine if user guess is valid or invalid
        if (!isGuessValid) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Please enter a valid guess.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to Submit Guess
        Button(
            onClick = {
                gameController.submitAttempt(sequence, userGuess)
                gameController.resetGame()
                onGameFinished()
            },
            enabled = isInputEnabled && isGuessValid
        ) {
            Text("Submit")
        }
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    val mockSequence = Sequence(length = 5)

    GameScreen(
        GameController(
            GameLog(),
            SequenceHolder()
        ),
        mockSequence
    )
}