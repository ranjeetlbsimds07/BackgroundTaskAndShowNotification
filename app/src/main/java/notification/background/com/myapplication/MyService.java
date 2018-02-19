package notification.background.com.myapplication;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    public static String hitApiUrl ="http://physician.ehiconnect.com/notifboard/?UserId=45&action=notifications&PartnerId=140001";

    //public static final int notify = 300000;  //interval between two services(Here Service run every 5 Minute)
    public static final int notify = 10000;  //interval between two services(Here Service run every 10 sec)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast
                    Toast.makeText(MyService.this, "Service is running", Toast.LENGTH_SHORT).show();
                    DownloadWebPageTask task = new DownloadWebPageTask();
                    task.execute();
                }
            });
        }
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String jsonResponse = serviceResponse();
            return jsonResponse;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);

        }

        @Override
        protected void onPostExecute(String reponse) {
            super.onPostExecute(reponse);
            Log.d("reponse==",reponse.toString());
            Intent intent = new Intent();
            intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
            sendBroadcast(intent);
        }
    }

    private String serviceResponse() {

        URL url;
        String response = "";
        int responseCode = -1;
        try {
            url = new URL(hitApiUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(45000);
            conn.setConnectTimeout(45000);
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            responseCode = conn.getResponseCode();
            if (conn.getInputStream() != null) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

}