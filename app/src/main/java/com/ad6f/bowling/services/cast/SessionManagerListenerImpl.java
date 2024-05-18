package com.ad6f.bowling.services.cast;

import androidx.annotation.NonNull;

import com.ad6f.bowling.MainMenu;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;

import java.io.IOException;

public class SessionManagerListenerImpl implements SessionManagerListener<CastSession> {
    private MainMenu mainActivity;

    @Override
    public void onSessionEnded(@NonNull CastSession castSession, int i) {
        mainActivity.popupLoadingCast(false);
        mainActivity.setAreButtonVisible(false);
    }

    @Override
    public void onSessionEnding(@NonNull CastSession castSession) {

    }

    @Override
    public void onSessionResumeFailed(@NonNull CastSession castSession, int i) {
        mainActivity.popupLoadingCast(false);
        mainActivity.setAreButtonVisible(false);
        mainActivity.popupErrorCast(true);
    }

    @Override
    public void onSessionResumed(@NonNull CastSession castSession, boolean b) {

        mainActivity.setAreButtonVisible(true);
        mainActivity.popupLoadingCast(false);
    }

    @Override
    public void onSessionResuming(@NonNull CastSession castSession, @NonNull String s) {
        mainActivity.popupLoadingCast(true);
        mainActivity.setAreButtonVisible(false);
    }

    @Override
    public void onSessionStartFailed(@NonNull CastSession castSession, int i) {
        mainActivity.popupLoadingCast(false);
        mainActivity.setAreButtonVisible(false);
        mainActivity.popupErrorCast(true);
    }

    @Override
    public void onSessionStarted(@NonNull CastSession castSession, @NonNull String s) {
        try {

            castSession.setMessageReceivedCallbacks(CastInfo.SETTING_NAMESPACE, (castDevice, namespace, message) -> {
                System.out.println(message);
            });

            mainActivity.setAreButtonVisible(true);
            mainActivity.popupLoadingCast(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSessionStarting(@NonNull CastSession castSession) {
        mainActivity.popupLoadingCast(true);
    }

    @Override
    public void onSessionSuspended(@NonNull CastSession castSession, int i) {

    }

    public SessionManagerListenerImpl(MainMenu mainActivity) {
        this.mainActivity = mainActivity;
    }
}
