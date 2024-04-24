package com.ad6f.bowling;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.flexbox.FlexboxLayout;

import java.util.LinkedHashSet;
import java.util.Set;

public class GameOption extends AppCompatActivity {
    private Set<String> players = new LinkedHashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_option);
    }

    public void back(View view) {
        this.startActivity(new Intent(this, MainActivity.class));
    }

    public void add(View view) {
        // Not working if we open other activities
        var alert = new AlertDialog.Builder(this);
        var input = new EditText(this);

        input.setSingleLine();
        input.setHint("Player name");
        alert.setView(input);

        alert.setTitle("Add Player : ");
        alert.setPositiveButton("Ok", (dialog, which) -> {
            System.out.println(dialog+" "+ which);

            if(input.getText().length() != 0) {
                String playerName = input.getText().toString();
                players.add(playerName);

                FlexboxLayout textView = this.findViewById(R.id.list);
                Button player = new Button(this);
                player.setOnClickListener((v) -> {
                    players.remove(playerName);
                    textView.removeView(player);
                });
                player.setText(playerName);
                textView.addView(player);
            }
        });

        alert.setNegativeButton("Cancel", (dialog, which) -> {

        });
        alert.show();
    }
}
