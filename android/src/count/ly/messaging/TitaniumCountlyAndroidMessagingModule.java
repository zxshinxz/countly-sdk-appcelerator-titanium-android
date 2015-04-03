/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package count.ly.messaging;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Set;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiProperties;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import android.app.Activity;
import android.os.Bundle;

@Kroll.module(name="TitaniumCountlyAndroidMessaging", id="count.ly.messaging")
public class TitaniumCountlyAndroidMessagingModule extends KrollModule
{

	// Standard Debugging variables
	private static final String LCAT = "CountlyMessagingModule";
	private static final boolean DBG = TiConfig.LOGD;
	
	// set vars
	public static Message message;
	
	private static WeakReference<TitaniumCountlyAndroidMessagingModule> lastInstance;
	
	// You can define constants with @Kroll.constant, for example:
	// @Kroll.constant public static final String EXTERNAL_NAME = value;

	public TitaniumCountlyAndroidMessagingModule()
	{
		super();
		lastInstance = new WeakReference(this);
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
		Log.d(LCAT, "inside onAppCreate");
		// put module init code that needs to run when the application is created
	}

	// Methods	
		@Kroll.method
		public void enableDebug() {
			Log.d(LCAT, "Enable Debug called");
			
			Countly.sharedInstance().setLoggingEnabled(true);
		}	
	
		@Kroll.method
		public void start(String apiKey,String url) {
			Log.d(LCAT, "Start called");
			
			Countly.sharedInstance().init(TiApplication.getAppCurrentActivity(),
					url, apiKey);
			
			Countly.sharedInstance().onStart();
		}	
		
		@Kroll.method
		public void startMessaging(String apiKey,String url,String projectID) {
			
			Log.d(LCAT, "Start Messaging called");
			
			Countly.sharedInstance()
			.init(TiApplication.getAppCurrentActivity(),url, apiKey, null, DeviceId.Type.ADVERTISING_ID)
			.initMessaging(TiApplication.getAppCurrentActivity(), null, projectID, Countly.CountlyMessagingMode.PRODUCTION);
			
			Countly.sharedInstance().onStart();
		}
		
		@Kroll.method
		public void startMessagingTest(String apiKey,String url,String projectID) {
			
			Log.d(LCAT, "Start Messaging Test called");
			
			Countly.sharedInstance()
			.init(TiApplication.getAppCurrentActivity(),url, apiKey, null, DeviceId.Type.ADVERTISING_ID)
			.initMessaging(TiApplication.getAppCurrentActivity(), null, projectID, Countly.CountlyMessagingMode.TEST);
			
			Countly.sharedInstance().onStart();
		}
		
		// Class to processCallBack on Module
		public static void processPushCallBack() {
			
			Log.d(LCAT, "processPushCallBack");
			
			TitaniumCountlyAndroidMessagingModule module = getModule();
			if (module == null) {
				return;
			}
			module.sendNotification(); 
			
		}
		
		@Kroll.method
		public void sendNotification() {
			
			Log.d(LCAT, "Send Notification");
			// Check if Module has Listeners 
			if (hasListeners("receivePush")) {
				// Log Listeners Found
				Log.d(LCAT, "Has Listener: receivePush");
				
				// Set Message 
				Message message = TitaniumCountlyAndroidMessagingModule.message;			
		
				// Create HashMap of Notification info and add data
				HashMap pushMessage = new HashMap();
				pushMessage.put("id", message.getId());
				pushMessage.put("message", message.getNotificationMessage());
				
				if (message.hasLink()){
					pushMessage.put("type", "hasLink");
					pushMessage.put("link", message.getLink());
				}else if (message.hasReview()){
					pushMessage.put("type", "hasReview");
				}else if (message.hasMessage()) {
					pushMessage.put("type", "hasMessage");
				}
				
				if (message.hasSoundUri()){
					pushMessage.put("sound", message.getSoundUri());
				}
				
				pushMessage.put("data", bundleToHashMap(message.getData()));			
				
				Log.d(LCAT, "pushMessage" + pushMessage);
		        // fireEvent pushCallBack with payload evt
		        fireEvent("receivePush", pushMessage); 
		       	     			
			}else{
				// Log No Listener
				Log.d(LCAT, "No Listener receivePush found");
			}
		}	
		
		@Kroll.method
		public void recordPushAction(String messageId) {
			
			Log.d(LCAT, "RecordPushAction");
			CountlyMessaging.recordMessageAction(messageId);
			
		}	
		
		@Kroll.method
		public void setLocation(String latitudeString, String longitudeString) {
			
			Log.d(LCAT, "setLocation");
			
			double latitude = Double.parseDouble(latitudeString);
			double longitude = Double.parseDouble(longitudeString);
			
			Countly.sharedInstance().setLocation(latitude, longitude);
			
		}	
		
		@Kroll.method
		public void stopCount() {
			Log.d(LCAT, "Stop Count called");
			Countly.sharedInstance().onStop();
		}

		
		@Kroll.method
		public void event(KrollDict args) {
			Log.d(LCAT, "Event Send called");
			
			Object keyObject = args.get("name");
			Object countObject = args.get("count");
			Object sumObject = args.get("sum");
			Object segmentationObject = args.get("segmentation");
					
			// set key and count
			String key = keyObject.toString();
			int count = (Integer) countObject;
						
			// START IF - has segmentation
			if(segmentationObject != null){
				
				// set segmentation
	        	HashMap<String, String> segmentation = (HashMap<String, String>) segmentationObject;
	        	
				// START IF - has sum
		        if(sumObject != null){
		        	
		        	// set sum
		        	double sum = Double.parseDouble(sumObject.toString());
		        	
		        	// recordEvent with key, segmentation, count and sum
		        	Countly.sharedInstance().recordEvent(key, segmentation, count, sum);

		        }else{
		        	
		        	// recordEvent with key, segmentation, count - no sum
		        	Countly.sharedInstance().recordEvent(key, segmentation, count);

		        }
		        // END IF - has sum
		        
		    }else if(sumObject != null){ // ELSE IF - no segmentation but has sum 
		    	
		    	// set sum
	        	double sum = Double.parseDouble(sumObject.toString());
	        	
	        	// recordEvent with key, count and sum - no segmentation
		    	Countly.sharedInstance().recordEvent(key, count, sum);

		    }else{ // ELSE - no segmentation or sum
		    	
		    	// recordEvent with key and count - no sum and no segmentation
		    	Countly.sharedInstance().recordEvent(key, count);

		    }
			// END IF - check for segmentation data

		}
		
		@Kroll.method
		public void userData(KrollDict args) {
			Log.d(LCAT, "UserData Send called");
			
			Object userDataObject = args.get("userData");
			Object customUserDataObject = args.get("customUserData");
			
			// set userData
	    	HashMap<String, String> userData = (HashMap<String, String>) userDataObject;
	    	
			// START IF - has customData
			if(customUserDataObject != null){
				
				// set customUserData
	        	HashMap<String, String> customUserData = (HashMap<String, String>) customUserDataObject;
	        	
				Countly.sharedInstance().setUserData(userData, customUserData);			
				
			}else{	// ELSE - no customData
				
				Countly.sharedInstance().setUserData(userData);
				
			}			
			// END IF - has customData
			
		}	
		
		// Class to find instance of Module
		public static TitaniumCountlyAndroidMessagingModule getModule() {
			TitaniumCountlyAndroidMessagingModule module = null;
				TiApplication appContext = TiApplication.getInstance();
				Activity activity = appContext.getCurrentActivity();
				Log.d(LCAT, "Activity:" + activity);
				Log.d(LCAT, "lastinstance" + lastInstance);
				if ((activity != null) && (lastInstance != null)) {
					module = (TitaniumCountlyAndroidMessagingModule)lastInstance.get();
				}
				return module;
		}
		
		private static String printBundle(Bundle bundle) {
			StringBuilder sb = new StringBuilder();
			for (String key : bundle.keySet()) {
					sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
			return sb.toString();
		}
		
		private static HashMap<String, String> bundleToHashMap(Bundle bundle){
			HashMap<String, String> hash = new HashMap<String, String>();
			Set<String> keys = bundle.keySet();
			for (String key : keys) {		    	    	
			    	hash.put(key, bundle.get(key).toString());
			}
			return hash;
		}
}
