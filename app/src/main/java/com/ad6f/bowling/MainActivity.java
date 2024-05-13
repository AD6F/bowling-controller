package com.ad6f.bowling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.ad6f.bowling.cast.CastInfo;
import com.ad6f.bowling.cast.SessionManagerListenerImpl;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.Setter;

public class MainActivity extends AppCompatActivity {
    private CastContext castContext;
    private SessionManager sessionManager;

    private SessionManagerListener<CastSession> mSessionManagerListener =
            new SessionManagerListenerImpl(this);

    public boolean areButtonVisible = false;

    public void setAreButtonVisible(boolean areButtonVisible) {
        if(this.areButtonVisible != areButtonVisible) {
            this.areButtonVisible = areButtonVisible;
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
        var object = new JSONObject();
        object.put("msg", "I always comeback!");

        // Get la currentSession et envoie un message
        sessionManager.getCurrentCastSession().sendMessage(CastInfo.SETTING_NAMESPACE, object.toString());
    }

    public void play(View view) {
        if(areButtonVisible)  {
            this.startActivity(new Intent(this, GameSetup.class));
        } else {
            popupCast();
        }
    }

    private void popupCast() {
        new AlertDialog.Builder(this)
            .setTitle("Bowling")
            .setMessage("You need to connect to the chromecast first.")
            .setPositiveButton("Ok", (dialog, which) -> {})
            .show();
    }
}