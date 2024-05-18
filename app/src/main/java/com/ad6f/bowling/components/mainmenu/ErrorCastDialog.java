package com.ad6f.bowling.components.mainmenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import androidx.annotation.NonNull;

public class ErrorCastDialog extends CastDialog {
    public ErrorCastDialog(@NonNull Context context, OnClickListener onClickListener) {
        super(context);
        super.setMessage("Unable to establish a connection with your chromecast.");
        super.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", onClickListener);
    }
}
