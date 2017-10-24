package com.bigappcompany.insurance.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.adapter.ImagesAdapter;
import com.bigappcompany.insurance.model.RetrivedFromAWS;
import com.bigappcompany.insurance.network.Download_web;
import com.bigappcompany.insurance.simpleaws.UploadService;
import com.bigappcompany.insurance.takeandpickimagelib.DefaultCallback;
import com.bigappcompany.insurance.takeandpickimagelib.EasyImage;
import com.bigappcompany.insurance.utility.ClsGeneral;
import com.bigappcompany.insurance.utility.OnTaskCompleted;
import com.bigappcompany.insurance.utility.PrefereneceName;
import com.bigappcompany.insurance.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 14 Oct 2017 at 1:25 PM
 */

public class DetailsPreviousActivityS extends AppCompatActivity implements View.OnClickListener {
	
	private static final String TAG = "DetailsPreviousDetailsS";
	private static final int REQUEST_CODE_PICTURE = 1;
	private final HashMap<String, Bitmap> mImageMap = new HashMap<>();
	int PERMISSION_ALL = 1;
	private Uri tempUri;
	private String tag;
	private static final String PHOTOS_KEY = "easy_image_photos_list";
	public RecyclerView rv_images;
	private ImagesAdapter imagesAdapter;
	private ArrayList<File> photos = new ArrayList<>();
	private ArrayList<String> textname = new ArrayList<>();
	private TextView text;
	private EditText previouspolicynumber, nomineename, nomineerelation, agentnumber;
	private CheckBox cb_policy, cb_rc, cb_address, cb_id;
	private ArrayList<String> path = new ArrayList<>();
	private ArrayList<String> folderName = new ArrayList<>();
	private ArrayList<RetrivedFromAWS> uploadedpath = new ArrayList<>();
	private TextView dialogfoldername, dialoguploadcount, dialogstatus, progresspercent;
	private TextView havepolicytext,  havenopolicytext;
	private ProgressBar dialogprogress;
	private Dialog dialog;
	private Boolean ispolicy;
	private View havepolicyview,havenopolicyview;
	private CardView agentnumbercard;
	
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailspreviousactivitys);
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
		    .setDefaultFontPath("fonts/Celias.otf")
		    .setFontAttrId(R.attr.fontPath)
		    .build()
		);
		Nammu.init(DetailsPreviousActivityS.this);
		initwidget(savedInstanceState);
		permissionCheck();
	}
	
	private void permissionCheck() {
		
		int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			Nammu.askForPermission(DetailsPreviousActivityS.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new
			    PermissionCallback() {
				    @Override
				    public void permissionGranted() {
					    //Nothing, this sample saves to Public gallery so it needs permission
				    }
				    
				    @Override
				    public void permissionRefused() {
					    
				    }
			    });
		}
		
		EasyImage.configuration(DetailsPreviousActivityS.this)
		    .setImagesFolderName("EasyImage sample")
		    .setCopyTakenPhotosToPublicGalleryAppFolder(true)
		    .setCopyPickedImagesToPublicGalleryAppFolder(true)
		    .setAllowMultiplePickInGallery(true);
		
		checkGalleryAppAvailability();
	}
	
	private void initwidget(Bundle savedInstanceState) {
		findViewById(R.id.iv_selected).setOnClickListener(this);
		findViewById(R.id.btnProceedInsurance).setOnClickListener(this);
		findViewById(R.id.havepolicylayout).setOnClickListener(this);
		findViewById(R.id.havenopolicylayout).setOnClickListener(this);
		findViewById(R.id.rvClick).setOnClickListener(this);
		text = (TextView) findViewById(R.id.text);
		havepolicytext = (TextView) findViewById(R.id.havepolicytext);
		havepolicyview = (View) findViewById(R.id.havepolicyview);
		havenopolicytext = (TextView) findViewById(R.id.havenopolicytext);
		havenopolicyview = (View) findViewById(R.id.havenopolicyview);
		agentnumbercard = (CardView)findViewById(R.id.agentnumbercard);
		previouspolicynumber = (EditText) findViewById(R.id.previouspolicynumber);
		nomineename = (EditText) findViewById(R.id.nomineename);
		nomineerelation = (EditText) findViewById(R.id.nomineerelation);
		agentnumber = (EditText) findViewById(R.id.agentnumber);
		rv_images = (RecyclerView) findViewById(R.id.rv_images);
		cb_policy = ((AppCompatCheckBox) findViewById(R.id.cb_policy));
		cb_rc = ((AppCompatCheckBox) findViewById(R.id.cb_rc));
		cb_address = ((AppCompatCheckBox) findViewById(R.id.cb_address));
		cb_id = ((AppCompatCheckBox) findViewById(R.id.cb_id));
		
		if (savedInstanceState != null) {
			photos = (ArrayList<File>) savedInstanceState.getSerializable(PHOTOS_KEY);
		}
		try {
			/*imagesAdapter = new ImagesAdapter(getApplicationContext(), photos, textname, DetailsPreviousActivityS.this);
			rv_images.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false));
			rv_images.setHasFixedSize(true);
			rv_images.setAdapter(imagesAdapter);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ispolicy = true;
		
	}
	
	private void uloadfiletoAws() {
		
		
		Intent intent = new Intent(DetailsPreviousActivityS.this, UploadService.class);
		//intent.putExtra(UploadService.ARG_FILE_PATH, iamgepath);
		//intent.putExtra(UploadService.ARG_FOLDER_NAME, foldrname);
		intent.putStringArrayListExtra(UploadService.ARG_FILE_PATH, path);
		intent.putStringArrayListExtra(UploadService.ARG_FOLDER_NAME, folderName);
		startService(intent);
	}
	
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btnProceedInsurance:
				if (nomineename.getText().toString().trim().equals("")) {
					Utility.displayMessageAlert("Enter Nominee Name", DetailsPreviousActivityS.this);
					return;
				}
				if (nomineerelation.getText().toString().trim().equals("")) {
					Utility.displayMessageAlert("Enter Relation with Nominee", DetailsPreviousActivityS.this);
					return;
				}
				if (ispolicy && photos.size() < 4) {
					Utility.toastDisplay("All documents are mandatory", DetailsPreviousActivityS.this);
					return;
				}
				if (!ispolicy && photos.size() < 3) {
					Utility.toastDisplay("All documents are mandatory", DetailsPreviousActivityS.this);
					return;
				}
				
				try {
					uploadedpath.clear();
					opencustomdialog();
					uloadfiletoAws();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				break;
			
			case R.id.iv_selected:
				if (text.getText().toString().equals(getResources().getString(R.string.youaredone))) {
					Toast.makeText(getApplicationContext(), "You are done!", Toast.LENGTH_SHORT).show();
				} else {
					EasyImage.openChooserWithGallery(this, "Pick source", 0);
				}
				break;
			case R.id.havepolicylayout:
				havepolicytext.setTextColor(getResources().getColor(R.color.colorPrimary));
				havenopolicytext.setTextColor(getResources().getColor(R.color.colorText));
				havepolicyview.setVisibility(View.VISIBLE);
				havenopolicyview.setVisibility(View.INVISIBLE);
				previouspolicynumber.setVisibility(View.VISIBLE);
				agentnumbercard.setVisibility(View.VISIBLE);
				cb_address.setVisibility(View.VISIBLE);
				cb_policy.setText("Upload Policy Document");
				text.setText(getResources().getString(R.string.uploadPolicy));
				ispolicy = true;
				updatelistandcheckbox();
				
				break;
			case R.id.havenopolicylayout:
				havepolicytext.setTextColor(getResources().getColor(R.color.colorText));
				havenopolicytext.setTextColor(getResources().getColor(R.color.colorPrimary));
				havepolicyview.setVisibility(View.INVISIBLE);
				havenopolicyview.setVisibility(View.VISIBLE);
				previouspolicynumber.setVisibility(View.GONE);
				agentnumbercard.setVisibility(View.GONE);
				cb_address.setVisibility(View.GONE);
				cb_policy.setText("Upload Vehivle Photo");
				text.setText(getResources().getString(R.string.addvehiclephote));
				ispolicy = false;
				updatelistandcheckbox();
				
				break;
			case R.id.rvClick:
				finish();
				break;
			default:
				break;
		}
	}
	
	private void updatelistandcheckbox() {
		
		cb_policy.setChecked(false);
		cb_rc.setChecked(false);
		cb_address.setChecked(false);
		cb_id.setChecked(false);
		path.clear();
		photos.clear();
		imagesAdapter.notifyDataSetChanged();
		
	}
	
	private void opencustomdialog() {
		
		dialog = new Dialog(DetailsPreviousActivityS.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.customdialog_uploadimage);
		
		
		dialogfoldername = (TextView) dialog.findViewById(R.id.foldername);
		dialoguploadcount = (TextView) dialog.findViewById(R.id.uploadcount);
		dialogstatus = (TextView) dialog.findViewById(R.id.status);
		dialogprogress = (ProgressBar) dialog.findViewById(R.id.progresss);
		progresspercent = (TextView) dialog.findViewById(R.id.progresspercent);
		
		/*Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
		dialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});*/
		
		dialog.show();
		
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(PHOTOS_KEY, photos);
	}
	
	private void checkGalleryAppAvailability() {
		if (!EasyImage.canDeviceHandleGallery(DetailsPreviousActivityS.this)) {
			//Device has no app that handles gallery intent
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		EasyImage.handleActivityResult(requestCode, resultCode, data, DetailsPreviousActivityS.this, new DefaultCallback() {
			@Override
			public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
				//Some error handling
				e.printStackTrace();
			}
			
			@Override
			public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
				onPhotosReturned(imageFiles);
			}
			
			@Override
			public void onCanceled(EasyImage.ImageSource source, int type) {
				//Cancel handling, you might wanna remove taken photo if it was canceled
				if (source == EasyImage.ImageSource.CAMERA) {
					File photoFile = EasyImage.lastlyTakenButCanceledPhoto(DetailsPreviousActivityS.this);
					if (photoFile != null) photoFile.delete();
				}
			}
			
		});
	}
	
	private void onPhotosReturned(List<File> returnedPhotos) {
		photos.addAll(returnedPhotos);
		textname.add(text.getText().toString());
		imagesAdapter.notifyDataSetChanged();
		changAddPhotoText(text.getText().toString());
		Uri uri = Uri.fromFile(returnedPhotos.get(0));
		String temppath = getPathFromContentUri(uri);
		path.add(temppath);
	}
	
	private void changAddPhotoText(String s) {
		if (s.equalsIgnoreCase(getResources().getString(R.string.uploadPolicy))) {
			folderName.add("policyDocument");
			cb_policy.setChecked(true);
			text.setText(getResources().getString(R.string.upload_rc_book));
		}
		if (s.equalsIgnoreCase(getResources().getString(R.string.addvehiclephote))) {
			folderName.add("Vehicle");
			cb_policy.setChecked(true);
			text.setText(getResources().getString(R.string.upload_rc_book));
		} else if (s.equalsIgnoreCase(getResources().getString(R.string.upload_rc_book))) {
			folderName.add("RcBook");
			cb_rc.setChecked(true);
			if (ispolicy) {
				text.setText(getResources().getString(R.string.upload_address_proof));
			} else {
				text.setText(getResources().getString(R.string.upload_id_proof));
			}
		} else if (s.equalsIgnoreCase(getResources().getString(R.string.upload_address_proof))) {
			folderName.add("AddressProof");
			cb_address.setChecked(true);
			text.setText(getResources().getString(R.string.upload_id_proof));
		} else if (s.equalsIgnoreCase(getResources().getString(R.string.upload_id_proof))) {
			folderName.add("IdProof");
			cb_id.setChecked(true);
			text.setText(getResources().getString(R.string.youaredone));
		}
		if (ispolicy) {
			if (photos.size() == 4) {
				text.setText(getResources().getString(R.string.youaredone));
			}
		}
		else
		{
			if (photos.size() == 3) {
				text.setText(getResources().getString(R.string.youaredone));
			}
		}
		
	}
	
	@Override
	public void onDestroy() {
		// Clear any configuration that was done!
		EasyImage.clearConfiguration(DetailsPreviousActivityS.this);
		super.onDestroy();
	}
	
	public void delete(int position, String s) {
		photos.remove(position);
		textname.remove(position);
		text.setText(s);
		if (s.equalsIgnoreCase(getResources().getString(R.string.uploadPolicy))) {
			folderName.remove(getResources().getString(R.string.policydocument));
			cb_policy.setChecked(false);
		} else if (s.equalsIgnoreCase(getResources().getString(R.string.upload_rc_book))) {
			folderName.remove(getResources().getString(R.string.rcbook));
			cb_rc.setChecked(false);
		} else if (s.equalsIgnoreCase(getResources().getString(R.string.upload_address_proof))) {
			folderName.remove(getResources().getString(R.string.addressproof));
			cb_address.setChecked(false);
		} else if (s.equalsIgnoreCase(getResources().getString(R.string.upload_id_proof))) {
			folderName.remove(getResources().getString(R.string.idproof));
			cb_id.setChecked(false);
		}
		imagesAdapter.notifyDataSetChanged();
		
	}
	
	private String getPathFromContentUri(Uri uri) {
		String path = uri.getPath();
		if (uri.toString().startsWith("content://")) {
			String[] projection = {MediaStore.MediaColumns.DATA};
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
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		IntentFilter f = new IntentFilter();
		f.addAction(UploadService.UPLOAD_STATE_CHANGED_ACTION);
		registerReceiver(uploadStateReceiver, f);
	}
	
	@Override
	public void onStop() {
		unregisterReceiver(uploadStateReceiver);
		super.onStop();
	}
	
	String policydocumentpath, rcbookpath, addressproofpath, idproofpath;
	private BroadcastReceiver uploadStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle b = intent.getExtras();
			//dialogstatus.setText(b.getString(UploadService.MSG_EXTRA));
			if (b.getString(UploadService.MSG_EXTRA).contains("successfully")) {
				uploadedpath.add(new RetrivedFromAWS(b.getString(UploadService.FOLDERNAME), b.getString(UploadService.PATH_LOCATION)));
				if (b.getInt(UploadService.COUNT) == 4) {
					dialog.dismiss();
					for (int i = 0; i < uploadedpath.size(); i++) {
						Log.e("Folder name ", "" + uploadedpath.get(i).getFoldername());
						Log.e("Folder Path ", "" + uploadedpath.get(i).getPath());
						if (uploadedpath.get(i).getFoldername().equals(getResources().getString(R
						    .string.policydocument))) {
							policydocumentpath = uploadedpath.get(i).getPath();
						}
						if (uploadedpath.get(i).getFoldername().equals(getResources().getString(R
						    .string.rcbook))) {
							rcbookpath = uploadedpath.get(i).getPath();
						}
						if (uploadedpath.get(i).getFoldername().equals(getResources().getString(R
						    .string.addressproof))) {
							addressproofpath = uploadedpath.get(i).getPath();
						}
						if (uploadedpath.get(i).getFoldername().equals(getResources().getString(R
						    .string.idproof))) {
							idproofpath = uploadedpath.get(i).getPath();
						}
					}
					
					try {
						uploadDataToServer(policydocumentpath, rcbookpath, addressproofpath, idproofpath);
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
			}
			int percent = b.getInt(UploadService.PERCENT_EXTRA);
			try {
				dialogprogress.setIndeterminate(percent < 0);
				dialogprogress.setProgress(percent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			progresspercent.setText("" + percent + " %");
			dialoguploadcount.setText("" + b.getInt(UploadService.COUNT) + "/" + photos.size());
			dialogfoldername.setText(b.getString(UploadService.FOLDERNAME));
		}
	};
	
	private void uploadDataToServer(String policydocumentpath, String rcbookpath, String addressproofpath, String idproofpath) throws JSONException {
		Download_web web = new Download_web(DetailsPreviousActivityS.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				
			}
		});
		JSONObject object = new JSONObject();
		object.put("policy_status_type_id", "1");
		object.put("policy_no", previouspolicynumber.getText().toString().trim());
		object.put("policy_document", policydocumentpath);
		object.put("policy_rc_book", rcbookpath);
		object.put("policy_address_proof", addressproofpath);
		object.put("policy_id_proof", idproofpath);
		object.put("policy_nominee_name", nomineename.getText().toString());
		object.put("policy_nominee_relationship", nomineerelation.getText().toString());
		object.put("policy_vehicle_id", "");
		web.setReqType("post", ClsGeneral.getPreferences(DetailsPreviousActivityS.this, PrefereneceName
		    .SESSION_TOKEN));
	}
}
