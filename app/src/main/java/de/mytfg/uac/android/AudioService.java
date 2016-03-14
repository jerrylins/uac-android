package de.mytfg.uac.android;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import org.acra.ACRA;

import java.io.DataOutputStream;
import java.io.IOException;

import de.mytfg.uac.signal.SignalConfig;
import de.mytfg.uac.signal.SignalOutputStream;
import de.mytfg.uac.util.ByteUtil;
import de.mytfg.uac.wave.stream.OutputWaveAndroid;


public class AudioService extends IntentService {

    private static final byte START_BYTE = ByteUtil.toByteArray("10011001")[0];

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AudioService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SignalConfig config = new SignalConfig();

        // Gets data from the incoming Intent
        String dataString = intent.getStringExtra("text");
        if  (dataString == null) {
            dataString = "No string found";
        }
        Boolean isBitSequence = intent.getBooleanExtra("isBitSequence", false);
        // Do work here, based on the contents of dataString
        OutputWaveAndroid outAndroid = new OutputWaveAndroid(config.getInt("samplingrate"), this);
        SignalOutputStream outSignal = new SignalOutputStream(outAndroid, config);
        DataOutputStream out = new DataOutputStream(outSignal);

        try {
            outSignal.synchronize();
            if (!isBitSequence) {
                out.write(START_BYTE);
                out.writeUTF(dataString);
            }
            else {
                byte[] data = ByteUtil.toByteArray(dataString);
                outSignal.write(data);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error writing audio", Toast.LENGTH_LONG).show();
            ACRA.getErrorReporter().handleException(e);
            e.printStackTrace();
        }

    }
}
