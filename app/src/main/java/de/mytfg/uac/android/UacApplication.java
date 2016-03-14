package de.mytfg.uac.android;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import de.mytfg.uac.signal.SignalConfig;


@ReportsCrashes(
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "https://acra.mytfg.de/acra-adventskalender/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "adventskalender_app",
        formUriBasicAuthPassword = "1hv8AeshKfgWyKtbsJxM420La8iLcRUlEg==",

        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast
)

public class UacApplication extends Application {
    public static SignalConfig signalConfig;

    @Override
    public void onCreate() {
        super.onCreate();

        // create signal config
        signalConfig = new SignalConfig();

        // Initialise ACRA:
        ACRA.init(this);
    }

}

