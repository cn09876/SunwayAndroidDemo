package com.example.cn09876.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import sw.ui.SwForm;
import sw.ui.SwListView;
import sw.ui.SwTest;


public class MainActivity extends SwForm {

    private ImageView mImg;
    SwTest pp1;
    SwListView lst1;

    static
    {
        System.loadLibrary("sw_jni");
    }



    private native String get_pwd(int idx);
    public  String svv=sww.util.get_enc()+"";
    public int iq1=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImg=(ImageView)findViewById(R.id.imageView);
        mImg.setBackgroundColor(Color.rgb(255,0,0));

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = get_pwd(1)+""+svv;
                ((Button) findViewById(R.id.button)).setText(s);
                iq1+=1111;
                lst1.add(iq1+"");
                if(lst1.count()>9)
                {
                    lst1.clear();
                    iq1=1111;
                    //TODO: have a test
                }
            }
        });

        //SwUtil.get_version();

        pp1=(SwTest)findViewById(R.id.pp1);
        lst1=(SwListView)findViewById(R.id.lst1);
        lst1.setSwDelegate(new SwListView.SwDelegate() {
            @Override
            public void OnItemSelect(int pos)
            {

            }

            @Override
            public View OnGetView(int position, View convertView, ViewGroup parent)
            {
                TextView tv=new TextView(MainActivity.this);
                tv.setTextColor(Color.YELLOW);
                tv.setBackgroundColor(Color.BLACK);
                tv.setTextSize(60);
                tv.setText(position+"");
                return tv;
            }
        });



    }


}
