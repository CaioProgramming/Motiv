package com.creat.motiv.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.creat.motiv.R;
import com.github.mmin18.widget.RealtimeBlurView;

public class Info {
    public static void tutorial(String tutorial, Activity activity) {
        final RealtimeBlurView blur = activity.findViewById(R.id.rootblur);
        blur.setVisibility(View.VISIBLE);
        final Dialog m_dialog = new Dialog(activity, R.style.Dialog_No_Border);
        m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        m_dialog.setCanceledOnTouchOutside(true);
        LayoutInflater m_inflater = LayoutInflater.from(activity);
        final View m_view = m_inflater.inflate(R.layout.quotepopup, null);
        m_dialog.setContentView(m_view);
        LinearLayout popup = m_view.findViewById(R.id.popup);
        TextView author = m_view.findViewById(R.id.author);
        TextView quote = m_view.findViewById(R.id.quote);
        quote.setText(tutorial);
        quote.setTextSize(16);
        quote.setTextColor(Color.WHITE);
        author.setVisibility(View.GONE);
        popup.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
        m_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                blur.setBlurRadius(0);
            }
        });
        m_dialog.show();
        CountDownTimer timer = new CountDownTimer(5000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                m_dialog.dismiss();
            }
        }.start();
    }



}