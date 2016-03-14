package de.mytfg.uac.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    Context context;

    private Button sendButton;
    private EditText editTxt_perperbit;
    private EditText editTxt_syncbits;
    private EditText editTxt_lowFreq;
    private EditText editTxt_highFreq;
    private EditText editTxt_text;
    private Switch switch_isBit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        initializeUI();

        // add button listeners
        addBtnListeners();
    }

    private void initializeUI () {
        editTxt_syncbits = (EditText)findViewById(R.id.editTxt_syncbits);
        editTxt_perperbit = (EditText)findViewById(R.id.editTxt_perperbit);
        editTxt_lowFreq = (EditText)findViewById(R.id.editTxt_lowFreq);
        editTxt_highFreq = (EditText)findViewById(R.id.editTxt_highFreq);
        editTxt_text = (EditText)findViewById(R.id.editTxt_text);
        switch_isBit = (Switch)findViewById(R.id.switch_bits);
    }


    public void addBtnListeners() {

        // send button
        sendButton = (Button) findViewById(R.id.btn_send);
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // read values from UI
                int periodsperbit = Integer.parseInt(editTxt_perperbit.getText().toString());
                String syncbits = editTxt_syncbits.getText().toString();
                int lowFreq = Integer.parseInt(editTxt_lowFreq.getText().toString());
                int highFreq = Integer.parseInt(editTxt_highFreq.getText().toString());
                boolean isBitSequence = switch_isBit.isChecked();
                String text = editTxt_text.getText().toString();
                // set fixed values
                UacApplication.signalConfig.put("samplingrate", 5000);
                UacApplication.signalConfig.put("modulation", "fm");
                // set UI values to config
                UacApplication.signalConfig.put("periodsperbit", periodsperbit);
                UacApplication.signalConfig.put("syncbits", syncbits);
                UacApplication.signalConfig.put("frequency.low", lowFreq);
                UacApplication.signalConfig.put("frequency.high", highFreq);
                // create and start sending intent
                Intent sendingIntent = new Intent(context, AudioService.class);
                sendingIntent.putExtra("text", text);
                sendingIntent.putExtra("isBitSequence",isBitSequence);
                context.startService(sendingIntent);
            }

        });

    }

}
