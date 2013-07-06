package com.gregttn.newgcm;

import com.gregttn.newgcm.R;
import com.gregttn.newgcm.service.GCMService;
import com.gregttn.newgcm.service.ServerGateway;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.widget.TextView;

public class DemoGCMActivity extends Activity {
	private static final String SERVER_REGISTRATION_ENDPOINT = "REGISTRATION SERVER ENDPOINT ADDRESS";
	
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
		
		if(!gcmService.isDeviceRegistered()) {
			registerDevice();
		}
	}
	
	private void registerDevice() {
		Runnable registerWithTheServer = new Runnable() {
			@Override
			public void run() {
				String deviceId = Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID);
				String registrationId = gcmService.getRegistrationId();
				
				serverGateway.registerDevice(deviceId, registrationId);
				
				StringBuilder message = new StringBuilder(R.string.notification_prefix).append("regiId ").append(registrationId);
				messagesTextView.append(message.toString());
			}
		};
		
		gcmService.registerDevice(registerWithTheServer);
	}
}
