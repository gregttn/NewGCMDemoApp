package com.gregttn.newgcm.service;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class GCMService {
	private static final String GCM_SHARED_PREFS = "gcmprefs";
	private static final String REG_ID = "registrationId";
	private static final String DEFAULT_REG_ID = "none";
	private static final String SENDER_ID = "YOUR SENDER ID";
	private static final String TAG = GCMService.class.getName();
	
	private final Context context;
	private String registrationId;
	private GoogleCloudMessaging gcm;
	
	public GCMService(Context context) {
		this.context = context;
		this.gcm = GoogleCloudMessaging.getInstance(context);
	}
	
	public boolean isDeviceRegistered() {
		SharedPreferences preferences = context.getSharedPreferences(GCM_SHARED_PREFS, Context.MODE_PRIVATE);
		String registrationId = preferences.getString(REG_ID, DEFAULT_REG_ID);
		
		return !DEFAULT_REG_ID.equals(registrationId);
	}
	
	public void registerDevice(final AsyncTask<Void, Void, Boolean> registerWithTheServer) {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String regId = "";
				try {
					regId = gcm.register(SENDER_ID);
					
					setRegistrationId(regId);
					saveRegistrationId(regId);
					
				} catch (IOException e) {
					Log.wtf(TAG, e.getMessage());
				} 
				
				return regId;
			}
			
			@Override
			protected void onPostExecute(String regId) {
				Log.i(TAG, "Registration id: " + regId);
				registerWithTheServer.execute();
			}
		}.execute();
	}
	
	public String getRegistrationId() {
		return registrationId;
	}
	
	private void setRegistrationId(String regId) {
		this.registrationId = regId;
	}
	
	private void saveRegistrationId(String regId) {
		SharedPreferences preferences = context.getSharedPreferences(GCM_SHARED_PREFS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(REG_ID, regId);
		editor.commit();
	}
}
