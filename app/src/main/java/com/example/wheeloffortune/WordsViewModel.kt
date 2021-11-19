package com.example.wheeloffortune

import android.provider.UserDictionary

data class WordsViewModel(val category: String, val word: String)

val nr1 = WordsViewModel("Outdoor Activity",  "Hiking")
val nr2 = WordsViewModel("Aviation", "A big jet")
fun main(args: Array<String>) {
    val nr1 = WordsViewModel("Outdoor Activity",  "Hiking")
    val nr2 = WordsViewModel("Aviation", "A big jet")

    println("${nr1.word}")
}
