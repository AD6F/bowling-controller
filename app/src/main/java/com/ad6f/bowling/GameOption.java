package com.ad6f.bowling;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.flexbox.FlexboxLayout;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.DoubleConsumer;

public class GameOption extends AppCompatActivity {
    private final Set<String> playersSet = new LinkedHashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_option);
        playerBox = this.findViewById(R.id.list);
        addButton = this.findViewById(R.id.add);
    }

    public void back(View view) {
        this.startActivity(new Intent(this, MainActivity.class));
    }

    public enum PopupError {
        NONE,
        UNIQUE,
        MIN_LENGTH
    }

    public PopupError popupError = PopupError.NONE;

    // Get the flexbox layout that will contain the buttons
    private FlexboxLayout playerBox;

    // Button that add
    private Button addButton;

    public void add(View view) {
        // Not working if we open other activities
        var alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Enter player name");

        var input = new EditText(this);
        input.setSingleLine();
        input.setHint("Player name");

        // Setter of the button Ok
        alertBuilder.setPositiveButton("Ok", (dialog, which) -> {
            String playerName = input.getText().toString();
            playersSet.add(playerName);

            Button playerBtn = new Button(this);

            // To do not let the button label be automaticly uppercase
            playerBtn.setAllCaps(false);

            playerBtn.setOnClickListener((v) -> {
                playersSet.remove(playerName);
                playerBox.removeView(playerBtn);

                if(!addButton.isEnabled()) {
                    addButton.setEnabled(true);
                }
            });

            playerBtn.setText(playerName);
            playerBox.addView(playerBtn);

            if(playersSet.size() == 4) {
                addButton.setEnabled(false);
            }
        });

        // Set the Cancel button
        alertBuilder.setNegativeButton("Cancel", ((dialog, which) -> {}));
        
        AlertDialog al = alertBuilder.create();

        // Handling error
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    if(popupError != PopupError.MIN_LENGTH) {
                        popupError = PopupError.MIN_LENGTH;
                        al.setTitle("Player name cannot be empty");
                        al.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                }

                else if(playersSet.contains(s.toString())) {
                    if(popupError != PopupError.UNIQUE) {
                        popupError = PopupError.UNIQUE;
                        al.setTitle("Player already exists");
                        al.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                }

                else {
                    if(popupError != PopupError.NONE) {
                        popupError = PopupError.NONE;
                        al.setTitle("Enter player name");
                        al.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                }
            }
        });

        // Add the input
        al.setView(input);
        al.show();
    }
}
