package com.zwt.app2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Intent intent = getIntent();
            Uri uri = intent.getData();
            if(uri != null) {
                String scheme = uri.getScheme();
                String host = uri.getHost();
                String path = uri.getPath();
                String p1 = uri.getQueryParameter("p1");
                String p2 = uri.getQueryParameter("p2");
                Log.e("zhang", "scheme [" + scheme + "] host[" + host +"] path [" + path +"] p1 [" + p1 +"] p2 [" + p2 +"]");
            }
        }catch (Throwable e){
            e.printStackTrace();
        }

    }
}
