package com.cloud13games.arduinocomm;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Thomas on 1/16/2016.
 * Allows bluetooth communication to be handled on a seperate thread
 */
public class BTHandler extends Thread {

    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothSocket btSocket;
    private BluetoothDevice btDevice;

    private OutputStream outputStream;

    public BTHandler() { }

    //opens the initial connection to the bluetooth chip
    public void startConnection() {
        BluetoothDevice bt = this.getDevice();
        try {
            btSocket = bt.createRfcommSocketToServiceRecord(SPP_UUID);
            btSocket.connect();
            outputStream = btSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //breaks the connection to the bluetooth chip
    public void stopConnection() {
        try {
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //sends bytes to the ardunio to process
    //** bytes are sent from a string, although the arduino can read it as a the original string
    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDevice(BluetoothDevice device) {
        btDevice = device;
    }
    private BluetoothDevice getDevice() {
        return btDevice;
    }
}
