package com.cloud13games.arduinocomm;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends Activity {

    Boolean deviceChosen = false;

    TextView sendMessage;
    ToggleButton LED13;
    Button sendButton;
    Button refreshButton;
    Button chooseButton;
    Button chooseButton1;
    Button chooseButton2;

    BTHandler btHandler = null;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_choose);

        chooseDevice();
        chooseButton.setText(BTHandler.text[0]);
        chooseButton1.setText(BTHandler.text[1]);
        chooseButton2.setText(BTHandler.text[2]);

        refreshButton = (Button) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (refreshButton.isPressed()) {
                    setContentView(R.layout.activity_choose);
                    chooseDevice();
                    chooseButton.setText(BTHandler.text[0]);
                    chooseButton1.setText(BTHandler.text[1]);
                    chooseButton2.setText(BTHandler.text[2]);
                }
            }
        });
        if (deviceChosen) {
            setContentView(R.layout.activity_main);
            listeners();
        }
    }

    public void onStart() {
        super.onStart();

    }

    private void chooseDevice() {
        chooseButton = (Button) findViewById(R.id.deviceButton);
        chooseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseButton.isPressed()) {
                    btHandler.setDevice();
                    deviceChosen = true;
                }
            }
        });

        chooseButton1 = (Button) findViewById(R.id.deviceButton1);
        chooseButton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseButton1.isPressed()) {
                    btHandler.setDevice();
                    deviceChosen = true;
                }
            }
        });

        chooseButton2 = (Button) findViewById(R.id.deviceButton2);
        chooseButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseButton2.isPressed()) {
                    btHandler.setDevice();
                    deviceChosen = true;
                }
            }
        });

        try {
            btHandler.writeList();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void listeners() {
        LED13 = (ToggleButton) findViewById(R.id.LEDButton);
        LED13.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LED13.isChecked()) {
                    try {
                        sendData("on");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else if (!LED13.isChecked()) {
                    try {
                        sendData("off");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    sendData(sendMessage.getText().toString());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendData(String data) {
        byte[] bytes = data.getBytes();
        btHandler.write(bytes);
    }

    public void onStop() {
        super.onStop();
        try {
            btHandler.stopConnection();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}