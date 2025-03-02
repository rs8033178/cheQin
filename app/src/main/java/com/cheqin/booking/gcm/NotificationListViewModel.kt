package com.cheqin.booking.gcm

import UtelNotificationItem
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.cheqin.booking.utils.SettingSharedPrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationListViewModel(app: Application) : AndroidViewModel(app) {

    fun getNotificationData(): LiveData<List<UtelNotificationItem>> = liveData(context = viewModelScope.coroutineContext) {
        val ssp = SettingSharedPrefs.getInstance(getApplication())

        val list = NotificationRemoteDataSource.fetchNotifications(ssp.prE_auth_token)
        emit(list)
    }

    fun markAllNotificationRead() {
        viewModelScope.launch {
            delay(500)
            val ssp = SettingSharedPrefs.getInstance(getApplication())

            NotificationRemoteDataSource.markAllNotificationRead(ssp.prE_auth_token)
        }
    }


}