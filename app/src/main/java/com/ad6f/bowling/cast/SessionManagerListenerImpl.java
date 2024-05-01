package com.ad6f.bowling.cast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ad6f.bowling.MainActivity;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;

import java.io.IOException;

public class SessionManagerListenerImpl implements SessionManagerListener<CastSession> {
    private AppCompatActivity mainActivity;

    @Override
    public void onSessionEnded(@NonNull CastSession castSession, int i) {

    }

    @Override
    public void onSessionEnding(@NonNull CastSession castSession) {

    }

    @Override
    public void onSessionResumeFailed(@NonNull CastSession castSession, int i) {

    }

    @Override
    public void onSessionResumed(@NonNull CastSession castSession, boolean b) {

    }

    @Override
    public void onSessionResuming(@NonNull CastSession castSession, @NonNull String s) {

    }

    @Override
    public void onSessionStartFailed(@NonNull CastSession castSession, int i) {

    }

    @Override
    public void onSessionStarted(@NonNull CastSession castSession, @NonNull String s) {
        try {
            castSession.setMessageReceivedCallbacks(CastInfo.NAMESPACE, (castDevice, namespace, message) -> {
                // NEED TO ADD CODE
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSessionStarting(@NonNull CastSession castSession) {

    }

    @Override
    public void onSessionSuspended(@NonNull CastSession castSession, int i) {

    }

    public SessionManagerListenerImpl(AppCompatActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
