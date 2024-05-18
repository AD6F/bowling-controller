package com.ad6f.bowling.components.mainmenu;

import android.app.AlertDialog;
import android.content.Context;
import androidx.annotation.NonNull;

public class LoadingCastDialog extends CastDialog {
    public LoadingCastDialog(@NonNull Context context) {
        super(context);
        super.setMessage("Connecting to chromecast...");
        super.setCancelable(false);
    }
}
