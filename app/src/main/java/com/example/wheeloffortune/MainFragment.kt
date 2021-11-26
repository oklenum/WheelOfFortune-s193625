package com.example.wheeloffortune

import android.annotation.SuppressLint
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
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wheeloffortune.databinding.FragmentMainBinding

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
    private lateinit var currentLifesTextView : TextView
    private lateinit var currentScoreTextView: TextView
    private lateinit var wheelResultTextView: TextView

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
        spinWheel = binding.spinWheelButton
        newGame = binding.newGameButton
        lettersLayout = binding.lettersLayout
        gameWonTextView = binding.gameWonTextView
        gameLostTextView = binding.gameLostTextView
        wheelResultTextView = binding.wheelResultTextView
        currentScoreTextView = binding.currentScoreTextView
        currentLifesTextView = binding.currentLifesTextView
        currentLifesTextView.visibility = View.VISIBLE
        lettersLayout.visibility = View.GONE
        newGame.setOnClickListener {
            startNewGame()
            Toast.makeText(activity,"New Game Started!", Toast.LENGTH_LONG).show()
        }
        val gameState = gameManager.startNewGame()
        updateUI(gameState)

        spinWheel.setOnClickListener {
            spinWheel()
            currentScoreTextView.text = "Current Score: ${spinWheel()}"
        }

        lettersLayout.children.forEach { letterView ->
            if (letterView is TextView) {
                letterView.setOnClickListener {
                    val gameState = gameManager.play((letterView).text[0])
                    updateUI(gameState)
                    letterView.visibility = View.GONE
                    lettersLayout.visibility = View.GONE
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
        currentLifesTextView.visibility = View.VISIBLE
        lettersLayout.visibility = View.GONE
        lettersLayout.children.forEach { letterView ->
            letterView.visibility = View.GONE
        }
        updateUI(gameState)
    }

    private fun spinWheel() {
        val randomValue = Values.values.random()
        wheelResultTextView.text = "Result: $randomValue"
        currentLifesTextView.visibility = View.VISIBLE
        lettersLayout.visibility = View.VISIBLE
        lettersLayout.children.forEach { letterView ->
            letterView.visibility = View.VISIBLE
        }
    }


    override fun onDestroy() {
        super.onDestroy()

    }


}



