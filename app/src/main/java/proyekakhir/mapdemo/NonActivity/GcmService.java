/*
Copyright 2015 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package proyekakhir.mapdemo.NonActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import proyekakhir.mapdemo.Activity.TemukanLubang.Activity7a_ResultTemukanLubang_ViewPoint;
import proyekakhir.mapdemo.R;


/**
 * Service used for receiving GCM messages. When a message is received this service will log it.
 */
public class GcmService extends GcmListenerService {

//    private LoggingService.Logger logger;
    SharedPreferences pref;
    Double lastLat, lastLon;

    public GcmService() {
//        logger = new LoggingService.Logger(this);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
    //    sendNotification("Received: " + data.toString());


        //Cek notifikasi aktif / tidak
        if(StaticPref.getShowNotif() == 1) {

            lastLat = StaticPref.getLast_lat();
            lastLon = StaticPref.getLast_lon();
            double x_lat = Double.parseDouble(data.getString("latitude"));
            double x_lon = Double.parseDouble(data.getString("longitude"));

            float[] results = new float[1];
            Location.distanceBetween(x_lat, x_lon,
                    lastLat, lastLon, results);
            Log.v("Jarak", Float.toString((results[0]/1000)));

            if((results[0]/1000) <= StaticPref.getJarak()){
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setAutoCancel(true)
                                .setContentTitle("Hati-hati!")
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentText("Terdapat Lubang Di Jalan Sekitar Anda.");
                // Creates an explicit intent for an Activity in your app
                Intent resultIntent = new Intent(this, Activity7a_ResultTemukanLubang_ViewPoint.class);
                resultIntent.putExtra("lat", data.getString("latitude"));
                resultIntent.putExtra("lon", data.getString("longitude"));

                // The stack builder object will contain an artificial back stack for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out of
                // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(Activity7a_ResultTemukanLubang_ViewPoint.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // mId allows you to update the notification later on.
                mNotificationManager.notify(1, mBuilder.build());
            }


        }
    }

    @Override
    public void onDeletedMessages() {
        sendNotification("Deleted messages on server");
    }

    @Override
    public void onMessageSent(String msgId) {
        sendNotification("Upstream message sent. Id=" + msgId);
    }

    @Override
    public void onSendError(String msgId, String error) {
        sendNotification("Upstream message send error. Id=" + msgId + ", error" + error);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
//        logger.log(Log.INFO, msg);
    }
}
