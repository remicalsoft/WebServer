package net.dixq.webserver2;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements OnMessageListener {

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);

        WebServer.INSTANCE.start(this);

        new Handler().postDelayed(()->{ // 3秒後に自分に対してメッセージをpostする
            Poster.INSTANCE.post("http://192.168.0.43:8080/message", "Hello, Server!");
        }, 3000);
    }

    @Override
    public void OnMessage(@NonNull String str) {
        Log.e("hoge", "受信したメッセージ: "+str);
    }

}
