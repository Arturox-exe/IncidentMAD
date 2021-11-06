package dte.masteriot.mdp.finalproyect_mobiledevices;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadURLContents implements Runnable {

    Handler creator;
    private String string_URL;

    public LoadURLContents(Handler handler, String strURL) {
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
             InputStream is = urlConnection.getInputStream();

            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();
            while (line != null) {
                response += line + "\n";
                line = in.readLine();
            }

            urlConnection.disconnect();
        } catch (Exception e) {
            response = e.toString();
        }

        if ("".equals(response) == false) {
             msg_data.putString("content", response);
        }
        msg.sendToTarget();
    }
}