package com.bigappcompany.insurance.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.adapter.VehiclePremiumAdapter;
import com.bigappcompany.insurance.model.VehiclePremiumModel;
import com.bigappcompany.insurance.network.Download_web;
import com.bigappcompany.insurance.utility.ClsGeneral;
import com.bigappcompany.insurance.utility.OnTaskCompleted;
import com.bigappcompany.insurance.utility.PrefereneceName;
import com.bigappcompany.insurance.utility.Utility;
import com.bigappcompany.insurance.utility.WebServices;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 27 Sep 2017 at 2:46 PM
 */

public class PolicyDetails extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
    PaymentResultWithDataListener{
	private CheckBox cb_yellow, cb_white, cb_claimbonus;
	private Button btnProceed;
	private List<String> vehicle_type_id_List = new ArrayList<>();
	private List<String> vehicle_type_name_List = new ArrayList<>();
	private ArrayList<VehiclePremiumModel> list = new ArrayList<>();
	private RecyclerView rc_premium;
	private int pos;
	private String vehicle_idv_id;
	
	private static final String TAG = "PolicyDetails.this";
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.policydetails);
		initWidget();
		Checkout.preload(getApplicationContext());
		getVehicleType();
		try {
			if (cb_claimbonus.isChecked()) {
				getInsurancePlan("1", "1");
			} else {
				getInsurancePlan("1", "0");
			}
			
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		
	}
	
	private void getInsurancePlan(String s, String ncb) throws JSONException {
		
		Download_web web = new Download_web(PolicyDetails.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				
				if (response.length() > 0) {
					Utility.closeDialog();
					try {
						TextView idvvalue = (TextView)findViewById(R.id.idvvalue);
						JSONObject object = new JSONObject(response);
						JSONObject object1 = object.getJSONObject("data");
						JSONObject object2 = object1.getJSONObject("Idv_info");
						vehicle_idv_id = object2.optString("vehicle_idv_id");
						String vehicle_idv_value = object2.optString("vehicle_idv_value");
						idvvalue.setText("IDV- "+vehicle_idv_value);
						setDataToList(object1.getJSONArray("premium_info"));
						
						if (object.optString("status").equals("0")) {
							Toast.makeText(PolicyDetails.this, "" + object.optString("msg"), Toast
							    .LENGTH_SHORT).show();
						}
						if (object.optString("status").equals("-2")) {
							object.optString("msg");
							startActivity(new Intent(PolicyDetails.this, LoginActivity.class));
							new HomeScreenActivity().finishActivity();
						}
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
			}
		});
		JSONObject object = new JSONObject();
		object.put("vehicle_id", "2");
		object.put("vehicle_type_id", "2");//s
		object.put("ncb_apply", "1");//ncb
		web.setPutdata(object.toString());
		web.setReqType("post", ClsGeneral.getPreferences(PolicyDetails.this, PrefereneceName.SESSION_TOKEN));
		web.execute(WebServices.GET_INSURANCE_PREMIUM);
	}
	
	private void setDataToList(JSONArray premium_info) throws JSONException {
		list.clear();
		for (int i = 0; i < premium_info.length(); i++) {
			JSONObject jsonObject = premium_info.getJSONObject(i);
			list.add(new VehiclePremiumModel(jsonObject.optString("insurance_package_id"), jsonObject
			    .optString("insurance_package_name"), jsonObject.optString("ipc_id"), jsonObject.optString("policy_actual_premium_price")
			    , jsonObject.optString("policy_final_premium_price"), jsonObject.optString("ipc_ncb_percentage")));
		}
		
		VehiclePremiumAdapter adapter = new VehiclePremiumAdapter(PolicyDetails.this, list, new ClickPosition() {
			
			@Override
			public void pos(int position) {
				pos = position;
				btnProceed.setText("Proceed to pay " + list.get(position).getPolicy_final_premium_price());
			}
		});
		rc_premium.setAdapter(adapter);
		btnProceed.setText("Proceed to pay " + list.get(0).getPolicy_final_premium_price());
		
	}
	
	private void getVehicleType() {
		
		Download_web web = new Download_web(PolicyDetails.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				
				Utility.closeDialog();
				try {
					JSONObject obj = new JSONObject(response);
					if (obj.optString("status").equals("1")) {
						JSONArray ja = obj.getJSONArray("data");
						for (int i = 0; i < ja.length(); i++) {
							JSONObject jo = ja.getJSONObject(i);
							vehicle_type_id_List.add(jo.optString("vehicle_type_id"));
							vehicle_type_name_List.add(jo.optString("vehicle_type_name"));
						}
						setVehicleName();
						
					}
					if (obj.optString("status").equals("0")) {
						Toast.makeText(PolicyDetails.this, "" + obj.optString("msg"), Toast
						    .LENGTH_SHORT).show();
					}
					if (obj.optString("status").equals("-2")) {
						obj.optString("msg");
						startActivity(new Intent(PolicyDetails.this, LoginActivity
						    .class));
					}
				} catch (JSONException e) {
					Log.e(TAG, e.getMessage(), e);
				}
				
				
			}
			
		});
		web.setReqType("get", ClsGeneral.getPreferences(PolicyDetails.this, PrefereneceName.SESSION_TOKEN));
		web.execute(WebServices.GET_VEHICLE_TYPE);
	}
	
	private void setVehicleName() {
		cb_yellow.setText(vehicle_type_name_List.get(0));
		cb_white.setText(vehicle_type_name_List.get(1));
		cb_yellow.setChecked(true);
	}
	
	
	private void initWidget() {
		TextView termascondition = (TextView) findViewById(R.id.termascondition);
		TextView tvHeading = (TextView) findViewById(R.id.tvHeading);
		btnProceed = (Button) findViewById(R.id.btnProceed);
		rc_premium = (RecyclerView) findViewById(R.id.rc_premium);
		rc_premium.setHasFixedSize(true);
		rc_premium.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false));
		SpannableString ssPhone = new SpannableString(getResources().getString(R.string.termscondition));
		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				Toast.makeText(PolicyDetails.this, "success", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(true);
			}
		};
		
		ssPhone.setSpan(clickableSpan, 0, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssPhone.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorsBlue)), 0, 5, 0);
		ssPhone.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorGradient)), 5, 18, 0);
		termascondition.setText(ssPhone);
		termascondition.setMovementMethod(LinkMovementMethod.getInstance());
//		holder.tvDetails.setBackgroundColor(context.getResources().getColor(R.color.colorsBlue));
		termascondition.setHighlightColor(PolicyDetails.this.getResources().getColor(R.color.colorsBlue));
		
		cb_yellow = ((AppCompatCheckBox) findViewById(R.id.cb_yellow));
		cb_white = ((AppCompatCheckBox) findViewById(R.id.cb_white));
		cb_claimbonus = ((AppCompatCheckBox) findViewById(R.id.cb_claimbonus));
		
		
		cb_yellow.setOnCheckedChangeListener(this);
		cb_white.setOnCheckedChangeListener(this);
		cb_claimbonus.setOnCheckedChangeListener(this);
		
		
		tvHeading.setText(getIntent().getStringExtra("name"));
		btnProceed.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startPayment();
				
			}
		});
		
		findViewById(R.id.rvClick).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
	
	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
		if (b) {
			try {
				if (compoundButton == cb_yellow) {
					
					cb_white.setChecked(false);
					//cb_claimbonus.setChecked(false);
				}
				if (compoundButton == cb_white) {
					cb_yellow.setChecked(false);
					//cb_claimbonus.setChecked(false);
				}
				if (compoundButton == cb_claimbonus) {
					//cb_yellow.setChecked(false);
					//cb_white.setChecked(false);
					//cb_claimbonus.setChecked(false);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		cb_claimbonus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				opendialog();
				
			}
		});
		
		
	}
	
	private void opendialog() {
		final Dialog dialog = new Dialog(PolicyDetails.this);
		dialog.setContentView(R.layout.claimbonusdialog);
		dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
		    WindowManager.LayoutParams.WRAP_CONTENT);
		
		TextView text = (TextView) dialog.findViewById(R.id.text);
		TextView yes = (TextView) dialog.findViewById(R.id.yes);
		TextView no = (TextView) dialog.findViewById(R.id.no);
		// if button is clicked, close the custom dialog
		yes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cb_claimbonus.setChecked(true);
				dialog.dismiss();
			}
		});
		no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cb_claimbonus.setChecked(false);
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}
	
	public void startPayment() {
	/*
	  You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
		final Activity activity = this;
		
		final Checkout co = new Checkout();
		
		try {
			JSONObject options = new JSONObject();
			options.put("name", "Razorpay Corp");
			options.put("description", "Demoing Charges");
			options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
			options.put("currency", "INR");
			options.put("amount", "" + (Double.parseDouble(list.get(pos).getPolicy_final_premium_price())) * 100);
			//options.put("amount", "1200");
			JSONObject preFill = new JSONObject();
			preFill.put("email", "test@razorpay.com");
			preFill.put("contact", "9876543210");
			options.put("prefill", preFill);
			options.put("order_id", "order_8t1FiAYcPnOK5d");
			
			co.open(activity, options);
		} catch (Exception e) {
			Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
			    .show();
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	@Override
	public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
		try {
			Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
			Log.e("pa_id", paymentData.getPaymentId());
			Log.e("signature", paymentData.getSignature());
			Log.e("order_id", paymentData.getOrderId());
		} catch (Exception e) {
			Log.e("Test", "Exception in onPaymentSuccess", e);
		}
	}
	
	/**
	 * The name of the function has to be
	 * onPaymentError
	 * Wrap your code in try catch, as shown, to ensure that this method runs correctly
	 */
	@SuppressWarnings("unused")
	@Override
	public void onPaymentError(int code, String response,PaymentData paymentData) {
		try {
			Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e("Text", "Exception in onPaymentError", e);
		}
	}
}
