package com.ad6f.bowling;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.ad6f.bowling.cast.SessionManagerListenerImpl;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;

public class CastConnect extends AppCompatActivity {
    CastContext castContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_connect);

        /*
        // Lier le boutton au Cast
        CastButtonFactory.setUpMediaRouteButton(this, findViewById(R.id.));
        castContext = CastContext.getSharedInstance(this);

        // rajouter le session listener au session manager
        castContext.getSessionManager().addSessionManagerListener(new SessionManagerListenerImpl(this), CastSession.class);
        */
    }
}