package com.example.myapplication4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TEST_IP = "255.255.255.255";
    private static final String MAC = "90:e6:ba:74:a5:5b";

    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidgetConnections();
    }

    private void setWidgetConnections() {
        btnSend = (Button) findViewById(R.id.button);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WakeOnLanTask(TEST_IP, MAC, 3131).execute();
            }
        });
    }


    class WakeOnLanTask extends AsyncTask<Void, Void, Integer> {

        private String IP;
        private String MAC;
        private int port;

        public WakeOnLanTask(String ip, String mac, int port) {
            this.IP = ip;
            this.MAC = mac;
            this.port = port;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... args) {
            return WakeOnLAN.sendPacket(IP, MAC, port);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }

    @Override
    public void onClick(View view) {
    }

}