package com.ad6f.bowling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.cast.framework.CastContext;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> players = new ArrayList<>();
    CastContext castContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void leave(View view) {
        // Not working if we open other activities
        //this.finish();
        this.startActivity(new Intent(this, CastConnect.class));
    }

    public void play(View view) {
        this.startActivity(new Intent(this, GameOption.class));
    }
}