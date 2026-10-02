package com.example.rapidrecall.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.rapidrecall.views.ViewObserver
import kotlin.random.Random

open class ObservableModel<M> {
    // List to keep track of observers
    private val _observers = mutableListOf<ViewObserver<M>>()

    // All models keep track of their own views
    fun addObserver( o: ViewObserver<M> ) {
        _observers.add(o)
    }
    fun removeObserver( o: ViewObserver<M> ) {
        _observers.remove(o)
    }

    // All models notify their views to update
    protected fun notifyObservers ( model: M ) {
        for (o in _observers) {
            o.update(model)
        }
    }
}

class Attempt (
    val sequenceLength: Int,
    val userGuess: String,
    val targetSequence: String,
    val isCorrect: Boolean,
    val timeStamp: Long = System.currentTimeMillis() //  taken from moment object is created
)

class Sequence(val length: Int){
    // List of digits in the sequence
    val digits = List(length) { Random.nextInt(10) }

    // Helper functions
    fun getAsString(): String = digits.joinToString("").trim()
    fun isMatch(userGuess: String): Boolean = (userGuess == getAsString())
}

class SequenceHolder: ObservableModel<Sequence?> () {
    // Holds the current sequence
    var currentSequence by mutableStateOf<Sequence?>(null)

    fun setSequence(newSequence: Sequence) {
        // Sets a new sequence and notifies observers
        currentSequence = newSequence
        notifyObservers(currentSequence)
    }

    fun clearSequence() {
        // Clears the current sequence and notifies observers
        currentSequence = null
        notifyObservers(null)
    }
}

class GameLog: ObservableModel<GameLog> () {
    // List of attempts
    private val _attempts = mutableListOf<Attempt>()

    // Read-only attempt list
    val attempts: List<Attempt>
        get() = _attempts
    fun addAttempt( newAttempt: Attempt ) {
        // Add a new attempt
        _attempts.add(newAttempt)
        notifyObservers(this)
    }

    // Helper functions
    fun getTotalAttempts(): Int =  _attempts.size
    fun getCorrectCount(): Int = _attempts.count { it.isCorrect } // count number of correct occurrences
    fun getAccuracyPercentage(): Double {
        return if (_attempts.isEmpty()) {
            0.0
        } else {
            (getCorrectCount().toDouble() / getTotalAttempts()) * 100 // must cast to double
        }
    }
}