package tutorials.com.hci;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by khanh-bk on 11/19/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String hour = loadHour();
        String setRing = MainActivity.hour + ":" + MainActivity.minus;
        if(hour.equals(setRing)){
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.ic_note);
            builder.setContentTitle("Có lịch nhắc nhở");
            builder.setContentText(MainActivity.note);
            Intent resultIntent = new Intent(context, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, builder.build());
        }
    }
    private String loadHour(){
        Date date = new Date();
        String strDateFormat24 = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat24);
        String currTime = sdf.format(date);
        return currTime;
    }
}
