package com.example.wheeloffortune

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

class WordAdapter(context: Context) :
    RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    private val rules: List<String>

    init {
        val words = context.resources.getStringArray(R.array.rules).toList()

        rules = words


    }

    class WordViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val rule = view.findViewById<TextView>(R.id.rules_view)
    }

    override fun getItemCount(): Int {
        return rules.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.rules_view, parent, false)

        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {

        val item = rules[position]
        holder.rule.text = item

    }
}


