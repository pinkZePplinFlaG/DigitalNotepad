package com.woody.digitalnotepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.toColorInt

class TextRecyclerViewAdapter(val textList: List<String>) :
    RecyclerView.Adapter<TextRecyclerViewAdapter.TextAndOrCheckboxHolder>() {


    // Describes an item view and its place within the RecyclerView
    class TextAndOrCheckboxHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val selectableTextLabel: TextView = itemView.findViewById(R.id.selectable_text_label)
        private val selectableText: TextView = itemView.findViewById(R.id.selectable_text)
        private val selectableTextCheckBox: CheckBox = itemView.findViewById(R.id.checkbox)
        fun bind(word: String) {
            if(word.contains(":") ) {
                val labelAndText = word.split(":")
                selectableTextLabel.text = labelAndText[0]
                selectableTextLabel.setTextColor("#008080".toColorInt())
                selectableText.text = labelAndText[1]
                selectableText.setTextColor("#008080".toColorInt())
            }else if(word == "Signed in to Firestore!" || word.contains("-----")){
                selectableTextLabel.text = word
                selectableTextLabel.setTextColor("#008080".toColorInt())
                selectableText.setTextColor("#008080".toColorInt())
                selectableText.text = buildString { append("") }
            }
            if (word.contains("Firestore!"))
                selectableTextCheckBox.visibility = View.GONE
        }
    }

    // Returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextAndOrCheckboxHolder {
        val selectableView = LayoutInflater.from(parent.context)
            .inflate(R.layout.selectable_text, parent, false)
        return TextAndOrCheckboxHolder(selectableView)
    }

    // Returns size of data list
    override fun getItemCount(): Int {
        return textList.size
    }

    // Displays data at a certain position
    override fun onBindViewHolder(holder: TextAndOrCheckboxHolder, position: Int) {
        holder.bind(textList[position])
    }
}