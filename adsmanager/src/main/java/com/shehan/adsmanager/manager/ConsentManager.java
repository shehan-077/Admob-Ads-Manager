package com.shehan.adsmanager.manager;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.shehan.adsmanager.callback.ConsentRequestHandler;

public class ConsentManager {

    private static ConsentInformation consentInformation;
    private static boolean privacyOptionRequired = false;
    private static boolean consentFlowComplete = false;

    public static void requestConsent(
            @NonNull Activity activity,
            boolean isTestMode,
            @Nullable String testDeviceHashedId,
            @Nullable ConsentRequestHandler listener
    ) {
        consentFlowComplete = false;
        privacyOptionRequired = false;

        consentInformation = UserMessagingPlatform.getConsentInformation(activity);

        ConsentRequestParameters.Builder paramBuilder =
                new ConsentRequestParameters.Builder();

        if (isTestMode) {
            ConsentDebugSettings.Builder debugParam = new ConsentDebugSettings.Builder(activity)
                    .setDebugGeography(
                            ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA
                    );

            if (testDeviceHashedId != null && !testDeviceHashedId.trim().isEmpty()) {
                debugParam.addTestDeviceHashedId(testDeviceHashedId);
            }

            paramBuilder.setConsentDebugSettings(debugParam.build());
        }

        ConsentRequestParameters param = paramBuilder.build();

        consentInformation.requestConsentInfoUpdate(
                activity,
                param, ()-> {

            privacyOptionRequired = consentInformation.getPrivacyOptionsRequirementStatus()
                    == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED;

            UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    activity,
                    formError -> {
                        consentFlowComplete = true;

                        if (formError != null && listener != null) {
                            listener.onConsentError(formError.getMessage());
                        }

                        if (listener != null) {
                            listener.onConsentReady(consentInformation.canRequestAds());
                        }
                    }
            );
        }, requestConsentError -> {
            consentFlowComplete = true;

            if (listener != null) {
                listener.onConsentReady(consentInformation.canRequestAds());
            }
        });
    }

    public static boolean canRequestAds() {
        return consentInformation != null && consentInformation.canRequestAds();
    }

    public static boolean isPrivacyOptionRequired() {
        return privacyOptionRequired;
    }

    public static boolean isConsentFlowComplete() {
        return consentFlowComplete;
    }

    public static void showPrivacyOptionForm(
            @NonNull Activity activity,
            @Nullable ConsentRequestHandler listener
    ) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity, formError -> {
            if (formError != null) {
                if (listener != null) {
                    listener.onConsentError(formError.getMessage());
                }
                return;
            }

            if (listener != null) {
                listener.onConsentReady(canRequestAds());
            }
        });
    }

    public static void resetForTesting(Context context) {
        ConsentInformation info = UserMessagingPlatform.getConsentInformation(context);
        info.reset();
        consentFlowComplete = false;
        privacyOptionRequired = false;
    }
}
