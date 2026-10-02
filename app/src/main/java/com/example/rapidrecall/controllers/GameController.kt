package com.example.rapidrecall.controllers

import com.example.rapidrecall.models.Attempt
import com.example.rapidrecall.models.GameLog
import com.example.rapidrecall.models.Sequence
import com.example.rapidrecall.models.SequenceHolder

class GameController(
    private val gameLog: GameLog,
    private val sequenceHolder: SequenceHolder
) {
    fun startGame(sequenceLength: Int) {
        // Create a new sequence object when game is started, and update holder
        val sequence = Sequence(sequenceLength)
        sequenceHolder.setSequence(sequence)
    }

    fun submitAttempt(sequence: Sequence, userGuess: String) {

        // Record the attempt
        val attempt = Attempt(
            sequenceLength = sequence.length,
            userGuess = userGuess,
            targetSequence = sequence.getAsString(),
            isCorrect = sequence.isMatch( userGuess )
        )

        // Add attempt to game log
        gameLog.addAttempt(attempt)
    }

    fun resetGame() {
        // Clear the sequence when the game is reset
        sequenceHolder.clearSequence()
    }
}