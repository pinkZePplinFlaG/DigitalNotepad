package com.woody.digitalnotepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(val textList: List<String>) :
    RecyclerView.Adapter<RecyclerViewAdapter.TextOrButtonViewHolder>() {


    // Describes an item view and its place within the RecyclerView
    class TextOrButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val selectableText: TextView = itemView.findViewById(R.id.selectable_text)
        private val selectableTextCheckBox: CheckBox = itemView.findViewById(R.id.checkbox)
        fun bind(word: String) {

            selectableText.text = word
            if (!word.contains("Id: ") || word.contains("userId"))
                selectableTextCheckBox.visibility = View.GONE
        }
    }

    // Returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextOrButtonViewHolder {
        val selectableView = LayoutInflater.from(parent.context)
            .inflate(R.layout.selectable_text, parent, false)
//        val selectableText: TextView = parent.findViewById(R.id.selectable_text)
//        val selectableTextCheckBox: CheckBox = parent.findViewById(R.id.checkbox)
//        textMap.put(selectableText.text as String, selectableTextCheckBox.isSelected)
        return TextOrButtonViewHolder(selectableView)
    }

    // Returns size of data list
    override fun getItemCount(): Int {
        return textList.size
    }

    // Displays data at a certain position
    override fun onBindViewHolder(holder: TextOrButtonViewHolder, position: Int) {
        holder.bind(textList[position])
    }
}