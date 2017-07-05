package com.united.mobile.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        Intent pushIntent = new Intent(context, NotificationService.class);
        context.startService(pushIntent);
        context = getApplicationContext();
        pushIntent = new Intent(context, PhoneUnlockService.class);
        context.startService(pushIntent);
    }
}
