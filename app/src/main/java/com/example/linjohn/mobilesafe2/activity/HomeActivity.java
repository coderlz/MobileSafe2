package com.example.linjohn.mobilesafe2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public class HomeActivity extends AppCompatActivity {

    private GridView gv_home_items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //
        gv_home_items = (GridView) findViewById(R.id.gv_home_items);
        gv_home_items.setAdapter(new HomeItemsAdapter());
        gv_home_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 8) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(),SettingsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    String[] itemNames = new String[]{"手机防盗","通讯卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理",
    "高级工具","设置中心"};
    int[] imageNames = new int[]{R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,R.drawable.taskmanager,
    R.drawable.netmanager,R.drawable.trojan,R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};
    private class HomeItemsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(getApplicationContext(),R.layout.item_home,null);
            ImageView imageView =(ImageView) view.findViewById(R.id.iv_home_item);
            imageView.setImageResource(imageNames[position]);
            TextView textView = (TextView) view.findViewById(R.id.tv_home_item);
            textView.setText(itemNames[position]);

            return view;
        }


        @Override
        public int getItemViewType(int position) {
            return 0;
        }


    }
}
