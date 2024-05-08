package com.ad6f.bowling;

import androidx.appcompat.app.AppCompatActivity;
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

public class CastConnect extends AppCompatActivity {
    CastContext castContext;
    SessionManager sessionManager;

    private SessionManagerListener<CastSession> mSessionManagerListener =
            new SessionManagerListenerImpl(this);

    public void castClick(View view) throws JSONException {
        var object = new JSONObject();
        object.put("msg", "I always comeback!");

        // Get la currentSession et envoie un message
        sessionManager.getCurrentCastSession().sendMessage(CastInfo.SETTING_NAMESPACE, object.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_connect);

        // Lier le boutton au Cast
        CastButtonFactory.setUpMediaRouteButton(this, findViewById(R.id.cast_button));
        castContext = CastContext.getSharedInstance(this);
        sessionManager = castContext.getSessionManager();
        sessionManager.addSessionManagerListener(mSessionManagerListener, CastSession.class);
    }
}