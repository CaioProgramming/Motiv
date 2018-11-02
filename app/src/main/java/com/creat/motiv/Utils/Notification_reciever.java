package com.creat.motiv.Utils;

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
import java.util.Random;

import static com.creat.motiv.Database.QuotesDB.path;

public class Notification_reciever extends BroadcastReceiver {
    private Query quotesdb;
    ArrayList<Quotes> quotesArrayList;



    @Override
    public void onReceive(final Context context, Intent intent) {
        quotesdb = FirebaseDatabase.getInstance().getReference();
         if (quotesArrayList != null){
             return;
         }else {
             quotesArrayList = new ArrayList<>();
             final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
             final Intent act;
             act = new Intent(context, MainActivity.class);
             act.putExtra("notification", true);
             quotesdb = FirebaseDatabase.getInstance().getReference().child(path);
             quotesdb.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     quotesArrayList.clear();
                     for (DataSnapshot d : dataSnapshot.getChildren()) {
                         Quotes quotes = new Quotes();
                         Quotes q = d.getValue(Quotes.class);
                         if (q != null) {
                             quotes.setId(d.getKey());
                             quotes.setAuthor(q.getAuthor());
                             quotes.setQuote(q.getQuote());
                             quotes.setUsername(q.getUsername());
                             quotesArrayList.add(quotes);
                             System.out.println("Quotes " + quotesArrayList.size());


                         }
                     }
                     Notification(context, notificationManager, act);


                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {
                     System.out.println(databaseError.getMessage());
                 }
             });
         }

    }

    private void Notification(Context context, NotificationManager notificationManager, Intent act) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,act,PendingIntent.FLAG_UPDATE_CURRENT);
        Random r = new Random();
        System.out.println("Notify quotes " + quotesArrayList.size());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Splash.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(act);
        if (quotesArrayList.size() > 0){
        int q = r.nextInt(quotesArrayList.size());
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
            notificationBuilder.setContentText(quotesArrayList.get(q).getQuote());
            notificationBuilder.setContentTitle(quotesArrayList.get(q).getAuthor());
            if (quotesArrayList.get(q).getUsername() != null){
            notificationBuilder.setSubText("Postado por " + quotesArrayList.get(q).getUsername());}
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle());
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            notificationBuilder.setVibrate(new long[] { 100, 500});
            notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setSound(Uri.parse("android.resource://com.creat.motiv/"+R.raw.light));
            notificationManager.notify(0,notificationBuilder.build());
        }

    }
}
