package com.ad6f.bowling;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class GameOption extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_option);
    }

    public void back(View view) {
        this.startActivity(new Intent(this, MainActivity.class));
    }
}
