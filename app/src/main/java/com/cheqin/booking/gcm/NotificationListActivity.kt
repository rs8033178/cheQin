package com.cheqin.booking.gcm

import UtelNotificationItem
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.cheqin.booking.R
import kotlinx.android.synthetic.main.activity_notification_list.*
import kotlinx.android.synthetic.main.toolbar.*

class NotificationListActivity : AppCompatActivity(), NotificationListAdapter.OnNotificationItemClicked {


    lateinit var viewModel: NotificationListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notification_list)

        toolbar.title = getString(R.string.notifications)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        rvNotifications.addItemDecoration(DividerItemDecoration(this, VERTICAL))

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get<NotificationListViewModel>(NotificationListViewModel::class.java);

        viewModel.getNotificationData().observe(this, Observer<List<UtelNotificationItem>> {

            if (it.isNotEmpty()) {
                tvEmptyMsg.visibility = View.GONE

                rvNotifications.adapter = NotificationListAdapter(it, this)

                viewModel.markAllNotificationRead()
            } else {
                tvEmptyMsg.visibility = View.VISIBLE
            }
        })

    }

    override fun onNotificationClicked(utelNotificationItem: UtelNotificationItem, pos: Int) {
        val intent = getNavigationIntent(this, utelNotificationItem.notification_type, utelNotificationItem.objectId)
        intent?.let {
            startActivity(intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}