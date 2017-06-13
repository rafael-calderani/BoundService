package com.rm39951.boundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    BoundService mBoundService;
    boolean mServiceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView timeStampText = (TextView) findViewById(R.id.tvTimerStamp);
        Button btnIniciaStamp = (Button) findViewById(R.id.btnStartStamp);
        Button btnParaStamp = (Button) findViewById(R.id.btnStopStamp);
        btnIniciaStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mServiceBound) bind();
                timeStampText.setText(mBoundService.getTimeStamp());
            }
        });

        btnParaStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mServiceBound) unbind();
                Intent i = new Intent(MainActivity.this, BoundService.class);
                stopService(i);
                TextView timeStampText = (TextView) findViewById(R.id.tvTimerStamp);
                timeStampText.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bind();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mServiceBound) unbind();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.ServiceBinder binder = (BoundService.ServiceBinder) service;
            mBoundService = binder.getService();
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };

    private void unbind() {
        unbindService(mServiceConnection);
        mServiceBound = false;
    }

    private void bind() {
        Intent i = new Intent(this, BoundService.class);
        startService(i);
        bindService(i, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
}
