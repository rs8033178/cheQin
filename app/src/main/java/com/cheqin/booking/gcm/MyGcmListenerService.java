/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cheqin.booking.gcm;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.android.gms.gcm.GcmListenerService;
import com.cheqin.booking.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Log.e("data", data.toString());
        String message = data.getString("message");
        String imagepathc = data.getString("imagepath");

        // Log.e(TAG, "From: " + from);
        //Log.e(TAG, "Message: " + message);
        //  Log.e(TAG, "imagepath: " + imagepathc);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }
        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        customNotification2(message, imagepathc);
        // [END_EXCLUDE]
    }

    private void customNotification2(String message, String imagepath) {
        storemessageAndImagePath(message, imagepath);
        Bitmap bitmap = getBitmapFromURL(imagepath);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setAutoCancel(true)
                .setContentTitle(message)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/raw/notification"))
                .setSmallIcon(R.drawable.mypillow_app_icon);
        //.setLargeIcon(bitmap)
        // .setContentText("Hello World!");
        NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
        bigPicStyle.bigPicture(bitmap);
        bigPicStyle.setBigContentTitle("my pillows");
        bigPicStyle.setSummaryText("hey, we have got you new offers");
        mBuilder.setStyle(bigPicStyle);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NotificationActivity.class);
        // The stack builder object will contain an artificial back stack for
        // the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself)
        // stackBuilder.addParentStack(MainActivity2.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void storemessageAndImagePath(String message, String imagepath) {
//        SharedPreference sharedPreference = new SharedPreference();
//        sharedPreference.addFavorite(this,new NotificationBean(message,imagepath));
    }

    // [END receive_message]

//    /**
//     * Create and show a simple notification containing the received GCM message.
//     *
//     * @param message GCM message received.
//     * @param imagepath
//     */
//    private void sendNotification(String message, String imagepath) {
//
//       // storeMessage(message);
//        storeImagePath(imagepath);
//        Intent intent = new Intent(this, MainActivity2.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_drawer)
//                .setContentTitle("savan app")
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }

//    private void customNotification(String message, String imagepath) {
//        storeImagePath(imagepath);
//        int icon = R.drawable.ic_drawer;
//        long when = System.currentTimeMillis();
//        Notification notification = new Notification(icon, "Custom Notification", when);
//
//        NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//
//        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_notification);
//
////         ImageLoader imageLoader = AppController.getInstance().getImageLoader();
////         ImageView imageView = contentView.fi
////         itemImg.setImageUrl(imagepath, imageLoader);
//        Bitmap bitmap = getBitmapFromURL(imagepath);
//        contentView.setImageViewResource(R.id.image_notification, bitmap);
//        contentView.setTextViewText(R.id.title, message);
//        contentView.setTextViewText(R.id.text, "This is a custom layout");
//        notification.contentView = contentView;
//
//        Intent notificationIntent = new Intent(this, MainActivity2.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        notification.contentIntent = contentIntent;
//
//        notification.flags |= Notification.FLAG_NO_CLEAR; //Do not clear the notification
//        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
//        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
//        notification.defaults |= Notification.DEFAULT_SOUND; // Sound
//
//        mNotificationManager.notify(1, notification);
//    }

//    private void storeMessage(String message) {
//        SharedPreference sharedPreference = new SharedPreference();
//        sharedPreference.addFavorite(this, message);
//    }
//
//    private void storeImagePath(String imagepath) {
//        SharedPreference sharedPreference = new SharedPreference();
//        sharedPreference.addFavorite(this,imagepath);
//    }
}
