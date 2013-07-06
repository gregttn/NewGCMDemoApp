package com.gregttn.newgcm;

import com.gregttn.newgcm.R;
import com.gregttn.newgcm.service.GCMService;
import com.gregttn.newgcm.service.ServerGateway;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.TextView;

public class DemoGCMActivity extends Activity {
	private static final String TAG = DemoGCMActivity.class.getName();
	private static final String SERVER_REGISTRATION_ENDPOINT = "SERVER REGISTRATION ENDPOINT ADDRESS";
	
	private ServerGateway serverGateway;
	private GCMService gcmService;
	private TextView messagesTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo_gcm);
		
		messagesTextView = (TextView) findViewById(R.id.messages);
		
		serverGateway = new ServerGateway(SERVER_REGISTRATION_ENDPOINT);
		gcmService = new GCMService(getApplicationContext());
		
		registerDevice();
	}
	
	private void registerDevice() {
		AsyncTask<Void, Void, Boolean> registerWithTheServer = new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				String deviceId = Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID);
				String registrationId = gcmService.getRegistrationId();
				
				return serverGateway.registerDevice(deviceId, registrationId);
			}
			
			@Override
			protected void onPostExecute(Boolean registered) {
				if (registered) {
					String message = "RegId " + gcmService.getRegistrationId();
					messagesTextView.setText(message);
				} else {
					Log.w(TAG, "App failed to register device with the server!");
				}
			}
		};
		
		gcmService.registerDevice(registerWithTheServer);
	}
}
