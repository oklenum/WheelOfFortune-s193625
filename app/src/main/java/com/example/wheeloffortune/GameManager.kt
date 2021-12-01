package com.example.wheeloffortune

import kotlin.random.Random

class GameManager {

    private var lettersUsed: String = ""
    private lateinit var underscoreWord: String
    private lateinit var wordToGuess: String
    private val maxLifes = 0
    private var currentLifes = 5
    private var currentScore = 0
    private var randomValue: Int = 0
    private var currentSpin = 0
    val scoreList = mutableListOf<Int>()

    fun startNewGame(): GameState {
        lettersUsed = ""
        currentLifes = 5
        currentScore = 0
        currentSpin = 0
        scoreList.clear()
        val randomIndex = Random.nextInt(0, Phases.phases.size)
        wordToGuess = Phases.phases[randomIndex]
        generateUnderscores(wordToGuess)

        return getGameState()
    }
    /*
    fun getPhasesIndex(): Int {
        val phaseIndex = wordToGuess.
    }

     */
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
            scoreList.add(randomValue)
        }

        currentScore = scoreList.sum()

        underscoreWord = finalUnderscoreWord
        return getGameState()

    }

    fun spinValue(): Int {
        randomValue = Values.values.random()

        return randomValue
    }

    fun missTurn(): GameState {
        currentLifes--

        return getGameState()
    }

    fun bankrupt(): GameState {
        scoreList.clear()
        currentScore = scoreList.sum()


        return getGameState()
    }

    private fun getGameState(): GameState {
        if (underscoreWord.equals(wordToGuess, true)) {
            return GameState.Won(wordToGuess)
        }

        if (currentLifes <= 0) {
            return GameState.Lost(wordToGuess)
        }

        return GameState.Running(lettersUsed, underscoreWord, currentLifes, currentScore)
    }
}