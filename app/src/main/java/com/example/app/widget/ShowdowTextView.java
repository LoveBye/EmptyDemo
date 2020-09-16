package com.example.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.app.R;

@SuppressLint("AppCompatCustomView")
public class ShowdowTextView extends TextView {
    public ShowdowTextView(Context context) {
        super(context);
        initTextView(context);
    }

    public ShowdowTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initTextView(context);
    }

    public ShowdowTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTextView(context);
    }

    private void initTextView(Context context) {
        setShadowLayer(15, 10, 10, context.getResources().getColor(R.color.gainsboro));
    }
}