package com.example.cn09876.myapp;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import sw.ui.SwForm;
import sw.ui.SwScrollView;


public class MainActivity extends SwForm {


    //TODO 变量定义的不好看啊
    static
    {
        System.loadLibrary("sw_jni");
    }

    SwScrollView swsv;
    ImageView img1;

    private native String get_pwd(int idx);
    public  String svv=sww.util.get_enc()+"";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = get_pwd(1) + "" + svv;
                ((Button) findViewById(R.id.button)).setText(s);
            }
        });




        findViewById(R.id.btn_h).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        findViewById(R.id.bnt_v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        img1=(ImageView)findViewById(R.id.img_1);

        swsv=(SwScrollView)findViewById(R.id.swsv);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.RED);
        gd.setCornerRadius(10);
        gd.setStroke(2, Color.WHITE);

        swsv.setBackground(gd);


        swsv.setOnZoomInOut(new SwScrollView.SwOnZoom() {
            @Override
            public void onZoomInOut(int typ, float f) {
                int i = 0;
            }

            @Override
            public void onFlyLeft() {

            }

            @Override
            public void onFlyRight() {

            }
        });

    }


}
