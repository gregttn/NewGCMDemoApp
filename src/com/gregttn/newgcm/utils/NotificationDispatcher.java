package com.gregttn.newgcm.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.gregttn.newgcm.R;
import com.gregttn.newgcm.model.NotificationInfo;

public class NotificationDispatcher {
    private static final int NOTIFICATION_ID = 2333;
    private final Context context;

    public NotificationDispatcher(Context context) {
        this.context = context;
    }

    public void dispatchNotification(NotificationInfo notificationInfo, Class<? extends Activity> activityClass) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(notificationInfo.getTitle())
                .setContentText(notificationInfo.getMessage());

        Intent resultIntent = new Intent(context, activityClass);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(activityClass);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }
}
