package com.watson.saralink;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    private static final String LINK_CFG_FNAME = "LinkConfig.json";
    public static final String KEY_IS_FOREGROUND_SERVICE_RUNNING = "isForegroundServiceRunning";

    private static Gson gson = new Gson();

    private static boolean isForegroundServiceRunning = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("sarababy");

        LinkConfig linkConfig = loadLinkConfig();
        //LinkConfig linkConfig = new LinkConfig();

        TextInputEditText ipEditText = findViewById(R.id.sarababy_ip_input);
        ipEditText.setText(linkConfig.getAnnaIp());

        TextInputEditText portEditText = findViewById(R.id.sarababy_port_input);
        portEditText.setText(String.valueOf(linkConfig.getAnnaPort()));


        Button startServiceButton = findViewById(R.id.start_foreground_service_button);
        startServiceButton.setEnabled(!isForegroundServiceRunning);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinkConfig linkConfig = new LinkConfig();
                TextInputEditText ipEditText = findViewById(R.id.sarababy_ip_input);
                linkConfig.setAnnaIp(ipEditText.getText().toString());
                TextInputEditText portEditText = findViewById(R.id.sarababy_port_input);
                linkConfig.setAnnaPort(Integer.valueOf(portEditText.getText().toString()));
                saveLinkConfig(linkConfig);

                LinkConfigManager.getInstance().setLinkConfig(linkConfig);

                Intent intent = new Intent(MainActivity.this, ForegroundService.class);
                intent.setAction(ForegroundService.ACTION_START_FOREGROUND_SERVICE);
                startService(intent);
                isForegroundServiceRunning = true;
                finish();
            }
        });

        Button stopServiceButton = (Button)findViewById(R.id.stop_foreground_service_button);
        stopServiceButton.setEnabled(isForegroundServiceRunning);
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForegroundService.class);
                intent.setAction(ForegroundService.ACTION_STOP_FOREGROUND_SERVICE);
                startService(intent);
                isForegroundServiceRunning = false;
                finish();
            }
        });
    }

    LinkConfig loadLinkConfig() {
        try {
            FileInputStream fin = openFileInput(LINK_CFG_FNAME);

            InputStreamReader reader= new InputStreamReader(fin);

            StringBuilder sb = new StringBuilder();
            char[] buf = new char[1024];
            int n = reader.read(buf, 0, 1024);
            while(-1 != n) {
                sb.append(String.copyValueOf(buf, 0, n));
                n = reader.read(buf, 0, 1024);
            }
            String json = sb.toString();
            LOGGER.info("------load json:{}", json);
            return gson.fromJson(sb.toString(), LinkConfig.class);
        } catch (FileNotFoundException e) {
            LOGGER.info("config file not exist.");
            return new LinkConfig();
        } catch (Exception e) {
            ExceptionUtils.throwException(e);
        }
        throw new RuntimeException("should not reach here.");
    }

    void saveLinkConfig(LinkConfig linkConfig) {
        try {
            FileOutputStream fout = openFileOutput(LINK_CFG_FNAME, MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fout);
            String json = gson.toJson(linkConfig);
            LOGGER.info("------save json:{}", json);
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            ExceptionUtils.throwException(e);
        }
    }
}
