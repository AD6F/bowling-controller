package com.ad6f.bowling;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.ad6f.bowling.components.mainmenu.ErrorCastDialog;
import com.ad6f.bowling.components.mainmenu.LoadingCastDialog;
import com.ad6f.bowling.services.cast.SessionManagerListenerImpl;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;

public class MainMenu extends AppCompatActivity {
    public static boolean areButtonVisible = false;

    private LoadingCastDialog loadingCastDialog = null;

    public static boolean isCastLoading = false;

    private ErrorCastDialog errorCastDialog = null;

    public static boolean isErrorDialog = false;

    public void refreshButton() {
        findViewById(R.id.play).setEnabled(areButtonVisible);
        findViewById(R.id.setting).setEnabled(areButtonVisible);
    }

    public void setAreButtonVisible(boolean areButtonVisible) {
        MainMenu.areButtonVisible = areButtonVisible;
        refreshButton();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(loadingCastDialog == null) {
            loadingCastDialog = new LoadingCastDialog(this);
        }

        if(errorCastDialog == null) {
            errorCastDialog = new ErrorCastDialog(this, (dialog, which) -> {
                isErrorDialog = false;
            });
        }

        // Lier le boutton au Cast
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), findViewById(R.id.cast_media));
        var castContext = CastContext.getSharedInstance(getApplicationContext());
        var sessionManager = castContext.getSessionManager();
        var sessionManagerListener = SessionManagerListenerImpl.getInstance();

        SessionManagerListenerImpl.mainActivity = this;
        SessionManagerListenerImpl.currentActivity = this.getLocalClassName();
        sessionManager.addSessionManagerListener(sessionManagerListener, CastSession.class);
    }

    public void leave(View view) {

    }

    public void play(View view) {
        GameSetup.reset();
        this.startActivity(new Intent(this, GameSetup.class));
    }

    public void popupLoadingCast(boolean isLoading) {
        isCastLoading = isLoading;
        loadingCastDialog.refresh(isCastLoading);
    }

    public void popupErrorCast(boolean isOpen) {
        isErrorDialog = isOpen;
        errorCastDialog.refresh(isErrorDialog);
    }

    // This is override because, this function is executed after the onCreate function and when we
    // rotate the function onCreate is recalled and this function too, we use this function to put the popup and
    // the buttons to their currentState.
    @Override
    protected void onStart() {
        super.onStart();
        refreshButton();
        loadingCastDialog.refresh(isCastLoading);
        errorCastDialog.refresh(isErrorDialog);
    }
}