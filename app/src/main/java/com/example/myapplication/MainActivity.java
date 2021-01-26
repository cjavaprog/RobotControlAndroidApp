package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static OutputStream outputStream;
    private ListView listView;
    private final ArrayList<String> deviceList = new ArrayList<>();
    private String addr = "";
    public static BluetoothSocket btSock = null;
    public static BluetoothDevice comp = null;
    public static BluetoothAdapter btAd = BluetoothAdapter.getDefaultAdapter();


    // display paired devices
    public void connect(BluetoothAdapter btAd) {
        if(BluetoothAdapter.checkBluetoothAddress(addr)) {
            comp = btAd.getRemoteDevice(addr);
            try {
                btSock = comp.createInsecureRfcommSocketToServiceRecord(mUUID);
                btSock.connect();
                outputStream = btSock.getOutputStream();
                Intent intent = new Intent(MainActivity.this, SteeringPanel.class);
                startActivity(intent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void turnBluetoothIfDisabled() throws InterruptedException {
        if(!btAd.isEnabled()) {
            Toast.makeText(MainActivity.this, "Turning bluetooth on", Toast.LENGTH_SHORT).show();
            btAd.enable();
            // wait for turn Bluetooth on
            TimeUnit.SECONDS.sleep(1);
        }
    }

    // main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // turn bluetooth on, if off
        try {
            turnBluetoothIfDisabled();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(btAd.isEnabled());

        if(btAd.isEnabled()) {
            listView = findViewById(R.id.devices);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceList);

            listView.setAdapter(arrayAdapter);
            for(BluetoothDevice bd : btAd.getBondedDevices()){
                String s = "Name: "+bd.getName();
                s += "\nMAC: "+bd.toString();
                deviceList.add(s);
            }

            // after click on device on list it try to connect to it
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listView.getChildAt(position).setBackgroundColor(getColor(R.color.pressedListViewColor));
                    addr = deviceList.get(position).substring(deviceList.get(position).indexOf("MAC: ") + 5);
                    connect(btAd);
                    listView.getChildAt(position).setBackgroundColor(getColor(R.color.mainBackground));
                }
            });
        }


        final Button disconn_button = findViewById(R.id.exit_button);

        disconn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(1);
            }
        });
    }

}