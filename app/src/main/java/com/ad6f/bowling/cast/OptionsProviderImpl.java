package com.ad6f.bowling.cast;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;

import java.util.List;

public class OptionsProviderImpl implements OptionsProvider {
    @NonNull
    @Override
    public CastOptions getCastOptions(@NonNull Context context) {
        return new CastOptions.Builder().setReceiverApplicationId(CastInfo.APPLICATION_ID).build();
    }

    @Nullable
    @Override
    public List<SessionProvider> getAdditionalSessionProviders(@NonNull Context context) {
        return null;
    }
}
