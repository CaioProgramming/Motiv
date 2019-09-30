package com.creat.motiv.Utils;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;


public class Typewritter extends androidx.appcompat.widget.AppCompatTextView {
    private CharSequence text;
    private int index;
    private long delay = 100;
    private Handler handler = new Handler();
    private Runnable addcharacter = new Runnable() {
        @Override
        public void run() {
            setText(text.subSequence(0, index++));
            if (index < text.length() + 1) {
                handler.postDelayed(addcharacter, delay);
            }

        }
    };

    public Typewritter(Context context) {
        super(context);
    }

    public Typewritter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void animateText(String text) {
        this.text = text;
        index = 0;
        setText("");
        handler.removeCallbacks(addcharacter);
        handler.postDelayed(addcharacter, delay);
    }
}
