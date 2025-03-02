package com.cheqin.booking.utils;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.cheqin.booking.MainActivity;
import com.cheqin.booking.User.UserBooking;
import com.cheqin.booking.User.UserNotifications;
import com.cheqin.booking.gcm.NotificationListActivity;
import com.cheqin.booking.gcm.NotificationNavigationHelperKt;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;


public class AppSingleTon extends MultiDexApplication {

    public static Context CONTEXT;
    public static Resources RESOURCES;
    public static LayoutInflater LAYOUT_INFLATER;
    public static RequestQueue VOLLEY_REQUEST_QUEUE;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initStaticObject();
        AndroidThreeTen.init(this);

//		UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
//		// Or, you can define it manually.
//		UploadService.NAMESPACE = "com.login_pages";

        // OneSignal Initialization
        // Logging set to help debug issues, remove before releasing your app.
        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.WARN);

        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        ;

    }

    public void initStaticObject() {

        CONTEXT = getApplicationContext();
        RESOURCES = CONTEXT.getResources();
        VOLLEY_REQUEST_QUEUE = Volley.newRequestQueue(this);
        LAYOUT_INFLATER = (LayoutInflater) CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String notificationID = notification.payload.notificationID;
            String title = notification.payload.title;
            String body = notification.payload.body;
            String smallIcon = notification.payload.smallIcon;
            String largeIcon = notification.payload.largeIcon;
            String bigPicture = notification.payload.bigPicture;
            String smallIconAccentColor = notification.payload.smallIconAccentColor;
            String sound = notification.payload.sound;
            String ledColor = notification.payload.ledColor;
            int lockScreenVisibility = notification.payload.lockScreenVisibility;
            String groupKey = notification.payload.groupKey;
            String groupMessage = notification.payload.groupMessage;
            String fromProjectNumber = notification.payload.fromProjectNumber;
            String rawPayload = notification.payload.rawPayload;

            String customKey;

            //Log.i("OneSignalExample", "NotificationID received: " + notificationID);

            /*Intent intent = new Intent(getApplicationContext(), (Class<?>) UserNotifications.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            //Log.i("OneSignalExample", "openURL = " + openURL);
            startActivity(intent);*/
            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null) {

                    //					Log.i("OneSignalExample", "customkey set with value: " + customKey);

                }

            }
        }
    }


    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String launchUrl = result.notification.payload.launchURL; // update docs launchUrl

            String notificationType = "";
            long objectId = -1L;

            if (data != null) {
                notificationType = data.optString("type");
                objectId = data.optLong("id");
            }

            if (actionType == OSNotificationAction.ActionType.Opened) {

                Intent showRequestScreen = new Intent(getApplicationContext(), UserBooking.class);
                //Intent intent = NotificationNavigationHelperKt.getNavigationIntent(getApplicationContext(), notificationType, objectId);
                Intent intent = new Intent(getApplicationContext(), (Class<?>) NotificationListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
                taskStackBuilder.addNextIntent(showRequestScreen);
                taskStackBuilder.addNextIntent(intent);


                taskStackBuilder.startActivities();

            }
        }
    }
}