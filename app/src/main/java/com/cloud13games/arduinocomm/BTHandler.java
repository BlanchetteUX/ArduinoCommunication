package com.cloud13games.arduinocomm;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Thomas on 1/16/2016.
 */
public class BTHandler extends Thread {

    public static String[] text = {"TEST","","","","","","","","","","","","","","","","","","",""
                                    ,"","","","","","","","","","","","","","","","","","","","",""
                                    ,"","","","","","","","","","","","","","","","","","","","",""
                                    ,"","","","","","","","","","","","","","","","","","","","",""
                                    ,"","","","","","","","","","","","","","","","","","","","",""
                                    ,"","","","","","","","","","","","","","","","","","","","",""
                                    ,"","","","","","","","","","","","","","","","","","","","",""};

    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String MAC_ID = "98:D3:31:FB:14:C6";

    private ArrayAdapter<BluetoothDevice> btArray = null;

    private BluetoothSocket btSocket;
    private BluetoothDevice btDevice;
    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    private OutputStream outputStream;

    public BTHandler() {
    }

    public void startConnection() {
        btAdapter.cancelDiscovery();

        BluetoothDevice device = this.getDevice();
        BluetoothSocket tempBT = null;
        OutputStream tempOut = null;

        try {
            tempBT = this.getDevice().createRfcommSocketToServiceRecord(SPP_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            tempOut = btSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btSocket = tempBT;
        outputStream = tempOut;


        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e1) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void stopConnection() {
        try {
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //PROBLEM!!!
    //HELP ME
    //FUCK
    //IT SHOWS NO BONDED DEVICES
    //HOW THE FUCK IS THAT POSSIBLE
    public void writeList() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        ArrayAdapter<String> devicesArray = null;
        if (devices.size() > 0) {
            //Object[] array = devices.toArray();
            //text[1] = array[1].toString();
            int i = 1;
            for (BluetoothDevice device : devices) {
                devicesArray.add(device.getName() + "/n" + device.getAddress());
                btArray.add(device);

                text[i] = devicesArray.getItem(i);
                i++;
            }
        }
    }

    public void setDevice() {
        BluetoothDevice device = null;
        device.equals(btArray.getItem(0));
        btDevice = device;
    }

    private BluetoothDevice getDevice() {
        return btDevice;
    }
}
