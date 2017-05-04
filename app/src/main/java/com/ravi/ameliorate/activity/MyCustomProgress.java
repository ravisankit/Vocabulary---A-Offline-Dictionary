/*
package ravi.ameliorate.com.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import ravi.ameliorate.com.R;

public class MyCustomProgress extends ProgressDialog {

    private AnimationDrawable animation;



    public static ProgressDialog ctor(Context context) {
        MyCustomProgress dialog = new MyCustomProgress(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        return dialog;
    }

    public MyCustomProgress(Context context, int theme) {
        super(context, theme);
    }


    public MyCustomProgress(Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_custom_progress);

        ImageView la = (ImageView) findViewById(R.id.animation);
        la.setBackgroundResource(R.drawable.pro);
        animation = (AnimationDrawable) la.getBackground();
    }

    @Override
    public void show() {
        super.show();
        animation.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        animation.stop();
    }
}
*/
