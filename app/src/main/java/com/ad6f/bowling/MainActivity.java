package com.ad6f.bowling;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.ad6f.bowling.services.cast.SessionManagerListenerImpl;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    public static boolean isCastActivated = false;

    public static boolean isCastLoading = false;

    private Dialog loadingCastDialog = null;

    public void refreshButton() {
        findViewById(R.id.play).setEnabled(isCastActivated);
        findViewById(R.id.setting).setEnabled(isCastActivated);
    }

    public void setAreButtonVisible(boolean areButtonVisible) {
        isCastActivated = areButtonVisible;
        refreshButton();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(loadingCastDialog == null) {
            loadingCastDialog = new AlertDialog.Builder(this)
                .setTitle("Bowling")
                .setMessage("Connecting to chromecast...")
                .setCancelable(false)
                .create();
        }

        // Lier le boutton au Cast
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), findViewById(R.id.cast_media));
        var castContext = CastContext.getSharedInstance(getApplicationContext());
        var sessionManager = castContext.getSessionManager();
        var sessionManagerListener = new SessionManagerListenerImpl(this);
        sessionManager.addSessionManagerListener(sessionManagerListener, CastSession.class);
    }

    public void leave(View view) throws JSONException {

    }

    public void play(View view) {
        this.startActivity(new Intent(this, GameSetup.class));
    }

    private void refreshLoadingCastDialog() {
        if(isCastLoading) {
            loadingCastDialog.show();
        } else {
            loadingCastDialog.hide();
        }
    }

    public void popupLoadingCast(boolean isLoading) {
        isCastLoading = isLoading;
        refreshLoadingCastDialog();
    }

    // This is override because, this function is executed after the onCreate function and when we
    // rotate the function onCreate is recalled and this function too, we use this function to put the popup and
    // the buttons to their currentState.
    @Override
    protected void onStart() {
        super.onStart();
        refreshButton();
        refreshLoadingCastDialog();
    }
}