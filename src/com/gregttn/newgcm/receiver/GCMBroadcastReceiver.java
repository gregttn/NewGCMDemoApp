package com.gregttn.newgcm.receiver;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.gregttn.newgcm.DemoGCMActivity;
import com.gregttn.newgcm.model.NotificationInfo;
import com.gregttn.newgcm.utils.NotificationDispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GCMBroadcastReceiver extends BroadcastReceiver{
	private final static String DEFAULT_CONTENT_ID = "message";
	private final static String TAG = GCMBroadcastReceiver.class.getName();
	private NotificationDispatcher notificationDispatcher;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		notificationDispatcher = initNotificationDispatcher(context);
		
		String messageType = gcm.getMessageType(intent);
		if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE .equals(messageType)) {
			dispatchNotification(intent);
		} else {
			Log.w(TAG, "Unrecognized message type received: " + messageType);
		}
	}
	
	private void dispatchNotification(Intent intent) {
		String message = intent.getExtras().getString(DEFAULT_CONTENT_ID);
		NotificationInfo notificationInfo = new NotificationInfo(message);
		
		notificationDispatcher.dispatchNotification(notificationInfo, DemoGCMActivity.class);
		Log.d(TAG, "Dispatched notification: " + notificationInfo);
	}
	
	private NotificationDispatcher initNotificationDispatcher(Context context) {
		if (notificationDispatcher == null) {
			notificationDispatcher = new NotificationDispatcher(context);
		}
		
		return notificationDispatcher;
	}
}
