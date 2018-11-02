package com.creat.motiv.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.creat.motiv.R;
import com.creat.motiv.Splash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Notifications extends FirebaseMessagingService{


    public Notifications() {
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
      notification(remoteMessage);



    }

    private void notification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this,Splash.class);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("Ei " + user.getDisplayName());
        notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle());
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        notificationBuilder.setVibrate(new long[] { 100, 1000});
        notificationBuilder.setSound(Uri.parse("android.resource://com.creat.motiv/"+R.raw.plucky));
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0,notificationBuilder.build());
    }

}
