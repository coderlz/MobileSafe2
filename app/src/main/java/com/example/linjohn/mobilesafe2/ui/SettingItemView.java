package com.example.linjohn.mobilesafe2.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.linjohn.mobilesafe2.R;


/**
 * Created by Administrator on 6/11/2016.
 */
public class SettingItemView extends RelativeLayout{

    private TextView tv_settings_title;
    private TextView tv_settings_desc;
    private CheckBox cb_settings_update;
    private String title;
    private String desc_on;
    private String desc_off;

    public SettingItemView(Context context) {
        super(context);
        init();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SettingItemView,
                0,0
                );

        try {

            title = a.getString(R.styleable.SettingItemView_custom_title);
            desc_on = a.getString(R.styleable.SettingItemView_desc_on);
            desc_off = a.getString(R.styleable.SettingItemView_desc_off);

        } finally {

            a.recycle();
        }

        if (isChecked()) {
            setDesc(desc_on);
        } else {

            setDesc(desc_off);
        }
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {

        //
        View settingView = View.inflate(getContext(), R.layout.item_settings,null);
        this.addView(settingView);

        //
        tv_settings_title = (TextView) settingView.findViewById(R.id.tv_settings_title);
        tv_settings_desc = (TextView)settingView.findViewById(R.id.tv_settings_desc);
        cb_settings_update = (CheckBox)settingView.findViewById(R.id.cb_settings_update);

    }

    public void setTitle(String title) {

        tv_settings_title.setText(title);
    }

    public void setDesc(String desc) {

        tv_settings_desc.setText(desc);
    }

    public void setChecked(boolean checked) {

        cb_settings_update.setChecked(checked);

        if (isChecked()) {
            setDesc(desc_on);


        } else {

            setDesc(desc_off);
        }
    }

    public boolean isChecked() {

        return cb_settings_update.isChecked();
    }
}
