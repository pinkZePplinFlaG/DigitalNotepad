package com.woody.digitalnotepad

class RecyclerViewChildren {
    private var children  = ArrayList<RecyclerItem>()

    fun getChild(index: Int):RecyclerItem{
        for(c in children) {
            if (c.index == index) {
                return c
            }
        }
        return RecyclerItem(0,"","","","","","","")
    }

    fun addChild(item: RecyclerItem){children.add(item)}
    fun removeChild(index: Int){children.removeAt(index)}
    fun clearChildren(){children = ArrayList()}
}