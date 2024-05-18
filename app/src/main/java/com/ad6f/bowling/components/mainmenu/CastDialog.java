package com.ad6f.bowling.components.mainmenu;

import android.app.AlertDialog;
import android.content.Context;
import androidx.annotation.NonNull;

public class CastDialog extends AlertDialog {
    public CastDialog(@NonNull Context context) {
        super(context);
    }

    public void refresh(boolean isLoading) {
        if(isLoading) {
            this.show();
        } else {
            this.hide();
        }
    }
}
