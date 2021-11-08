package dte.masteriot.mdp.finalproyect_mobiledevices.mqtt;


import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import dte.masteriot.mdp.finalproyect_mobiledevices.R;

public class MqttClient extends AppCompatActivity {
    final String serverUri = "tcp://2.137.213.175";
    final String subscriptionTopic = "incidents/madrid";
    final String publishTopic = "incidents/madrid";
    private String currentDateTimeString = "";
    private String new_message = "";
    MqttAndroidClient mqttAndroidClient;
    String clientId = "Client1";
    private HistoryAdapter mAdapter;

    String user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        RecyclerView mRecyclerView = findViewById(R.id.history_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HistoryAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);

        clientId = clientId + System.currentTimeMillis();

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    subscribeToTopic();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                addToHistory("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                addToHistory(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        //mqttConnectOptions.setCleanSession(true);
        /*willPayload = "Client " + clientId + " disconnect";
        mqttConnectOptions.setWill("incidents/madrid", willPayload.getBytes(),1,true);*/

        //addToHistory("Connecting to " + serverUri + "...");
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to connect to: " + serverUri +
                            ". Cause: " + ((exception.getCause() == null)?
                            exception.toString() : exception.getCause()));
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            addToHistory(e.toString());
        }
    }

    private void addToHistory(String mainText) {
        System.out.println("LOG: " + mainText);
        mAdapter.add(mainText);
        Snackbar.make(findViewById(android.R.id.content), mainText, Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast Tsubscribe = Toast.makeText(getApplicationContext(),"Subscribed to: " + subscriptionTopic, Toast.LENGTH_SHORT);
                    Tsubscribe.show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast Tsubscribe = Toast.makeText(getApplicationContext(),"Failed to subscribe", Toast.LENGTH_SHORT);
                    Tsubscribe.show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Toast Tsubscribe = Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT);
            Tsubscribe.show();

        }

    }

    public void onPublishMessage(View view) {
        EditText eMessage = (EditText) findViewById(R.id.eMessage);

        if(!eMessage.getText().toString().isEmpty()) {
            currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            MqttMessage message = new MqttMessage();
            Bundle savedInstanceState = null;
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    user= null;
                } else {
                    user= extras.getString("user");
                }
            } else {
                user= (String) savedInstanceState.getSerializable("user");
            }

            String publish = currentDateTimeString + "\n" + "   " + user + ":   " + eMessage.getText().toString();
            eMessage.setText("");
            message.setPayload(publish.getBytes());
            message.setRetained(false);
            message.setQos(0);
            try {
                mqttAndroidClient.publish(publishTopic, message);

            } catch (Exception e) {
                e.printStackTrace();
                addToHistory(e.toString());
            }

            if (!mqttAndroidClient.isConnected()) {
                addToHistory("Client not connected!");
            }
        }

        else{
            Toast Tmessage = Toast.makeText(getApplicationContext(),"Your message can't be empty", Toast.LENGTH_SHORT);
            Tmessage.show();
        }

    }
}