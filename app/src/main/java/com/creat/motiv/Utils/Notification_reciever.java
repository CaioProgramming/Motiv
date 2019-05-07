package com.creat.motiv.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.creat.motiv.Beans.Quotes;
import com.creat.motiv.MainActivity;
import com.creat.motiv.R;
import com.creat.motiv.Splash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static com.creat.motiv.Database.QuotesDB.path;

public class Notification_reciever extends BroadcastReceiver {
    private Query quotesdb;
    Quotes quotes;
    ArrayList<Quotes> quotesArrayList;



    @Override
    public void onReceive(final Context context, final Intent intent) {
        Pref preferences = new Pref(context);
        if (preferences.alarm()) {
            quotesdb = FirebaseDatabase.getInstance().getReference();
            quotesdb.keepSynced(false);

                quotesdb = FirebaseDatabase.getInstance().getReference().child(path);

                quotesdb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Random random = new Random();
                        int questionCount = (int) dataSnapshot.getChildrenCount();
                        int rand = random.nextInt(questionCount);
                        Iterator itr = dataSnapshot.getChildren().iterator();
                        for(int i = 0; i < rand; i++) {
                            itr.next();
                        }
                        DataSnapshot childSnapshot = (DataSnapshot) itr.next();

                        quotes = childSnapshot.getValue(Quotes.class);
                        Notification(context);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }


    private void Notification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent act;
        act = new Intent(context, MainActivity.class);
        act.putExtra("notification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, act, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Splash.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(act);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
            notificationBuilder.setContentText(quotes.getQuote());
            notificationBuilder.setContentTitle(quotes.getAuthor());
            if (quotes.getUsername() != null){
            notificationBuilder.setSubText("Postado por " + quotes.getUsername());}
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle());
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            notificationBuilder.setVibrate(new long[] { 100, 500});
            notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        notificationBuilder.setCategory(Notification.CATEGORY_MESSAGE);

        notificationBuilder.setSmallIcon(R.drawable.ic_notification);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setSound(Uri.parse("android.resource://com.creat.motiv/"+R.raw.light));
        if (notificationManager != null) {
            notificationManager.notify(0,notificationBuilder.build());
        }

    }
}
