package com.cheqin.booking.User

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.cheqin.booking.R

class SimpleDropDownAdapter(context: Context, private val items: MutableList<String>, private val needCustomTypeFace: Boolean = false) : ArrayAdapter<String>(context, R.layout.drop_down_list_item) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val text = view.findViewById<View>(R.id.text1) as TextView


        if (needCustomTypeFace && position == 0) {
            text.setTypeface(null, Typeface.ITALIC)
        } else {
            text.setTypeface(text.typeface, Typeface.NORMAL)
        }

        return view
    }


    fun setData(list: List<String>) {
        this.items.clear()
        this.items.addAll(list)
    }

    override fun getItem(position: Int): String? {
        return this.items[position]
    }

    override fun getCount(): Int {
        return this.items.size
    }
}