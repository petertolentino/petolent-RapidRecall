package com.example.rapidrecall.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.rapidrecall.models.GameLog
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StatsView: ViewObserver<GameLog> {
    // Set private variable to hold the current log
    private var currentLog by mutableStateOf<GameLog?>(null)

    override fun update(model: GameLog) {
        // Update the observed model to the current log
        currentLog = model
    }

    @Composable
    fun RenderFeedback(
        // Renders feedback for feedback screen
        gameLog: GameLog,
        onBackToStart: () -> Unit
    ) {
        FeedbackScreen(
            gameLog = currentLog ?: gameLog,
            onBackToStart = onBackToStart
        )
    }

    @Composable
    fun RenderSummary(
        // Renders feedback for summary screen
        gameLog: GameLog,
        onBackToStart: () -> Unit
    ) {
        SummaryScreen(
            gameLog = currentLog ?: gameLog,
            onBackToStart = onBackToStart
        )
    }

    @Composable
    fun RenderLog(
        // Renders feedback for log screen
        gameLog: GameLog,
        onBackToStart: () -> Unit
    ) {
        LogScreen(
            gameLog = currentLog ?: gameLog,
            onBackToStart = onBackToStart
        )
    }
}

@Composable
fun FeedbackScreen(
    gameLog: GameLog,
    onBackToStart: () -> Unit = {}
) {
    // Get most recent attempt
    val attempt = gameLog.attempts.last()

    // Format the feedback
    val result = if (attempt.isCorrect) "Correct" else "Incorrect"

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display Result
        Text(
            text = result,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.padding(vertical = 5.dp))

        // Display Comparison
        Text(
            text = "Actual Sequence: ${attempt.targetSequence}\n" +
                    "Your Guess: ${attempt.userGuess}",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.padding(vertical = 5.dp))

        // Back to Start Menu Button
        Button(
            onClick = { onBackToStart() }
        ) {
            Text("Back To Start Menu")
        }
    }
}

@Composable
@Preview
fun FeedbackScreenPreview() {
    FeedbackScreen(gameLog = GameLog())
}
@Composable
fun SummaryScreen(
    gameLog: GameLog,
    onBackToStart: () -> Unit = {}
) {
    val totalAttempts = gameLog.getTotalAttempts()
    val totalCorrect = gameLog.getCorrectCount()

    // Formats accuracy to 1 decimal place with a % sign
    val formattedAccuracy = String.format(Locale.getDefault(), "%.1f%%", gameLog.getAccuracyPercentage())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display Summary Statistics
        Text(
            text = "Total number of attempts: $totalAttempts",
            fontSize = 16.sp,
            modifier = Modifier.padding(5.dp)
        )
        Text(
            text = "Total number of correct attempts: $totalCorrect",
            fontSize = 16.sp,
            modifier = Modifier.padding(5.dp)
        )
        Text(
            text = "Accuracy: $formattedAccuracy",
            fontSize = 16.sp,
            modifier = Modifier.padding(5.dp)
        )

        // Back to Start Menu Button
        Button(
            onClick = { onBackToStart() },
            modifier = Modifier.padding(24.dp)
        ) {
            Text("Back")
        }
    }
}

@Preview
@Composable
fun SummaryScreenPreview() {
    SummaryScreen(GameLog())
}

@Composable
fun LogScreen(
    gameLog: GameLog,
    onBackToStart: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Fixed Screen Title
        Text(
            text = "Attempt History",
            fontSize = 28.sp,
            modifier = Modifier.padding(24.dp)
        )

        if (gameLog.attempts.isEmpty()) {
            // Display message if there are no attempts logged
            Text(
                text = "No attempts logged yet.",
                fontSize = 16.sp,
                modifier = Modifier.padding(24.dp)
            )

        } else {
            // Display Attempt Log
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(gameLog.attempts) { attempt ->

                    // Format attempt log
                    val formattedResult = if (attempt.isCorrect) "Correct" else "Incorrect"
                    val formattedTime = SimpleDateFormat(
                        "MMM dd, h:mm a",
                        Locale.getDefault()).format(Date(attempt.timeStamp)
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                // Display attempt log
                                Text("Sequence Length: ${attempt.sequenceLength}", fontSize = 16.sp)
                                Text("Target: ${attempt.targetSequence}", fontSize = 16.sp)
                                Text("Guess: ${attempt.userGuess}", fontSize = 16.sp)
                                Text("Result: $formattedResult", fontSize = 16.sp)
                                Text("Time Stamp: $formattedTime", fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 5.dp))

        // Back to Start Menu Button
        Button(
            onClick = { onBackToStart() }
        ) {
            Text("Back")
        }
    }
}

@Preview
@Composable
fun LogScreenPreview() {
    LogScreen(GameLog())
}


