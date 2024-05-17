package com.ad6f.bowling.components.mainmenu;

import android.app.AlertDialog;
import android.content.Context;
import androidx.annotation.NonNull;

public class LoadingCastDialog extends AlertDialog {
    public LoadingCastDialog(@NonNull Context context) {
        super(context);
        super.setTitle("Bowling");
        super.setMessage("Connecting to chromecast...");
        super.setCancelable(false);
        super.isShowing();
    }

    public void refresh(boolean isLoading) {
        if(isLoading) {
            this.show();
        } else {
            this.hide();
        }
    }
}
