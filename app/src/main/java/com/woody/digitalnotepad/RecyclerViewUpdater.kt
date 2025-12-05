package com.woody.digitalnotepad

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewUpdater() {
    fun updateRecyclerView(mainContext: AppCompatActivity, messages: ArrayList<RecyclerItem>) {
        val recyclerView: RecyclerView = mainContext.findViewById(R.id.recycler_view)
        recyclerView.adapter = RecyclerViewAdapter(messages)
    }
}