package com.example.wheeloffortune

sealed class GameState {
    class Running(val lettersUsed: String,
    val underscoreWord: String, val currentLifes: Int, var currentScore: Int) : GameState()
    class Lost(val wordToGuess: String) : GameState()
    class Won(val wordToGuess: String) : GameState()
}