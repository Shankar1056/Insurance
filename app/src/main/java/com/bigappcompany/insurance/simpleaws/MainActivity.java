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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bigappcompany.insurance.R;

import java.net.URISyntaxException;

public class MainActivity extends Activity {

	private static final int FILE_SELECT_CODE = 0;
	
	Button select;
	Button interrupt;
	ProgressBar progress;
	TextView status;
	public static final String TAG = "MainActivity.this";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_aws);
		
		select = (Button) findViewById(R.id.btn_select);
		interrupt = (Button) findViewById(R.id.btn_interrupt);
		progress = (ProgressBar) findViewById(R.id.progress);
		status = (TextView) findViewById(R.id.status);
		
		select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// start file chooser
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("*/*");
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(
						Intent.createChooser(intent, "Select a file to upload"),
						FILE_SELECT_CODE);
			}
		});
		
		interrupt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// interrupt any active upload
				Intent intent = new Intent(UploadService.UPLOAD_CANCELLED_ACTION);
				sendBroadcast(intent);
			}
		});
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		IntentFilter f = new IntentFilter();
		f.addAction(UploadService.UPLOAD_STATE_CHANGED_ACTION);
		registerReceiver(uploadStateReceiver, f);
	}

	@Override
	protected void onStop() {
		unregisterReceiver(uploadStateReceiver);
		super.onStop();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FILE_SELECT_CODE) {
			if (resultCode == RESULT_OK) {  
                // get path of selected file 
                Uri uri = data.getData();
				String path = null;
				try {
					path = getPathFromContentUri(uri);
				} catch (URISyntaxException e) {
					Log.e(TAG, e.getMessage(), e);
				}
				Log.d("S3", "uri=" + uri.toString());
                Log.d("S3", "path=" + path);
                // initiate the upload
                Intent intent = new Intent(this, UploadService.class);
                intent.putExtra(UploadService.ARG_FILE_PATH, path);
                startService(intent);
            }
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/*private String getPathFromContentUri(Uri uri) {
		String path = uri.getPath();
		if (uri.toString().startsWith("content://")) {
			String[] projection = { MediaStore.MediaColumns.DATA };
			ContentResolver cr = getApplicationContext().getContentResolver();
			Cursor cursor = cr.query(uri, projection, null, null, null);
			if (cursor != null) {
				try {
					if (cursor.moveToFirst()) {
						path = cursor.getString(0);
					}
				} finally {
					cursor.close();
				}
			}

		}
		return path;
	}*/
	@SuppressLint("NewApi")
	private String getPathFromContentUri(Uri uri) throws URISyntaxException {
		final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
		String selection = null;
		String[] selectionArgs = null;
		// Uri is different in versions after KITKAT (Android 4.4), we need to
		// deal with different Uris.
		if (needToCheckUri && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				return Environment.getExternalStorageDirectory() + "/" + split[1];
			} else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				uri = ContentUris.withAppendedId(
				    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
			} else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				if ("image".equals(type)) {
					uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				selection = "_id=?";
				selectionArgs = new String[] {
				    split[1]
				};
			}
		}
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = {
			    MediaStore.Images.Media.DATA
			};
			Cursor cursor = null;
			try {
				cursor = getContentResolver()
				    .query(uri, projection, selection, selectionArgs, null);
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}
	
	
	private BroadcastReceiver uploadStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	Bundle b = intent.getExtras();
        	status.setText(b.getString(UploadService.MSG_EXTRA));
        	int percent = b.getInt(UploadService.PERCENT_EXTRA);
        	progress.setIndeterminate(percent < 0);
        	progress.setProgress(percent);
        }
    };
	
	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}
	
	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}
	
	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

}
