package org.sxchinacourt.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.sxchinacourt.R;


public class CustomProgress extends LinearLayout {

    private ImageView imageView;

    private Context context;

    public CustomProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_progress, this, true);
        imageView = (ImageView) view.findViewById(R.id.common_loading_dialog);
    }

    public void start() {
        this.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    public void stop() {
        this.setVisibility(View.GONE);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.stop();

    }

}
