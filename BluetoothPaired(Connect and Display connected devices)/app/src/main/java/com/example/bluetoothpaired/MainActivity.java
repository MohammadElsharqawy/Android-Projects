package com.example.bluetoothpaired;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button getPairedBtn;
    TextView pairedDevicesTextView;

    String pairedDevicesText = "";

    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;


    public final int REQUEST_ENABLE_BT = 15;
    public final int REQUEST_PERMISSIONS = 10;


    private ArrayAdapter<String> discoveredDevicesAdapter;
    private List<String> discoveredDevicesList;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_PERMISSIONS);
            }
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceAddress = device.getAddress();
                String deviceInfo = deviceName + " (" + deviceAddress + ")";
                discoveredDevicesList.add(deviceInfo);
                discoveredDevicesAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPairedBtn = (Button) findViewById(R.id.btn);
        pairedDevicesTextView = (TextView) findViewById(R.id.paired_devices);
        ListView mListViewBluetoothDevices = findViewById(R.id.list_bluetooth_devices);




        discoveredDevicesList = new ArrayList<>();
        discoveredDevicesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, discoveredDevicesList);
        mListViewBluetoothDevices.setAdapter(discoveredDevicesAdapter);

       /* //check Runtime-permission.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_PERMISSIONS);
        }
        */
        //get bluetooth adapter
        bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();


        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available!", Toast.LENGTH_LONG).show();
            finish(); // close the application
        }
        /*
        //enable bluetooth on the device

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBTIntent = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE); //implicit intent
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
            //code (REQUEST_ENABLE_BT) da 3lshan lma t return mra tanya 23rf meen fyhom elly rg3 tany.
        }
*/

        getPairedBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        checkAndScan();
                        getPairedDevices();


                    }
                }
        );

    }

    public void onRequestPermissionResult(int requestCode, String permission[], int[] results) {
        super.onRequestPermissionsResult(requestCode, permission, results);
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                    Discovery();
                } else {
                    Toast.makeText(this, "give me the permission idiot why are you here then!!!!", Toast.LENGTH_LONG).show();
                    finish();
                    System.exit(0);
                }
                return;
            }
        }
    }


    //fires when the user enables bluetooth on the device
    @Override
    protected void onActivityResult(int request_code, int result_code, @Nullable Intent data) {
        super.onActivityResult(request_code, result_code, data);


        switch (request_code) {
            case REQUEST_ENABLE_BT:
                if (result_code == RESULT_CANCELED) {
                    Toast.makeText(this, "Please enable bluetooth on your device", Toast.LENGTH_LONG).show();
                }
                if (result_code == RESULT_OK) {
                    discoverDevices();
                    break;
                }
        }
    }

        protected void getPairedDevices () {
            pairedDevicesText = "PairedDevices\n\n";
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_PERMISSIONS);
            }
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice Device : pairedDevices) {
                    String deviceName = Device.getName();
                    String DeviceMAC = Device.getAddress();
                    pairedDevicesText += "Device Name: " + deviceName + "\nDevice MAC Address: " + DeviceMAC + "\n\n";
                }
            } else {
                pairedDevicesText += "no devices were found!!!!!!\n";
            }
            pairedDevicesTextView.setText(pairedDevicesText);
        }


        @Override
        protected void onDestroy () {
            super.onDestroy();
            unregisterReceiver(receiver);
        }

        private void checkAndScan() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS);
            } else {
                Discovery();
            }
        }

        private void Discovery() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Location permission not granted", Toast.LENGTH_SHORT).show();
                return;
            }
            discoveredDevicesList.clear();
            discoveredDevicesAdapter.notifyDataSetChanged();

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver, filter);

            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                discoverDevices();
            }
        }

        private void discoverDevices () {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Location permission not granted", Toast.LENGTH_SHORT).show();
                return;
            }
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            bluetoothAdapter.startDiscovery();
        }

}

