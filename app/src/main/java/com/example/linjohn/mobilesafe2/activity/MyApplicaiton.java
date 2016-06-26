package com.example.linjohn.mobilesafe2.activity;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 5/9/2016.
 */
public class MyApplicaiton extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
    }
}
