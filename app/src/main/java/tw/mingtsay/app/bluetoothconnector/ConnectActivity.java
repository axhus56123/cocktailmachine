package tw.mingtsay.app.bluetoothconnector;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ConnectActivity extends AppCompatActivity {
    //private final UUID uuid = UUID.fromString("8c4102d5-f0f9-4958-806e-7ba5fd54ce7c");
    private final UUID serialPortUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private String name, address;

    private Button buttonConnect, buttonDisconnect, buttonSend;
    private EditText textInput;
    private TextView textContent;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    private Thread reader = new Thread(new Runnable() {
        @Override
        public void run() {
            readerStop = false;
            while (!readerStop) {
                read();

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    private boolean readerStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        final String deviceName = getIntent().getStringExtra("DeviceName");
        final String deviceAddress = getIntent().getStringExtra("DeviceAddress");

        name = deviceName != null ? deviceName : "裝置名稱未顯示";
        address = deviceAddress;

        setTitle(String.format("%s (%s)", address, name));

        buttonConnect = findViewById(R.id.buttonConnect);
        buttonDisconnect = findViewById(R.id.buttonDisconnect);
        buttonSend = findViewById(R.id.buttonSend);
        textInput = findViewById(R.id.textInput);
        textContent = findViewById(R.id.textContent);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect();
            }
        });

        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnect();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

        textInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                send();
                return false;
            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        readerStop = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        reader.start();
    }

    private void connect() {
        final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

        try {
            socket = device.createRfcommSocketToServiceRecord(serialPortUUID);
            socket.connect();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        if (socket == null) return;

        try {
            socket.close();
            socket = null;
            inputStream = null;
            outputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send() {
        if (outputStream == null) return;

        try {
            outputStream.write(textInput.getText().toString().getBytes());
            outputStream.flush();

            textInput.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read() {
        if (inputStream == null) return;

        try {
            if (inputStream.available() <= 0) return;
            byte[] buffer = new byte[256];
            textContent.append(new String(buffer, 0, inputStream.read(buffer)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
