package com.cheqin.booking.utils;

import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import java.math.BigInteger;

public class NotificationExtenderUtil extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        // Read Properties from result
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Red on Android 5.0+ devices.
                Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/raw/notification");
                return builder.setColor(new BigInteger("FFFF0000", 16).intValue()).setSound(soundUri);
            }
        };

        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        //Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        // Return true to stop the notification from displaying
        return true;
    }
}
