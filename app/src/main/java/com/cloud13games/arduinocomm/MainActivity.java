package com.cloud13games.arduinocomm;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {

    TextView sendMessage;
    Button speechButton;
    Button sendButton;
    ListView deviceList;

    BTHandler btHandler = new BTHandler();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_list);
        deviceList = (ListView) findViewById(R.id.deviceList);

        //Bluetooth & listview chunk
        final Set<BluetoothDevice> pairedDevices =  BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        final String[] list = new String[20];
        //needed to avoid nullpointerexception
        for(int j = 0; j <= 19; j++) {
            list[j] = new String();
        }
        list[3] = "NUMBER OF DEVICES FOUND: " + pairedDevices.size();
        int i = 5;
        for (BluetoothDevice bt : pairedDevices) {
            list[i] = bt.getName() + " :: " + bt.getAddress();
            i++;
        }

        //populate listview and check for item click
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_layout, R.id.label, list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int j = 5;
                for (BluetoothDevice bt : pairedDevices) {
                    if (position == j) {
                        btHandler.setDevice(bt);
                        Toast.makeText(getApplicationContext(), bt.getAddress(), Toast.LENGTH_SHORT).show();
                    }
                    j++;
                }
                setContentView(R.layout.activity_main);
                listeners();
                btHandler.startConnection();
            }
        });
    }

    /*
        Handles the click listeners for the two buttons on the 'main' activity
    */
    private void listeners() {
        sendMessage = (TextView) findViewById(R.id.sendText);
        speechButton = (Button) findViewById(R.id.speechButton);
        sendButton = (Button) findViewById(R.id.sendButton);

        speechButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                speech();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String s = sendMessage.getText().toString();
                try {
                    sendData(s);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //VOICE TO TEXT CODE
    private static final int SPEECH_REQUEST_CODE = 0;

    private void speech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            //Insert code to handle the voice to text
            parseVoice(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
        Parses through the captured VTT as a string and checks for a command to send to the arduino
     */
    private void parseVoice(String s) {
        if(s.contains("blue") && s.contains("on")) {
            sendData("bON");
        } else if(s.contains("blue") && s.contains("off")) {
            sendData("bOFF");
        } else if((s.contains("orange") || s.contains("yellow"))&& s.contains("on")) {
            sendData("oON");
        } else if((s.contains("orange") || s.contains("yellow")) && s.contains("off")) {
            sendData("oOFF");
        } else {
            sendMessage.setText(s);
        }

    }

    /*
        Sends data to the arduino
     */
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