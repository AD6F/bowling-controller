package com.ad6f.bowling.services.cast;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.ad6f.bowling.GameLoop;
import com.ad6f.bowling.GameSetup;
import com.ad6f.bowling.MainMenu;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class SessionManagerListenerImpl implements SessionManagerListener<CastSession> {
    private static SessionManagerListenerImpl sessionManagerListener = new SessionManagerListenerImpl();

    public static MainMenu mainActivity;

    public static GameLoop gameLoop;

    public static GameSetup gameSetup;

    public static String currentActivity;

    public void setCallBacks(@NonNull CastSession castSession) throws IOException {
        castSession.setMessageReceivedCallbacks(CastInfo.SETTING_NAMESPACE, (castDevice, namespace, message) -> {
            System.out.println(message);
            try {
                String action = new JSONObject(message).getString("action");

                if(action.equals("end")) {
                    if(gameLoop != null && currentActivity.equals(gameLoop.getLocalClassName())) {
                        gameLoop.finish();
                    }

                    else if(gameSetup != null && currentActivity.equals(gameSetup.getLocalClassName())) {
                        gameSetup.finish();
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        castSession.setMessageReceivedCallbacks(CastInfo.GAME_NAMESPACE, (castDevice, namespace, message) -> {
            System.out.println("GAME CAST MESSAGE");
            JSONObject jsonObject;

            System.out.println("ORIGINAL : "+message);
            try {
                jsonObject = new JSONObject(message);
                System.out.println(jsonObject);
                GameLoop.playerReceived(jsonObject.getString("player"));
            } catch (JSONException e) {
                System.out.println("Error cannot converted.");
                throw new RuntimeException(e);
            }
        });

        castSession.setMessageReceivedCallbacks(CastInfo.DATA_NAMESPACE, (castDevice, namespace, message) -> {
            System.out.println("DATA MESSAGE: ");
            System.out.println(message);
        });
    }

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
        castSession.sendMessage(CastInfo.NAVIGATION_NAMESPACE, "{\"page\": 0}");
        mainActivity.setAreButtonVisible(true);
        mainActivity.popupLoadingCast(false);
        try {
            setCallBacks(castSession);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            System.out.println("Session started");
            System.out.println(currentActivity);
            mainActivity.setAreButtonVisible(true);
            mainActivity.popupLoadingCast(false);

            setCallBacks(castSession);
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

    private SessionManagerListenerImpl() {
    }

    public static SessionManagerListenerImpl getInstance() {
        return sessionManagerListener;
    }
}
