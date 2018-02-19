package notification.background.com.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

/**
 * Created by Guest User on 2/18/2018.
 */

public class NotificatonBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Notification(context, "Wifi Connection On");
    }

    private void Notification(Context mContext, String message) {
        NotificationManager mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        Intent intent = new Intent(mContext, MainActivity.class);
        builder.setContentTitle("Hello test app");
        builder.setContentText(message);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_ONE_SHOT);
        builder = setNotificationsDefault(builder, message, contentIntent);
        mNotifyManager.notify(1, builder.build());
    }

    private NotificationCompat.Builder setNotificationsDefault(NotificationCompat.Builder builder, String message, PendingIntent pendingIntent) {
        builder.setTicker(message);

        //set small icon as per android version because in LOLLIPOP
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder.setSmallIcon(R.drawable.abcd);
        } else{
            builder.setSmallIcon(R.drawable.abcd);
        }
        //builder.setLargeIcon(icon);
        builder.setContentIntent(pendingIntent);
        builder.setLights(Color.BLUE, 500, 500);

        // wait before 1st vibration,ms for
        long[] pattern = {100, 500};
        builder.setVibrate(pattern);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);
        return builder;
    }
}
