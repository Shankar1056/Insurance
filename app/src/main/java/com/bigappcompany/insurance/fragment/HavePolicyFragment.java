package com.bigappcompany.insurance.fragment;

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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.activity.DetailsPreviousPolicy;
import com.bigappcompany.insurance.activity.LoginActivity;
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
import com.bigappcompany.insurance.utility.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import static com.bigappcompany.insurance.R.id.status;


/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 04 Aug 2017 at 11:51 AM
 */

@SuppressWarnings("ConstantConditions")
public class HavePolicyFragment extends Fragment implements View.OnClickListener {
	private static final String TAG = "HavePolicyFragment";
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
	private ProgressBar dialogprogress;
	private Dialog dialog;
	private String[] permision = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest
	    .permission.CAMERA};
	
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_have_policy, null);
		
		view.findViewById(R.id.iv_selected).setOnClickListener(this);
		view.findViewById(R.id.btnProceedInsurance).setOnClickListener(this);
		text = (TextView) view.findViewById(R.id.text);
		previouspolicynumber = (EditText) view.findViewById(R.id.previouspolicynumber);
		nomineename = (EditText) view.findViewById(R.id.nomineename);
		nomineerelation = (EditText) view.findViewById(R.id.nomineerelation);
		agentnumber = (EditText) view.findViewById(R.id.agentnumber);
		rv_images = (RecyclerView) view.findViewById(R.id.rv_images);
		cb_policy = ((AppCompatCheckBox) view.findViewById(R.id.cb_policy));
		cb_rc = ((AppCompatCheckBox) view.findViewById(R.id.cb_rc));
		cb_address = ((AppCompatCheckBox) view.findViewById(R.id.cb_address));
		cb_id = ((AppCompatCheckBox) view.findViewById(R.id.cb_id));
		
		Nammu.init(getActivity());
		
		if (savedInstanceState != null) {
			photos = (ArrayList<File>) savedInstanceState.getSerializable(PHOTOS_KEY);
		}
		try {
			imagesAdapter = new ImagesAdapter(getActivity(), photos, textname, HavePolicyFragment.this);
			rv_images.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
			rv_images.setHasFixedSize(true);
			rv_images.setAdapter(imagesAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			Nammu.askForPermission(getActivity(), permision,new PermissionCallback() {
				@Override
				public void permissionGranted() {
					//Nothing, this sample saves to Public gallery so it needs permission
				}
				
				@Override
				public void permissionRefused() {
					
				}
			});
		}
		
		EasyImage.configuration(getActivity())
		    .setImagesFolderName("EasyImage sample")
		    .setCopyTakenPhotosToPublicGalleryAppFolder(true)
		    .setCopyPickedImagesToPublicGalleryAppFolder(true)
		    .setAllowMultiplePickInGallery(true);
		
		checkGalleryAppAvailability();
		
		
		return view;
	}
	
	private void uloadfiletoAws() {
		
		
		Intent intent = new Intent(getActivity(), UploadService.class);
		//intent.putExtra(UploadService.ARG_FILE_PATH, iamgepath);
		//intent.putExtra(UploadService.ARG_FOLDER_NAME, foldrname);
		intent.putExtra(UploadService.FRAGMENT_NAME, "policy");
		intent.putStringArrayListExtra(UploadService.ARG_FILE_PATH, path);
		intent.putStringArrayListExtra(UploadService.ARG_FOLDER_NAME, folderName);
		getActivity().startService(intent);
	}
	
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btnProceedInsurance:
				if (nomineename.getText().toString().trim().equals("")) {
					Utility.displayMessageAlert("Enter Nominee Name", getActivity());
					return;
				}
				if (nomineerelation.getText().toString().trim().equals("")) {
					Utility.displayMessageAlert("Enter Relation with Nominee", getActivity());
					return;
				}
				/*if (photos.size() < 4) {
					Utility.toastDisplay("All documents are mandatory", getActivity());
					return;
				}*/
				if (textname.size() > 0) {
					for (int i = 0; i < textname.size(); i++) {
						if (textname.get(i).equalsIgnoreCase(getResources().getString(R.string.upload_rc_book)
						)) {
							try {
								uploadedpath.clear();
								opencustomdialog();
								uloadfiletoAws();
								break;
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							
						}
						if (i + 1 == textname.size()) {
							Utility.displayMessageAlert("Upload RC book is Mendatory",
							    getActivity());
							return;
						}
					}
				} else {
					Utility.displayMessageAlert("RcBook is Mendatory", getActivity());
					return;
				}
				
				
				break;
			
			case R.id.iv_selected:
				if (text.getText().toString().equals(getResources().getString(R.string.youaredone))) {
					Toast.makeText(getActivity(), "You are done!", Toast.LENGTH_SHORT).show();
				} else {
					EasyImage.openChooserWithGallery(this, "Pick source", 0);
				}
				break;
		}
	}
	
	private void opencustomdialog() {
		
		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.customdialog_uploadimage);
		
		
		dialogfoldername = (TextView) dialog.findViewById(R.id.foldername);
		dialoguploadcount = (TextView) dialog.findViewById(R.id.uploadcount);
		dialogstatus = (TextView) dialog.findViewById(status);
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
		if (!EasyImage.canDeviceHandleGallery(getActivity())) {
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
		
		EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
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
					File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());
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
		
		if (s.equalsIgnoreCase(getResources().getString(R.string.upload_rc_book))) {
			folderName.add("RcBook");
			cb_rc.setChecked(true);
			checkCheckBox();
		} else if
		    (s.equalsIgnoreCase(getResources().getString(R.string.uploadPolicy))) {
			folderName.add("policyDocument");
			cb_policy.setChecked(true);
			checkCheckBox();
		} else if (s.equalsIgnoreCase(getResources().getString(R.string.upload_address_proof))) {
			folderName.add("AddressProof");
			cb_address.setChecked(true);
			checkCheckBox();
		} else if (s.equalsIgnoreCase(getResources().getString(R.string.upload_id_proof))) {
			folderName.add("IdProof");
			cb_id.setChecked(true);
			checkCheckBox();
		}
		
		if (photos.size() == 4) {
			text.setText(getResources().getString(R.string.youaredone));
		}
	}
	
	private void checkCheckBox() {
		
		if (!cb_rc.isChecked()) {
			text.setText(getResources().getString(R.string.upload_rc_book));
			return;
		}
		if (!cb_policy.isChecked()) {
			text.setText(getResources().getString(R.string.uploadPolicy));
			return;
		}
		if (!cb_address.isChecked()) {
			text.setText(getResources().getString(R.string.upload_address_proof));
			return;
		}
		if (!cb_id.isChecked()) {
			text.setText(getResources().getString(R.string.upload_id_proof));
			return;
		}
	}
	
	@Override
	public void onDestroy() {
		// Clear any configuration that was done!
		EasyImage.clearConfiguration(getActivity());
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
			ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
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
		getActivity().registerReceiver(uploadStateReceiver, f);
	}
	
	@Override
	public void onStop() {
		getActivity().unregisterReceiver(uploadStateReceiver);
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
				if (b.getInt(UploadService.COUNT) == textname.size()) {
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
						uploadDataToServer(policydocumentpath, rcbookpath, addressproofpath,
						    idproofpath);
						//startActivity(new Intent(getActivity(), DetailsPreviousPolicy.class));
						path.clear();
						
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
			}
			int percent = b.getInt(UploadService.PERCENT_EXTRA);
			try {
				dialogprogress.setIndeterminate(percent < 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			dialogprogress.setProgress(percent);
			progresspercent.setText("" + percent + " %");
			dialoguploadcount.setText("" + b.getInt(UploadService.COUNT) + "/" + photos.size());
			dialogfoldername.setText(b.getString(UploadService.FOLDERNAME));
		}
	};
	
	private void uploadDataToServer(String policydocumentpath, String rcbookpath, String addressproofpath, String idproofpath) throws JSONException {
		Download_web web = new Download_web(getActivity(), new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				if (response.length() > 0) {
					
					
					Utility.closeDialog();
					try {
						JSONObject obj = new JSONObject(response);
						if (obj.optString("status").equals("1")) {
							JSONObject jo = obj.getJSONObject("data");
							String ploicy_id = jo.optString("ploicy_id");
							
							Intent intent = new Intent(getActivity(), DetailsPreviousPolicy.class);
							startActivity(intent);
							
						}
						if (obj.optString("status").equals("0")) {
							Toast.makeText(getActivity(), "" + obj.optString("msg"), Toast
							    .LENGTH_SHORT).show();
						}
						if (obj.optString("status").equals("-2")) {
							obj.optString("msg");
							getActivity().startActivity(new Intent(getActivity(), LoginActivity
							    .class));
						}
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
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
		object.put("policy_vehicle_id", ClsGeneral.getPreferences(getActivity(), PrefereneceName.VEHICLE_ID));
		//object.put("policy_vehicle_id", "");
		web.setReqType("post", ClsGeneral.getPreferences(getActivity(), PrefereneceName.SESSION_TOKEN));
		web.setPutdata(object.toString());
		web.execute(WebServices.SET_POLICY_DETAILS);
	}
}
