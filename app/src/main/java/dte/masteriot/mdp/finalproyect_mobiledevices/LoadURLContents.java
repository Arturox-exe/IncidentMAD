package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoadURLContents implements Runnable {

    public static InputStream is;
    Handler creator;
    private String string_URL;

    public LoadURLContents(Handler handler,  String strURL) {
        creator = handler;
        string_URL = strURL;
    }

    @Override
    public void run() {
        Message msg = creator.obtainMessage();
        Bundle msg_data = msg.getData();

        String response = "";
        HttpURLConnection urlConnection;

        try {
            URL url = new URL(string_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            is = urlConnection.getInputStream();

            urlConnection.disconnect();
        } catch (Exception e) {
            response = e.toString();
        }

        if ("".equals(response) == false) {
            msg_data.putString("text", response);
        }
        msg.sendToTarget();
    }
}