package com.shehan.adsmanager.loadingview;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.RenderMode;
import com.airbnb.lottie.model.KeyPath;
import com.shehan.adsmanager.R;

public final class LoadingOverlay {

    private Dialog dialog;
    private LottieAnimationView lottie;

    public void show(@NonNull Activity activity, @Nullable Integer tintColor) {
        if (activity.isFinishing()) return;
        if (dialog != null && dialog.isShowing()) return;

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_loading);
        dialog.setCancelable(false);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        lottie = dialog.findViewById(R.id.lottieView);
        lottie.setRenderMode(RenderMode.HARDWARE);
        lottie.setRepeatCount(LottieDrawable.INFINITE);

        if (tintColor != null) {
            CustomizeLoadingView(tintColor);
        }

        lottie.playAnimation();

        dialog.show();

        if (activity instanceof LifecycleOwner) {
            LifecycleOwner owner = (LifecycleOwner) activity;
            owner.getLifecycle().addObserver(new DefaultLifecycleObserver() {
                @Override public void onDestroy(@NonNull LifecycleOwner lifecycleOwner) {
                    dismiss();
                    owner.getLifecycle().removeObserver(this);
                }
            });
        }
    }

    public void dismiss() {
        if (lottie != null) lottie.cancelAnimation();
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
        lottie = null;
        dialog = null;
    }

    private void CustomizeLoadingView(int color) {
        if (lottie == null) return;

        lottie.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                frameInfo -> new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        );
    }
}
