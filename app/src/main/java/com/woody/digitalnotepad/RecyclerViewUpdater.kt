package com.woody.digitalnotepad

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewUpdater() {
    fun updateTextRecyclerView(mainContext: AppCompatActivity, messages: Array<String>) {
        val textRecyclerView: RecyclerView = mainContext.findViewById(R.id.recycler_view)
        textRecyclerView.adapter = TextRecyclerViewAdapter(messages.toList())
    }
}