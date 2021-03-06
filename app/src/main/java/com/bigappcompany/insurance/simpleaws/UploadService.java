/***
 * Copyright (c) 2012 readyState Software Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bigappcompany.insurance.simpleaws;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.bigappcompany.insurance.R;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UploadService extends IntentService {
	
	public static final String ARG_FILE_PATH = "file_path";
	public static final String ARG_FOLDER_NAME = "foldername";
	public static final String FRAGMENT_NAME = "fragmentname";
	public static final String UPLOAD_STATE_CHANGED_ACTION = "com.bigappcompany.insurance.simpleaws.UPLOAD_STATE_CHANGED_ACTION";
	public static final String NOPOLICY_UPLOAD_STATE_CHANGED = "com.bigappcompany.insurance.simpleaws.NOPOLICY_UPLOAD_STATE_CHANGED_ACTION";
	public static final String UPLOAD_CANCELLED_ACTION = "com.bigappcompany.insurance.simpleaws.UPLOAD_CANCELLED_ACTION";
	public static final String S3KEY_EXTRA = "s3key";
	public static final String PERCENT_EXTRA = "percent";
	public static final String MSG_EXTRA = "msg";
	public static final String PATH_LOCATION = "pathlocation";
	public static final String COUNT = "count";
	public static final String FOLDERNAME = "foldrname";
	
	private static final int NOTIFY_ID_UPLOAD = 1337;
	
	private AmazonS3Client s3Client;
	private Uploader uploader;
	
	private NotificationManager nm;
	
	private ArrayList<String> path = new ArrayList<>();
	private ArrayList<String> folderName = new ArrayList<>();
	private int i;
	private String name;
	private Intent intent;
	
	
	public UploadService() {
		super("simpl3r-example-upload");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		s3Client = new AmazonS3Client(
		    new BasicAWSCredentials("accessKey", "SecretKey"));
		s3Client.setEndpoint("endpoint(url)");
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		IntentFilter f = new IntentFilter();
		f.addAction(UPLOAD_CANCELLED_ACTION);
		registerReceiver(uploadCancelReceiver, f);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {

//		String filePath = intent.getStringExtra(ARG_FILE_PATH);
//		String foldername = intent.getStringExtra(ARG_FOLDER_NAME);
		name = intent.getStringExtra(FRAGMENT_NAME);
		path = intent.getStringArrayListExtra(ARG_FILE_PATH);
		folderName = intent.getStringArrayListExtra(ARG_FOLDER_NAME);
		for ( i = 0; i < path.size(); i++) {
			File fileToUpload = new File(path.get(i));
			final String s3ObjectKey = md5(path.get(i)) + ".png";
			String s3BucketName = "smartcovers/"+ folderName.get(i);
			
			//final String msg = "Uploading " + s3ObjectKey + "...";
			final String msg = "["+(i+1)+"/"+path.size() +"] Uploading :-  " + folderName.get(i) + "...";
			
			// create a new uploader for this file
			uploader = new Uploader(this, s3Client, s3BucketName, s3ObjectKey, fileToUpload);
			
			// listen for progress updates and broadcast/notify them appropriately
			uploader.setProgressListener(new Uploader.UploadProgressListener() {
				@Override
				public void progressChanged(ProgressEvent progressEvent,
				                            long bytesUploaded, int percentUploaded) {
					
					Notification notification = buildNotification(msg, percentUploaded);
					nm.notify(NOTIFY_ID_UPLOAD, notification);
					broadcastState(s3ObjectKey, percentUploaded, msg,i+1,folderName.get(i),"");
				}
			});
			
			// broadcast/notify that our upload is starting
			Notification notification = buildNotification(msg, 0);
			nm.notify(NOTIFY_ID_UPLOAD, notification);
			broadcastState(s3ObjectKey, 0, msg,i+1,folderName.get(i),"");
			
			try {
				String s3Location = uploader.start(); // initiate the upload
				broadcastState(s3ObjectKey, -1, "File successfully uploaded to " + s3Location,i+1,
				    folderName.get(i),s3Location);
			} catch (UploadIterruptedException uie) {
				broadcastState(s3ObjectKey, -1, "User interrupted",i+1,folderName.get(i),"");
			} catch (Exception e) {
				e.printStackTrace();
				broadcastState(s3ObjectKey, -1, "Error: " + e.getMessage(),i+1,folderName.get(i),"");
			}
		}
	}
	
	@Override
	public void onDestroy() {
		nm.cancel(NOTIFY_ID_UPLOAD);
		unregisterReceiver(uploadCancelReceiver);
		super.onDestroy();
	}
	
	private void broadcastState(String s3key, int percent, String msg, int count, String foldername, String
	    s3location) {
		
		if (name.equals("policy")) {
			 intent = new Intent(UPLOAD_STATE_CHANGED_ACTION);
		}
		else if (name.equals("nopolicy"))
		{
			intent = new Intent(NOPOLICY_UPLOAD_STATE_CHANGED);
		}
		Bundle b = new Bundle();
		b.putString(S3KEY_EXTRA, s3key);
		b.putInt(PERCENT_EXTRA, percent);
		b.putString(MSG_EXTRA, msg);
		b.putString(PATH_LOCATION, s3location);
		b.putInt(COUNT, count);
		b.putString(FOLDERNAME, foldername);
		intent.putExtras(b);
		sendBroadcast(intent);
	}
	
	private Notification buildNotification(String msg, int progress) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setWhen(System.currentTimeMillis());
		builder.setTicker(msg);
		builder.setContentTitle(getString(R.string.app_name));
		builder.setContentText(msg);
		builder.setSmallIcon(R.mipmap.ic_launcher);
		builder.setOngoing(true);
		builder.setProgress(100, progress, false);
		builder.setAutoCancel(true);
		
		/*Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		builder.setContentIntent(contentIntent);*/
		
		return builder.build();
	}
	
	private BroadcastReceiver uploadCancelReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (uploader != null) {
				uploader.interrupt();
			}
		}
	};
	
	private String md5(String s) {
		try {
			// create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			
			// create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
