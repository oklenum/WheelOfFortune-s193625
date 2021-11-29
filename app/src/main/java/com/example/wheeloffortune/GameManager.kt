package com.example.wheeloffortune

import kotlin.random.Random

class GameManager {

    private var lettersUsed: String = ""
    private lateinit var underscoreWord: String
    private lateinit var wordToGuess: String
    private val maxLifes = 0
    private var currentLifes = 5
    private var currentScore = 0

    fun startNewGame(): GameState {
        lettersUsed = ""
        currentLifes = 5
        currentScore = 0
        val randomIndex = Random.nextInt(0, Phases.words.size)
        wordToGuess = Phases.words[randomIndex]
        generateUnderscores(wordToGuess)
        return getGameState()
    }
    fun generateUnderscores(word: String) {
        val stringBuilder = StringBuilder()
        word.forEach { char ->
            if (char == ' ') {
                stringBuilder.append(" ")
            } else {
                stringBuilder.append("_")
            }
        }
        underscoreWord = stringBuilder.toString()
    }

/*
    fun generateValue(value: Int) {
        var wheelOutcome = Values.values.random()
        var value = wheelOutcome
        val scoreList = mutableListOf<Int>()
        scoreList.add(value)
    }

 */



    fun play(letter: Char): GameState {
        if (lettersUsed.contains(letter)) {
            return GameState.Running(lettersUsed, underscoreWord, currentLifes, currentScore)
        }
        lettersUsed += letter
        val indexes = mutableListOf<Int>()

        wordToGuess.forEachIndexed { index, char ->
            if (char.equals(letter, true)) {
                indexes.add(index)
            }
        }
        var finalUnderscoreWord = "" + underscoreWord
        indexes.forEach { index ->
            val stringBuilder = StringBuilder(finalUnderscoreWord).also { it.setCharAt(index, letter) }
            finalUnderscoreWord = stringBuilder.toString()
        }

        if (indexes.isEmpty()) {
            currentLifes--
        }

        if (indexes.isNotEmpty()) {
            currentScore
        }

        underscoreWord = finalUnderscoreWord
        return getGameState()

    }

    private fun getGameState(): GameState {
        if (underscoreWord.equals(wordToGuess, true)) {
            return GameState.Won(wordToGuess)
        }

        if (currentLifes == maxLifes) {
            return GameState.Lost(wordToGuess)
        }

        return GameState.Running(lettersUsed, underscoreWord, currentLifes, currentScore)
    }
}