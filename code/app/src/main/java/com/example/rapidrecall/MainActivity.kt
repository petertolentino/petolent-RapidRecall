package com.example.rapidrecall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.rapidrecall.controllers.GameController
import com.example.rapidrecall.models.GameLog
import com.example.rapidrecall.models.SequenceHolder
import com.example.rapidrecall.ui.theme.RapidRecallTheme
import com.example.rapidrecall.views.GameView
import com.example.rapidrecall.views.SetupScreen
import com.example.rapidrecall.views.StartScreen
import com.example.rapidrecall.views.StatsView

enum class Screen { START, SETUP, GAME, FEEDBACK, SUMMARY, LOG }
class MainActivity : ComponentActivity() {
    // Initialize View Observers
    private val gameView = GameView()
    private val statsView = StatsView()

    // Initialize Models
    private val gameLog = GameLog()
    private val sequenceHolder = SequenceHolder()

    // Initialize Controller
    private val gameController = GameController(gameLog, sequenceHolder)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Register Observers
        sequenceHolder.addObserver(gameView)
        gameLog.addObserver(statsView)

        setContent {
            RapidRecallTheme {
                var currentScreen by remember { mutableStateOf(Screen.START) }

                // Switch Case to Handle Screen Flow
                when (currentScreen) {
                    // Start Screen Case
                    Screen.START -> {
                        StartScreen(
                            onGameSetup = { currentScreen = Screen.SETUP },
                            onViewLogs = { currentScreen = Screen.LOG },
                            onViewSummary = { currentScreen = Screen.SUMMARY }
                        )
                    }

                    // Setup Screen Case
                    Screen.SETUP -> {
                        SetupScreen(
                            gameController,
                            onGameStarted = { currentScreen = Screen.GAME }
                        )
                    }

                    // Game Screen Case
                    Screen.GAME -> {
                        gameView.Render(
                            gameController,
                            onGameFinished = { currentScreen = Screen.FEEDBACK }
                        )
                    }

                    // Feedback Screen Case
                    Screen.FEEDBACK -> {
                        statsView.RenderFeedback(
                            gameLog,
                            onBackToStart = { currentScreen = Screen.START }
                        )
                    }

                    // Log Screen Case
                    Screen.LOG -> {
                        statsView.RenderLog(
                            gameLog,
                            onBackToStart = { currentScreen = Screen.START }
                        )
                    }

                    // Summary Screen Case
                    Screen.SUMMARY -> {
                        statsView.RenderSummary(
                            gameLog,
                            onBackToStart = { currentScreen = Screen.START }
                        )
                    }
                }
            }
        }
    }
}