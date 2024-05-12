package com.ad6f.bowling

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.flexbox.FlexboxLayout

class GameOption : AppCompatActivity() {
    private val playersSet: MutableSet<String> = LinkedHashSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_option)
        playerBox = findViewById(R.id.list)
        addButton = findViewById(R.id.add)
    }

    fun back(view: View?) {
        this.startActivity(Intent(this, MainActivity::class.java))
    }

    enum class PopupError {
        NONE,
        UNIQUE,
        MIN_LENGTH
    }

    var popupError = PopupError.NONE

    // Get the flexbox layout that will contain the buttons
    private var playerBox: FlexboxLayout? = null

    // Button that add
    private var addButton: Button? = null

    fun add(view: View?) {
        // Not working if we open other activities
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Enter player name")
        val input = EditText(this)
        input.setSingleLine()
        input.hint = "Player name"

        // Setter of the button Ok
        alertBuilder.setPositiveButton("Ok") { dialog: DialogInterface?, which: Int ->
            val playerName = input.text.toString()
            playersSet.add(playerName)
            val playerBtn = Button(this)

            // To do not let the button label be automaticly uppercase
            playerBtn.isAllCaps = false
            playerBtn.setOnClickListener { v: View? ->
                playersSet.remove(playerName)
                playerBox!!.removeView(playerBtn)
                if (!addButton!!.isEnabled) {
                    addButton!!.isEnabled = true
                }
            }
            playerBtn.text = playerName
            playerBox!!.addView(playerBtn)
            if (playersSet.size == 4) {
                addButton!!.isEnabled = false
            }
        }

        // Set the Cancel button
        alertBuilder.setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int -> }
        val al = alertBuilder.create()

        // Handling error
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0) {
                    if (popupError != PopupError.MIN_LENGTH) {
                        popupError = PopupError.MIN_LENGTH
                        al.setTitle("Player name cannot be empty")
                        al.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                    }
                } else if (playersSet.contains(s.toString())) {
                    if (popupError != PopupError.UNIQUE) {
                        popupError = PopupError.UNIQUE
                        al.setTitle("Player already exists")
                        al.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                    }
                } else {
                    if (popupError != PopupError.NONE) {
                        popupError = PopupError.NONE
                        al.setTitle("Enter player name")
                        al.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
                    }
                }
            }
        })

        // Add the input
        al.setView(input)
        al.show()
    }
}
