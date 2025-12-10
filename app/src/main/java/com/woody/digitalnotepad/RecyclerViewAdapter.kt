package com.woody.digitalnotepad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(val itemList: ArrayList<RecyclerItem>) :
    RecyclerView.Adapter<RecyclerViewAdapter.TextAndOrCheckboxHolder>() {

    class TextAndOrCheckboxHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tealText0: TextView = itemView.findViewById(R.id.teal_text0)
        private val tealText1: TextView = itemView.findViewById(R.id.teal_text1)
        private val tealText2: TextView = itemView.findViewById(R.id.teal_text2)
        private val tealText3: TextView = itemView.findViewById(R.id.teal_text3)
        private val tealText4: TextView = itemView.findViewById(R.id.teal_text4)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        fun bind(item: RecyclerItem) {
            if (!item.firebaseStatus.isEmpty()) {
                tealText0.text = item.firebaseStatus
                checkBox.visibility = CheckBox.GONE
            } else {
                if (!item.docId.isEmpty()) {
                    tealText4.text = item.docId
                }
                if (!item.fireCol0.isEmpty()) {
                    tealText0.text = item.fireCol0
                }
                if (!item.fireCol1.isEmpty()) {
                    tealText1.text = item.fireCol1
                }
                if (!item.fireCol2.isEmpty()) {
                    tealText2.text = item.fireCol2
                }
                if (!item.fireCol3.isEmpty()) {
                    tealText3.text = item.fireCol3
                }
            }
        }
    }

    // Returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextAndOrCheckboxHolder {
        val selectableView = LayoutInflater.from(parent.context)
            .inflate(R.layout.teal_text, parent, false)
        return TextAndOrCheckboxHolder(selectableView)
    }

    // Returns size of data list
    override fun getItemCount(): Int {
        return itemList.size
    }

    // Displays data at a certain position
    override fun onBindViewHolder(holder: TextAndOrCheckboxHolder, position: Int) {
        holder.bind(itemList[position])
    }
}