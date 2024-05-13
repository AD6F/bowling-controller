package com.ad6f.bowling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.ad6f.bowling.cast.CastInfo;
import com.ad6f.bowling.cast.SessionManagerListenerImpl;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private CastContext castContext;
    private SessionManager sessionManager;

    private SessionManagerListener<CastSession> mSessionManagerListener =
            new SessionManagerListenerImpl(this);

    public static boolean isCastActivated = false;

    private Dialog loadingCastDialog = null;

    private Dialog castErrorDialog = null;

    public void setAreButtonVisible(boolean areButtonVisible) {
        if(isCastActivated != areButtonVisible) {
            isCastActivated = areButtonVisible;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lier le boutton au Cast
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), findViewById(R.id.cast_media));
        castContext = CastContext.getSharedInstance(getApplicationContext());
        sessionManager = castContext.getSessionManager();
        sessionManager.addSessionManagerListener(mSessionManagerListener, CastSession.class);
    }

    public void leave(View view) throws JSONException {

    }

    public void play(View view) {
        if(isCastActivated)  {
            this.startActivity(new Intent(this, GameSetup.class));
        } else {
            popupCast();
        }
    }

    private void popupCast() {
        if(castErrorDialog == null) {
            castErrorDialog = new AlertDialog.Builder(this)
                .setTitle("Bowling")
                .setMessage("You need to connect to the chromecast first.")
                .setPositiveButton("Ok", (dialog, which) -> {})
                .create();
        }

        castErrorDialog.show();
    }

    public void popupLoadingCast(boolean open) {
        if(loadingCastDialog == null) {
            loadingCastDialog = new AlertDialog.Builder(this)
               .setTitle("Bowling")
               .setMessage("Connecting to chromecast...")
               .setCancelable(false)
               .create();
        }

        if(open) {
            loadingCastDialog.show();
        } else {
            loadingCastDialog.hide();
        }
    }
}