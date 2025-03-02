package com.cheqin.booking.gcm

import UtelNotificationItem
import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.recyclerview.widget.RecyclerView
import com.cheqin.booking.R

class NotificationListAdapter(private val items: List<UtelNotificationItem>, private val listener: OnNotificationItemClicked) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnNotificationItemClicked {
        fun onNotificationClicked(utelNotificationItem: UtelNotificationItem, pos: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_list_item, parent, false)

        val holder = NotificationListHolder(view)

        view.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                listener.onNotificationClicked(items[pos], pos)
            }
        }
        return holder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as NotificationListHolder
        holder.bind(items[position])
    }

    inner class NotificationListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDesc = itemView.findViewById<TextView>(R.id.tvDesc)
        val ebrimaTf = ResourcesCompat.getFont(itemView.context, R.font.ebrima)
        val ebrimaBdTf = ResourcesCompat.getFont(itemView.context, R.font.ebrimabd)


        fun bind(utelNotificationItem: UtelNotificationItem) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvDesc.text = Html.fromHtml(utelNotificationItem.message, FROM_HTML_MODE_LEGACY)
            } else {
                tvDesc.text = Html.fromHtml(utelNotificationItem.message)
            }

            if (utelNotificationItem.isRead) {
                tvDesc.typeface = ebrimaTf
            } else {
                tvDesc.typeface = ebrimaBdTf
            }

        }
    }
}