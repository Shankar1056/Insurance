package com.bigappcompany.insurance.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.activity.HomeScreenActivity;
import com.bigappcompany.insurance.activity.LoginActivity;
import com.bigappcompany.insurance.activity.VehicleDetailsActivity;
import com.bigappcompany.insurance.adapter.ImageSliderAdapter;
import com.bigappcompany.insurance.network.Download_web;
import com.bigappcompany.insurance.utility.ClsGeneral;
import com.bigappcompany.insurance.utility.OnTaskCompleted;
import com.bigappcompany.insurance.utility.PrefereneceName;
import com.bigappcompany.insurance.utility.Utility;
import com.bigappcompany.insurance.utility.WebServices;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 04 Aug 2017 at 11:51 AM
 */

public class FindDetailsFragment extends Fragment {
	
	private View rootView;
	private DrawerArrowDrawable drawerArrowDrawable;
	private CirclePageIndicator indicator;
	private ViewPager viewPager;
	private RelativeLayout rvClick;
	private Button btnGetDetails;
	private EditText vehiclereg_number;
	private static final String TAG = "FindDetailsFragment";
	
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.find_details_fragment, null);
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
		    .setDefaultFontPath("fonts/Celias.otf")
		    .setFontAttrId(R.attr.fontPath)
		    .build()
		);
		initViews();
		eventListners();
		return rootView;
	}
	
	
	private void eventListners() {
		
		ImageSliderAdapter homeViewPagerAdapter = new ImageSliderAdapter(getActivity());
		viewPager.setAdapter(homeViewPagerAdapter);
		indicator.setViewPager(viewPager);
		
		rvClick.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				((HomeScreenActivity) getActivity()).openDrawer();
				
			}
		});
		
		btnGetDetails.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (vehiclereg_number.getText().toString().trim().equals(""))
				{
					Toast.makeText(getActivity(), "Enter Vehicle registration number", Toast.LENGTH_SHORT).show();
					vehiclereg_number.requestFocus();
					return;
				}
				else
				{
					try {
						getVehicleDetails();
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
				
			}
		});
		
		
	}
	
	private void getVehicleDetails() throws JSONException {
		Utility.showDailog(getActivity(),getResources().getString(R.string.pleasewait));
		Download_web web = new Download_web(getActivity(), new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				Log.e("Response : ", response);
				if (response.length() > 0) {
					Utility.closeDialog();
					try {
						JSONObject object = new JSONObject(response);
						if (object.optString("status").equals("1")) {
							Intent intent = new Intent(getActivity(), VehicleDetailsActivity.class);
							intent.putExtra("response",response);
							startActivity(intent);
						}
						if (object.optString("status").equals("0")) {
							Toast.makeText(getActivity(), ""+object.optString("msg"), Toast.LENGTH_SHORT).show();
						}
						if (object.optString("status").equals("-2")) {
							object.optString("msg");
							startActivity(new Intent(getActivity(), LoginActivity.class));
							new HomeScreenActivity().finishActivity();
						}
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
				
			}
			
		});
		JSONObject object = new JSONObject();
		//object.put("vehicle_no","KAO1HC3714");
		object.put("vehicle_no",vehiclereg_number.getText().toString().trim());
		web.setPutdata(object.toString());
		web.setReqType("post", ClsGeneral.getPreferences(getActivity()
		, PrefereneceName.SESSION_TOKEN));
		web.execute(WebServices.FIND_VEHICLE_NUMBER);
	}
	
	
	private void initViews() {
		
		indicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
		viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
		rvClick = (RelativeLayout) rootView.findViewById(R.id.rvClick);
		btnGetDetails = (Button) rootView.findViewById(R.id.btnGetDetails);
		vehiclereg_number = (EditText) rootView.findViewById(R.id.vehiclereg_number);
		
		/*vehiclereg_number.setInputType(InputType.TYPE_CLASS_TEXT);
		vehiclereg_number.setFilters(new InputFilter[]{new InputFilter.AllCaps()});*/
		
		InputFilter[] editFilters = vehiclereg_number.getFilters();
		InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
		System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
		newFilters[editFilters.length] = new InputFilter.AllCaps();
		vehiclereg_number.setFilters(newFilters);
		
	}
	
	
}
