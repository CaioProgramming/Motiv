package com.creat.motiv;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.creat.motiv.Beans.Quotes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        quotesdb.keepSynced(true);
        quotesArrayList = new ArrayList<>();
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Intent act;
        if (user != null){
             act = new Intent(context,MainActivity.class);
        } else {
            act = new Intent(context,Splash.class);

        }
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

    private void Notification(Context context, NotificationManager notificationManager, Intent act) {
        PendingIntent intent1 = PendingIntent.getActivity(context,100,act,PendingIntent.FLAG_UPDATE_CURRENT);
        Random r = new Random();
        System.out.println("Notify quotes " + quotesArrayList.size());
        if (quotesArrayList.size() > 0){
        int q = r.nextInt(quotesArrayList.size());
         NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                 .setAutoCancel(true)
                 .setContentIntent(intent1)
                 .setContentTitle(quotesArrayList.get(q).getQuote())
                 .setSmallIcon(R.mipmap.ic_launcher)
                  .setContentText(quotesArrayList.get(q).getAuthor());

         notificationManager.notify(100,builder.build());}
         else{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setAutoCancel(true)
                    .setContentTitle("Motiv")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("Ei, estamos com saudaes, vem ver o que anda rolando por aqui");

            notificationManager.notify(100,builder.build());
        }
    }
}
