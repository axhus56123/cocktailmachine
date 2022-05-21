package tw.mingtsay.app.bluetoothconnector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private final Set<BluetoothDevice> discoveredDevices = new HashSet<>();

    private RecyclerViewAdapter recyclerViewAdapter;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) { //發現遠程設備，接收每台發現的設備的相關信息。
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device == null) return;

                Log.d("onReceive", device.getName() + ":" + device.getAddress());

                discoveredDevices.add(device);
                updateList();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) { //本地藍牙適配器已完成設備發現過程
                stopDiscovery();
            }
        }
    };

    private Button buttonDiscovery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());


        recyclerViewAdapter = new RecyclerViewAdapter(); //建立藍芽動態清單

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(dividerItemDecoration);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //查詢裝置是否支援藍芽
        if (bluetoothAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("本裝置不支援藍芽功能")
                    .setCancelable(false)
                    .setMessage("本裝置不支援藍芽功能，程式即將結束。")
                    .setNeutralButton("結束", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .show();
        }

        buttonDiscovery = findViewById(R.id.buttonDiscovery);
        buttonDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.ACCESS_COARSE_LOCATION")) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 0);
                    return;
                }

                if (!bluetoothAdapter.isEnabled()) {
                    Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(intentBluetoothEnable);
                    return;
                }

                discoverDevices();
            }
        });

        final IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(broadcastReceiver, filter);

        updateList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDiscovery();
    }

    private void startDiscovery() {
        bluetoothAdapter.startDiscovery();
        buttonDiscovery.setText("正在更新…");
    }

    private void stopDiscovery() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        buttonDiscovery.setText("更新已儲存的藍芽裝置");
    }

    private void discoverDevices() {
        stopDiscovery();

        discoveredDevices.clear();
        updateList();

        startDiscovery();
    }

    private void updateList() {
        recyclerViewAdapter.notifyDataSetChanged();
    } //更新藍芽清單

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView textName, textAddress;
        private BluetoothDevice device;
        private boolean isPaired;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            textName = itemView.findViewById(R.id.textName);
            textAddress = itemView.findViewById(R.id.textAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopDiscovery();

                    Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
                    intent.putExtra("DeviceName", device.getName());
                    intent.putExtra("DeviceAddress", device.getAddress());

                    startActivity(intent);
                }
            });
        }

        void loadDevice(@NonNull BluetoothDevice device, boolean isPaired) { //載入已儲存到的藍芽裝置
            this.device = device;
            this.isPaired = isPaired;

            String name = this.device.getName();
            if (name == null) name = "裝置名稱未顯示";

            icon.setImageResource(this.isPaired ? R.drawable.ic_bluetooth_black_24dp : R.drawable.ic_bluetooth_searching_black_24dp);
            textName.setText(name);
            textAddress.setText(this.device.getAddress());
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_device_item, parent, false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (position < pairedDevices.size()) {
                holder.loadDevice(pairedDevices.toArray(new BluetoothDevice[0])[position], true);
            } else {
                holder.loadDevice(discoveredDevices.toArray(new BluetoothDevice[0])[position - pairedDevices.size()], false);
            }
        }

        @Override
        public int getItemCount() {
            return bluetoothAdapter.getBondedDevices().size() + discoveredDevices.size();
        }
    }

}
