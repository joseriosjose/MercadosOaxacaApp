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
import com.oaxaca.turismo.mercados.MainActivity;
import com.oaxaca.turismo.mercados.MainActivity2;
import com.oaxaca.turismo.mercados.R;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



public class NotificationUtils {
    private static final int NOTIFICATION_ID = 200;
    private static final String CHANNEL_ID = "0";
    private static final String URL = "url";
    private static final String ACTIVITY = "activity";
    Map<String, Class> activityMap = new HashMap<>();
    private Context mContext;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
        //Mapa de actividades
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
        try{//verificar si tiene alguna actividad extra
            String destination,mercado = "";
            if(notificationVO.getActionDestination().contains("-")){
                String[] parts = notificationVO.getActionDestination().split("-");
                destination = parts[0]; //
                mercado = parts[1]; //
            }else {
                destination=notificationVO.getActionDestination();
            }
             //obtener el contenido basico
            String message = notificationVO.getMessage();
            String title2 = notificationVO.getTitle();
            String iconUrl = notificationVO.getIconUrl();
            String action = notificationVO.getAction();
            Bitmap iconBitMap = null;
            if (iconUrl != null) {
                iconBitMap = getBitmapFromURL(iconUrl); //obtener imagen si se necesita
            }
            final int icon = R.mipmap.ic_launcher_round;
            PendingIntent resultPendingIntent;

            if (URL.equals(action)) { //si la accion es una busqueda en internet
                Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));
                resultPendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);

            } else if (ACTIVITY.equals(action) && activityMap.containsKey(destination)) {//si es abrir un mercado en especifico
                resultIntent = new Intent(mContext, activityMap.get(destination));
                resultIntent.putExtra("IdM",mercado);
                resultPendingIntent = PendingIntent.getActivity(mContext,0,resultIntent,PendingIntent.FLAG_CANCEL_CURRENT);
            } else {
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                resultPendingIntent = PendingIntent.getActivity(  mContext,0,     resultIntent, PendingIntent.FLAG_CANCEL_CURRENT );
            }

            final int NOTIFY_ID = 0; // ID de la notificacion
            String id = context.getString(R.string.default_notification_channel_id); // id del canal
            String title = context.getString(R.string.default_notification_channel_title); // canal

            NotificationCompat.Builder builder;
            if (notifManager == null) {
                notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            //verificar la version del dispositivo y crear la notificacion para este
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//si es oreo
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
                        builder.setContentTitle(title2)                            // requerido
                                .setSmallIcon(R.mipmap.ic_launcher_round)   // requerido
                                .setContentText(context.getString(R.string.app_name)) // requerido
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
                        builder.setContentTitle(title2)                            // requerido
                                .setSmallIcon(R.mipmap.ic_launcher_round)   // requerido
                                .setContentText(context.getString(R.string.app_name)) // requerido
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
            } else {//si no es oreo
                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        mContext, CHANNEL_ID);
                Notification notification;

                if (iconBitMap == null) {
                    //Cuando se aplica el estilo de bandeja de entrada, el usuario puede expandir la notificación
                    NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                    inboxStyle.addLine(message);
                    notification = mBuilder.setSmallIcon(icon).setTicker(title2).setWhen(0)
                            .setAutoCancel(true)
                            .setContentTitle(title2)
                            .setContentIntent(resultPendingIntent)
                            .setStyle(inboxStyle)
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                            .setContentText(message)
                            .build();

                } else {
                    //Si se crea Bitmap desde la URL, muestra el ícono grande
                    NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                    bigPictureStyle.setBigContentTitle(title2);
                    bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
                    bigPictureStyle.bigPicture(iconBitMap);
                    notification = mBuilder.setSmallIcon(icon).setTicker(title2).setWhen(0)
                            .setAutoCancel(true)
                            .setContentTitle(title2)
                            .setContentIntent(resultPendingIntent)
                            .setStyle(bigPictureStyle)
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                            .setContentText(message)
                            .build();
                }
                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
        }catch (Exception e){

        }


    }


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
     * reproducir sonido
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