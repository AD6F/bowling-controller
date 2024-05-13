package com.ad6f.bowling.cast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ad6f.bowling.MainActivity;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SessionManagerListenerImpl implements SessionManagerListener<CastSession> {
    private MainActivity mainActivity;

    @Override
    public void onSessionEnded(@NonNull CastSession castSession, int i) {
        mainActivity.setAreButtonVisible(false);
    }

    @Override
    public void onSessionEnding(@NonNull CastSession castSession) {

    }

    @Override
    public void onSessionResumeFailed(@NonNull CastSession castSession, int i) {

    }

    @Override
    public void onSessionResumed(@NonNull CastSession castSession, boolean b) {

        mainActivity.setAreButtonVisible(true);
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
            castSession.setMessageReceivedCallbacks(CastInfo.SETTING_NAMESPACE, (castDevice, namespace, message) -> {
                // NEED TO ADD CODE
            });

            mainActivity.setAreButtonVisible(true);
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

    public SessionManagerListenerImpl(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
