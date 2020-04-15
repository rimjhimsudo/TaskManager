package com.example.taskmanager;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public  static  final String channel11d="channel11d";
    public  static  final String channel1name="channel1name";
    /*public  static  final String channel2id="channel2id";
    public  static  final String channel2name="channel2name";*/
    private NotificationManager notificationManager;
    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createChannels();
        }

    }
    @TargetApi(Build.VERSION_CODES.O)
    public  void  createChannels(){
        NotificationChannel channel1=new NotificationChannel(channel11d,channel1name, NotificationManager.IMPORTANCE_HIGH);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel1);

        /*NotificationChannel channel2=new NotificationChannel(channel2id,channel2name, NotificationManager.IMPORTANCE_DEFAULT);
        channel2.enableLights(true);
        channel2.enableVibration(true);
        channel2.setLightColor(R.color.colorPrimary);
        channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel2);*/

    }
    public  NotificationManager getManager(){
        if (notificationManager==null){
            notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
    public NotificationCompat.Builder getChannel1Notification(){
        return  new NotificationCompat.Builder(getApplicationContext(), channel11d)
                .setContentTitle("Alarm")
                .setContentText("alarmnager worked").setSmallIcon(R.drawable.ic_add_alarm_black_24dp);
        //all three parameters are important
    }
}
