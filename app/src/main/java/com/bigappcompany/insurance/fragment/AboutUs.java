package com.bigappcompany.insurance.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.activity.HomeScreenActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 02 Oct 2017 at 12:10 PM
 */

@SuppressLint("ValidFragment")
public class AboutUs extends Fragment {
	private TextView call, email;
	public String text;
	
	public AboutUs(String text) {
		this.text = text;
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = null;
		if (text.equals("aboutus")) {
			view = inflater.inflate(R.layout.aboutus, container, false);
		} else if (text.equals("terms")) {
			view = inflater.inflate(R.layout.aboutus, container, false);
		} else if (text.equals("policy")) {
			view = inflater.inflate(R.layout.aboutus, container, false);
		} else if (text.equals("contactus")) {
			view = inflater.inflate(R.layout.contactus, container, false);
			initcontactwidget(view);
		}
		else if (text.equals("refund")) {
			view = inflater.inflate(R.layout.aboutus, container, false);
		}
		
		return view;
	}
	
	private void initcontactwidget(View view) {
		call = (TextView) view.findViewById(R.id.call);
		email = (TextView) view.findViewById(R.id.email);
		call.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (isPermissionGranted()) {
					call_action();
				}
			}
		});
		email.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
				getActivity().startActivity(Intent.createChooser(emailIntent, "Send mail..."));*/
				
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
				    "mailto","support@bookmyinsurance.co.in", null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
			}
		});
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
		    .setDefaultFontPath("fonts/Celias.otf")
		    .setFontAttrId(R.attr.fontPath)
		    .build()
		);
		initwidget(view);
		eventListners(view);
	}
	
	private void eventListners(View view) {
		RelativeLayout rvClick = (RelativeLayout) view.findViewById(R.id.rv_click);
		rvClick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((HomeScreenActivity) getActivity()).openDrawer();
			}
		});
		
	}
	
	private void initwidget(View view) {
		TextView tvHeading = (TextView) view.findViewById(R.id.tvHeading);
		TextView tv_text = (TextView) view.findViewById(R.id.tv_text);
		
		
		if (text.equals("aboutus")) {
			tvHeading.setText(getResources().getString(R.string.aboutus));
			tv_text.setText(getResources().getString(R.string.aboutusdesc));
		} else if (text.equals("terms")) {
			tvHeading.setText(getResources().getString(R.string.termscondition));
			tv_text.setText(Html.fromHtml(getResources().getString(R.string.termsconditiondesc)));
		} else if (text.equals("policy")) {
			tvHeading.setText(getResources().getString(R.string.policy));
			tv_text.setText(Html.fromHtml(getResources().getString(R.string.privacypolicy)));
		}
		else if (text.equals("refund")) {
			tvHeading.setText(getResources().getString(R.string.refunfcancellation));
			tv_text.setText(Html.fromHtml(getResources().getString(R.string.cancellrefund)));
		}
		else if (text.equals("contactus")) {
			tvHeading.setText("Contact Us");
		}
	}
	
	public boolean isPermissionGranted() {
		if (Build.VERSION.SDK_INT >= 23) {
			if (getActivity().checkSelfPermission(android.Manifest.permission.CALL_PHONE)
			    == PackageManager.PERMISSION_GRANTED) {
				Log.v("TAG", "Permission is granted");
				return true;
			} else {
				
				Log.v("TAG", "Permission is revoked");
				ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
				return false;
			}
		} else { //permission is automatically granted on sdk<23 upon installation
			Log.v("TAG", "Permission is granted");
			return true;
		}
	}
	
	
	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       String permissions[], int[] grantResults) {
		switch (requestCode) {
			
			case 1: {
				
				if (grantResults.length > 0
				    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
					call_action();
				} else {
					Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
				}
				return;
			}
			
			// other 'case' lines to check for other
			// permissions this app might request
		}
	}
	
	public void call_action() {
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:9601517867"));
			startActivity(callIntent);
		} catch (Exception e) {
			
		}
	}
}
