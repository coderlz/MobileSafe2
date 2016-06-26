package com.example.linjohn.mobilesafe2.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.ui.SettingItemView;

public class SettingsActivity extends AppCompatActivity {

    private SettingItemView updateView;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //
        updateView = (SettingItemView) findViewById(R.id.v_settings_update);
        //updateView.setTitle("提示更新");

        //
        sp = getSharedPreferences("config",MODE_PRIVATE);
        editor = sp.edit();

        //
        if (sp.getBoolean("update",true)) {
            //updateView.setDesc("开启更新");
            updateView.setChecked(true);
        } else  {
            updateView.setChecked(false);
           // updateView.setDesc("关闭更新");
        }

        updateView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (sp.getBoolean("update",true)) {
                    updateView.setChecked(false);
                    //updateView.setDesc("关闭更新");
                    editor.putBoolean("update",false);

                } else  {
                    updateView.setChecked(true);
                    //updateView.setDesc("开启更新");
                    editor.putBoolean("update",true);
                }

                editor.commit();
            }
        });
    }
}
