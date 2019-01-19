package com.oaxaca.turismo.mercados.clases;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.oaxaca.turismo.mercados.MainActivity;
import com.oaxaca.turismo.mercados.MainActivity2;
import com.oaxaca.turismo.mercados.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abhi on 13 Nov 2017 013.
 */

public class NotificationUtils {
    private static final int NOTIFICATION_ID = 200;
    private static final String PUSH_NOTIFICATION = "pushNotification";
    private static final String CHANNEL_ID = "0";
    private static final String URL = "url";
    private static final String ACTIVITY = "activity";
    Map<String, Class> activityMap = new HashMap<>();
    private Context mContext;
    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
        //Populate activity map
        activityMap.put("MainActivity", MainActivity.class);
        activityMap.put("SecondActivity", MainActivity2.class);
    }

    /**
     * Displays notification based on parameters
     *
     * @param notificationVO
     * @param resultIntent
     */

    private NotificationManager notifManager;

    public void createNotification(NotificationVO notificationVO, Intent resultIntent,Context context) {
        try{
            String destination,mercado = "";
            if(notificationVO.getActionDestination().contains("-")){
                String[] parts = notificationVO.getActionDestination().split("-");
                destination = parts[0]; //
                mercado = parts[1]; //
            }else {
                destination=notificationVO.getActionDestination();
            }

            String message = notificationVO.getMessage();
            String title2 = notificationVO.getTitle();
            String iconUrl = notificationVO.getIconUrl();
            String action = notificationVO.getAction();
            Bitmap iconBitMap = null;
            if (iconUrl != null) {
                iconBitMap = getBitmapFromURL(iconUrl);
            }
            final int icon = R.mipmap.ic_launcher;

            PendingIntent resultPendingIntent;

            if (URL.equals(action)) {
                Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));

                resultPendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
            } else if (ACTIVITY.equals(action) && activityMap.containsKey(destination)) {
                resultIntent = new Intent(mContext, activityMap.get(destination));
                resultIntent.putExtra("IdM",mercado);
                resultPendingIntent =
                        PendingIntent.getActivity(
                                mContext,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT
                        );
            } else {
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                resultPendingIntent =
                        PendingIntent.getActivity(
                                mContext,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT
                        );
            }



            final int NOTIFY_ID = 0; // ID of notification
            String id = context.getString(R.string.default_notification_channel_id); // default_channel_id
            String title = context.getString(R.string.default_notification_channel_title); // Default Channel

            NotificationCompat.Builder builder;
            if (notifManager == null) {
                notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = notifManager.getNotificationChannel(id);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(id, title, importance);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notifManager.createNotificationChannel(mChannel);
                }

                builder = new NotificationCompat.Builder(context, id);
                if(iconBitMap == null){
                    builder.setContentTitle(title2)                            // required
                            .setSmallIcon(R.mipmap.ic_launcher_round)   // required
                            .setContentText(context.getString(R.string.app_name)) // required
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setContentIntent(resultPendingIntent)
                            .setContentText(message)
                            .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                }else{
                    NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                    bigPictureStyle.setBigContentTitle(title2);
                    bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
                    bigPictureStyle.bigPicture(iconBitMap);

                    builder.setContentTitle(title2)                            // required
                            .setSmallIcon(R.mipmap.ic_launcher_round)   // required
                            .setContentText(context.getString(R.string.app_name)) // required
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true)
                            .setContentIntent(resultPendingIntent)
                            .setContentText(message)
                            .setStyle(bigPictureStyle)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                            .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                }



                Notification notification = builder.build();
                notifManager.notify(NOTIFY_ID, notification);
            }
            else {
                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        mContext, CHANNEL_ID);

                Notification notification;

                if (iconBitMap == null) {
                    //When Inbox Style is applied, user can expand the notification
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                    inboxStyle.addLine(message);
                    notification = mBuilder.setSmallIcon(icon).setTicker(title2).setWhen(0)
                            .setAutoCancel(true)
                            .setContentTitle(title2)
                            .setContentIntent(resultPendingIntent)
                            .setStyle(inboxStyle)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                            .setContentText(message)
                            .build();

                } else {
                    //If Bitmap is created from URL, show big icon
                    NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                    bigPictureStyle.setBigContentTitle(title2);
                    bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
                    bigPictureStyle.bigPicture(iconBitMap);
                    notification = mBuilder.setSmallIcon(icon).setTicker(title2).setWhen(0)
                            .setAutoCancel(true)
                            .setContentTitle(title2)
                            .setContentIntent(resultPendingIntent)
                            .setStyle(bigPictureStyle)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                            .setContentText(message)
                            .build();
                }
                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
        }catch (Exception e){
            //   Log.d("verga","valio nepe"+e);
        }


    }

    /**
     * Downloads push notification image before displaying it in
     * the notification tray
     *
     * @param strURL : URL of the notification Image
     * @return : BitMap representation of notification Image
     */
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Playing notification sound
     */
    public boolean playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}