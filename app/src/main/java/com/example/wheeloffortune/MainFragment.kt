package com.example.wheeloffortune

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.GameManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wheeloffortune.databinding.FragmentMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.concurrent.timer
import kotlin.math.absoluteValue


class MainFragment : Fragment() {
    private val gameManager = GameManager()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var wordTextView: TextView
    private lateinit var lettersUsedTextView: TextView
    private lateinit var spinWheel: Button
    private lateinit var newGame: Button
    private lateinit var lettersLayout: ConstraintLayout
    private lateinit var gameLostTextView: TextView
    private lateinit var gameWonTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var currentLifesTextView : TextView
    private lateinit var currentScoreTextView: TextView
    private lateinit var wheelResultTextView: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      _binding = FragmentMainBinding.inflate(inflater, container, false)
        wordTextView = binding.wordTextView
        lettersUsedTextView = binding.lettersUsedTextView
        spinWheel = binding.spinWheelButton
        newGame = binding.newGameButton
        lettersLayout = binding.lettersLayout
        gameWonTextView = binding.gameWonTextView
        gameLostTextView = binding.gameLostTextView
        categoryTextView = binding.categoryTextView
        wheelResultTextView = binding.wheelResultTextView
        currentScoreTextView = binding.currentScoreTextView
        currentScoreTextView.visibility = View.VISIBLE
        currentLifesTextView = binding.currentLifesTextView
        currentLifesTextView.visibility = View.VISIBLE

        lettersLayout.visibility = View.GONE
        wheelResultTextView.text = ("Spin to play!")
        categoryTextView.text = Phases.categories[gameManager.getPhasesIndex()]
        newGame.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this.context)
            alertBuilder.setMessage("Are you sure you want to start new game?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    Navigation.findNavController(it).navigate(R.id.mainFragment)
                    Toast.makeText(activity,"New Game Started!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
            val alert = alertBuilder.create()
            alert.show()
        }


        val gameState = gameManager.startNewGame()
        updateUI(gameState)

        spinWheel.setOnClickListener {
            val value = gameManager.spinValue()
            wheelResultTextView.text = "Spin: $value"
            currentLifesTextView.visibility = View.VISIBLE
            lettersLayout.visibility = View.VISIBLE
            spinWheel.visibility = View.GONE

            if (value == 0) {
                wheelResultTextView.text = "Missed Turn..., Spin Again!"
                spinWheel.visibility = View.VISIBLE
                lettersLayout.visibility = View.GONE
                val lostLife = gameManager.missTurn()
                updateUI(lostLife)
            }

            if (value == 1) {
                wheelResultTextView.text = "Bankrupt..., Spin Again!"
                spinWheel.visibility = View.VISIBLE
                lettersLayout.visibility = View.GONE
                val lostScore = gameManager.bankrupt()
                updateUI(lostScore)
            }
        }

        lettersLayout.children.forEach { letterView ->
            if (letterView is TextView) {
                letterView.setOnClickListener {
                    val lettersState = gameManager.play((letterView).text[0])
                    updateUI(lettersState)
                    letterView.visibility = View.GONE
                    lettersLayout.visibility = View.GONE
                    spinWheel.visibility = View.VISIBLE
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(gameState: GameState) {
        when (gameState) {
            is GameState.Running -> {
                wordTextView.text = gameState.underscoreWord
                lettersUsedTextView.text = "Letters used: ${gameState.lettersUsed}"
                currentLifesTextView.text = "Lifes left: ${gameState.currentLifes}"
                currentScoreTextView.text = "Current Score: ${gameState.currentScore}"
            }
            is GameState.Won -> gameWon(gameState.wordToGuess)
            is GameState.Lost -> gameLost(gameState.wordToGuess)
        }
    }

    private fun gameLost(wordToGuess: String) {
        wordTextView.text = wordToGuess
        gameLostTextView.visibility = View.VISIBLE
        currentLifesTextView.visibility = View.VISIBLE
        currentLifesTextView.text = "Lifes left: 0"
        lettersLayout.visibility = View.GONE
    }

    private fun gameWon(wordToGuess: String) {
        wordTextView.text = wordToGuess
        gameWonTextView.visibility = View.VISIBLE
        currentLifesTextView.visibility = View.VISIBLE
        currentScoreTextView.text = "Current Score: ${gameManager.getWinResult()}"
        lettersLayout.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }


}



