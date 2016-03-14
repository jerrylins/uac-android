package de.mytfg.uac.android;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.acra.ACRA;

import java.io.DataOutputStream;
import java.io.IOException;

import de.mytfg.uac.signal.SignalConfig;
import de.mytfg.uac.signal.SignalOutputStream;
import de.mytfg.uac.util.ByteUtil;
import de.mytfg.uac.wave.stream.OutputWaveAndroid;


public class AudioService extends IntentService {
    private static final String TAG = "AudioService-class";
    private static final byte START_BYTE = ByteUtil.toByteArray("10011001")[0];

    public AudioService() {
        super("UAC-AudioService");
        Log.d(TAG, "AudioService() with name=UAC-AudioService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AudioService(String name) {
        super(name);
        Log.d(TAG, "AudioService() with name=" + name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Toast.makeText(this, "onHandleIntent()", Toast.LENGTH_LONG).show();

        SignalConfig config = UacApplication.signalConfig;

        // Gets data from the incoming Intent
        String dataString = intent.getStringExtra("text");
        if  (dataString == null) {
            dataString = "No string found";
        }

        Toast.makeText(this, "dataString=" + dataString, Toast.LENGTH_LONG).show();
        Log.d(TAG, "dataString=" + dataString);

        Boolean isBitSequence = intent.getBooleanExtra("isBitSequence", false);
        Boolean sendStartByte = intent.getBooleanExtra("sendStartByte", false);
        // Do work here, based on the contents of dataString
        OutputWaveAndroid outAndroid = new OutputWaveAndroid(config.getInt("samplingrate"), this);
        SignalOutputStream outSignal = new SignalOutputStream(outAndroid, config);
        DataOutputStream out = new DataOutputStream(outSignal);

        try {
            outSignal.synchronize();
            if (!isBitSequence) {
                if (sendStartByte) {
                    out.write(START_BYTE);
                }
                out.writeUTF(dataString);
            }
            else {
                byte[] data = ByteUtil.toByteArray(dataString);
                outSignal.write(data);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error writing audio", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Error writing audio");
            ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
        }

    }
}
