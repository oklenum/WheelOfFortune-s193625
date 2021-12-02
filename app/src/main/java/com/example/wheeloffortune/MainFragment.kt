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
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
    private lateinit var rulesButton: Button
    private val randomValue: Int = 0
    val currentScore = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      _binding = FragmentMainBinding.inflate(inflater, container, false)
        wordTextView = binding.wordTextView
        lettersUsedTextView = binding.lettersUsedTextView
        rulesButton = binding.rulesButton
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
        //randomValue = Values.values.random()
        wheelResultTextView.text = ("Spin to play!")
        categoryTextView.text = Phases.categories[gameManager.getPhasesIndex()]
        newGame.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this.context)
            alertBuilder.setMessage("Are you sure you want to start new game?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    startNewGame()
                    Toast.makeText(activity,"New Game Started!", Toast.LENGTH_LONG).show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
            val alert = alertBuilder.create()
            alert.show()
        }
        rulesButton.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_mainFragment_to_rulesFragment)
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
            is GameState.Won -> showGameWon(gameState.wordToGuess)
            is GameState.Lost -> showGameLost(gameState.wordToGuess)
        }
    }

    private fun showGameLost(wordToGuess: String) {
        wordTextView.text = wordToGuess
        gameLostTextView.visibility = View.VISIBLE
        currentLifesTextView.visibility = View.VISIBLE
        currentLifesTextView.text = "Lifes left: 0"
        lettersLayout.visibility = View.GONE
    }

    private fun showGameWon(wordToGuess: String) {
        wordTextView.text = wordToGuess
        gameWonTextView.visibility = View.VISIBLE
        currentLifesTextView.visibility = View.VISIBLE
        lettersLayout.visibility = View.GONE
    }

    private fun startNewGame() {
        gameLostTextView.visibility = View.GONE
        gameWonTextView.visibility = View.GONE
        val gameState = gameManager.startNewGame()
        spinWheel.visibility = View.VISIBLE
        wheelResultTextView.text = "Spin to play!"
        categoryTextView.text = Phases.categories[gameManager.getPhasesIndex()]
        currentLifesTextView.visibility = View.VISIBLE
        lettersLayout.visibility = View.GONE
        lettersLayout.children.forEach { letterView ->
            letterView.visibility = View.VISIBLE
        }
        updateUI(gameState)
    }


    override fun onDestroy() {
        super.onDestroy()

    }


}



