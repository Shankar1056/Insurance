package com.bigappcompany.insurance.activity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.network.Download_web;
import com.bigappcompany.insurance.utility.ClsGeneral;
import com.bigappcompany.insurance.utility.OnTaskCompleted;
import com.bigappcompany.insurance.utility.PrefereneceName;
import com.bigappcompany.insurance.utility.SmsBroadcastReceiver;
import com.bigappcompany.insurance.utility.Utility;
import com.bigappcompany.insurance.utility.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener {
	private ImageView ivNext, ivNextSignup;
	private FrameLayout frameLayout;
	private View loginView, signupView, otpView;
	private LinearLayout llAgentCode;
	private CheckBox checkBox;
	private EditText etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6, et_number, et_name, et_email, et_agentCode;
	private TextView tvResend;
	private ImageView ivNextToOTP;
	private TextView tvStatement;
	private StringBuilder otpString;
	private SmsBroadcastReceiver readsms;
	private IntentFilter intentFilter;
	private static String otp;
	private static final String TAG = "LoginActivity.class";
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Window window = this.getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorGreen));
		}
		
		setContentView(R.layout.activity_login_main);
		overridePendingTransition(R.anim.start, R.anim.end);
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
		    .setDefaultFontPath("fonts/Pangram-Bold.otf")
		    .setFontAttrId(R.attr.fontPath)
		    .build()
		);
		
		frameLayout = (FrameLayout) findViewById(R.id.llLayout);
		loginView = getLayoutInflater().inflate(R.layout.activity_login, null);
		otpView = getLayoutInflater().inflate(R.layout.activity_otp, null);
		signupView = getLayoutInflater().inflate(R.layout.activity_signup, null);
		if (ClsGeneral.getBoolean(LoginActivity.this,PrefereneceName.OTP_DONE))
		{
			frameLayout.addView(signupView);
			et_name = (EditText) findViewById(R.id.et_name);
			et_email = (EditText) findViewById(R.id.et_email);
			et_agentCode = (EditText) findViewById(R.id.et_agentCode);
		}
		else {
			frameLayout.addView(loginView);
		}
		initViews();
		eventListerns();
		getsmsandcheckpermission();
	}
	
	private void getsmsandcheckpermission() {
		readsms = new SmsBroadcastReceiver() {
			@Override
			protected void onSmsReceived(String s) {
				if (s != null && s.length() > 0) {
					if (s.contains("SpotSoon")) {
						String splitmsg[] = s.split(" ");
						otp = splitmsg[0];
						try {
							etOtp1.setText("" + otp.charAt(0));
							etOtp2.setText("" + otp.charAt(1));
							etOtp3.setText("" + otp.charAt(2));
							etOtp4.setText("" + otp.charAt(3));
							etOtp5.setText("" + otp.charAt(4));
							if (submit().length() <= 4) {
								Utility.displayMessageAlert("Enter 5 digit otp",
								    LoginActivity.this);
							} else if (submit().length() == 5) {
								
								verifyOtp();
								
								/*if (ClsGeneral.getPreferences(LoginActivity.this,
								    ClsGeneral.USER_ID).equals(""))
								{
									
								}
								else
								{
									frameLayout.removeAllViews();
									otpView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right));
									frameLayout.addView(signupView);
								}*/
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		
		intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		intentFilter.setPriority(1000);
		
		if (Build.VERSION.SDK_INT >= 23) {
			checkLocationPermission();
		}
	}
	
	private void verifyOtp() throws JSONException {
		Utility.showDailog(LoginActivity.this, "Getting otp," + getResources().getString(R.string.pleasewait));
		Download_web web = new Download_web(LoginActivity.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				Log.e("Response : ", response);
				if (response.length() > 0) {
					Utility.closeDialog();
					try {
						JSONObject object = new JSONObject(response);
						if (object.optString("status").equals("1")) {
							JSONObject jsonObject = object.getJSONObject("data");
							ClsGeneral.setPreferences(LoginActivity.this, PrefereneceName
							    .SESSION_TOKEN, jsonObject.optString("token"));
							JSONObject jo = jsonObject.getJSONObject("user_data");
							ClsGeneral.setPreferences(LoginActivity.this, PrefereneceName
							    .USER_NAME, jo.optString("name"));
							ClsGeneral.setPreferences(LoginActivity.this, PrefereneceName
							    .USER_PHONE, jo.optString("phone"));
							ClsGeneral.setPreferences(LoginActivity.this, PrefereneceName
							    .USER_PHONE, jo.optString("email"));
							ClsGeneral.setBoolean(LoginActivity.this, PrefereneceName
							    .OTP_DONE, true);
							
							if (jo.optString("email") == null || jo.optString("email")
							    .equals("null") || jo.optString("email").equals("")) {
								frameLayout.removeAllViews();
								signupView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right));
								frameLayout.addView(signupView);
								et_name = (EditText) findViewById(R.id.et_name);
								et_email = (EditText) findViewById(R.id.et_email);
								et_agentCode = (EditText) findViewById(R.id.et_agentCode);
								
							} else {
								ClsGeneral.setBoolean(LoginActivity.this, PrefereneceName
								    .USER_EXIST,true);
								Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
								startActivity(intent);
								finishAffinity();
							}
							
							
						}
						if (object.optString("status").equals("0")) {
							Toast.makeText(LoginActivity.this, ""+object.optString("msg"), Toast.LENGTH_SHORT).show();
						}
						if (object.optString("status").equals("-2")) {
							object.optString("msg");
							frameLayout.removeAllViews();
							signupView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right));
							frameLayout.addView(loginView);
						}
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
				
			}
			
		});
		JSONObject object = new JSONObject();
		object.put("otp", submit().toString());
		web.setPutdata(object.toString());
		web.setReqType("post", "");
		web.execute(WebServices.VERIFY_OTP);
	}
	
	private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
	
	private void checkLocationPermission() {
		if (ContextCompat.checkSelfPermission(this,
		    Manifest.permission.RECEIVE_SMS)
		    != PackageManager.PERMISSION_GRANTED) {
			
			
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
			    Manifest.permission.RECEIVE_SMS)) {
				
				ActivityCompat.requestPermissions(this,
				    new String[]{Manifest.permission.RECEIVE_SMS},
				    MY_PERMISSIONS_REQUEST_LOCATION);
				
				
			} else {
				ActivityCompat.requestPermissions(this,
				    new String[]{Manifest.permission.RECEIVE_SMS},
				    MY_PERMISSIONS_REQUEST_LOCATION);
			}
			
		} else {
			
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_LOCATION: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
				    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if (ContextCompat.checkSelfPermission(this,
					    Manifest.permission.RECEIVE_SMS)
					    == PackageManager.PERMISSION_GRANTED) {
						
						/*if (mGoogleApiClient == null) {
							buildGoogleApiClient();
						}
						mMap.setMyLocationEnabled(true);*/
					}
					
				} else {
					
					Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
				}
				
			}
			
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (intentFilter != null) {
			this.registerReceiver(readsms, intentFilter);
		}
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		if (intentFilter != null) {
			this.unregisterReceiver(readsms);
		}
	}
	
	private void eventListerns() {
		
		ivNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (et_number.getText().toString().trim().equals("")) {
					Utility.toastDisplay(getResources().getString(R.string
					    .pleasewait), LoginActivity.this);
					return;
				}
				if (et_number.getText().toString().length() <= 9) {
					Utility.toastDisplay("Enter 10 digit number", LoginActivity.this);
					return;
				} else {
					try {
						getOtp();
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage(), e);
					}
					
				}
			}
		});
		
		ivNextToOTP.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				if (submit().length() <= 4) {
					Utility.displayMessageAlert("Enter 5 digit otp",
					    LoginActivity.this);
				} else if (submit().length() == 5) {
					
					try {
						verifyOtp();
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
				
			}
		});
		ivNextSignup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					if (et_name.getText().toString().trim().equals("")) {
						Toast.makeText(LoginActivity.this, "Enter your name", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
				if (et_email.getText().toString().trim().equals("")) {
					Toast.makeText(LoginActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!Utility.isValiedEmail(et_email.getText().toString().trim())) {
					Toast.makeText(LoginActivity.this, "Enter correct email", Toast.LENGTH_SHORT).show();
					return;
				}
				if (checkBox.isChecked()) {
					if (et_agentCode.getText().toString().trim().equals("")) {
						Toast.makeText(LoginActivity.this, "Enter agent code", Toast.LENGTH_SHORT).show();
						return;
					}
				} else {
					try {
						saveuserdetails();
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
				
			}
		});
		
		
		checkBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (checkBox.isChecked()) {
					llAgentCode.setVisibility(View.VISIBLE);
				} else {
					llAgentCode.setVisibility(View.GONE);
				}
			}
		});
		
		clickforEt();
		
		
	}
	
	private void saveuserdetails() throws JSONException {
		Utility.showDailog(LoginActivity.this, getResources().getString(R.string.pleasewait));
		Download_web web = new Download_web(LoginActivity.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				Log.e("Response : ", response);
				if (response.length() > 0) {
					Utility.closeDialog();
					try {
						JSONObject object = new JSONObject(response);
						if (object.optString("status").equals("1")) {
							ClsGeneral.setPreferences(LoginActivity.this, PrefereneceName
							    .USER_NAME, et_name.getText().toString());
							ClsGeneral.setPreferences(LoginActivity.this, PrefereneceName
							    .USER_EMAIL, et_email.getText().toString());
							ClsGeneral.setBoolean(LoginActivity.this,PrefereneceName
							    .USER_EXIST,true);
							Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
							startActivity(intent);
							finishAffinity();
							
						}
						if (object.optString("status").equals("0")) {
							Toast.makeText(LoginActivity.this, ""+object.optString("msg"), Toast.LENGTH_SHORT).show();
						}
						if (object.optString("status").equals("-2")) {
							object.optString("msg");
							frameLayout.removeAllViews();
							signupView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right));
							frameLayout.addView(loginView);
						}
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
				
			}
			
		});
		JSONObject object = new JSONObject();
		object.put("name", et_name.getText().toString());
		object.put("email", et_email.getText().toString());
		//object.put("insurance_agent_code", et_agentCode.getText().toString());
		object.put("insurance_agent_code", "SAVlF4J0nGWfkHn");
		
		web.setPutdata(object.toString());
		web.setReqType("post", ClsGeneral.getPreferences(LoginActivity.this, PrefereneceName
		    .SESSION_TOKEN));
		web.execute(WebServices.UPDATE_USER_DETAILS);
	}
	
	private void getOtp() throws JSONException {
		Utility.showDailog(LoginActivity.this, getResources().getString(R.string.pleasewait));
		Download_web web = new Download_web(LoginActivity.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				Log.e("Response : ", response);
				if (response.length() > 0) {
					Utility.closeDialog();
					try {
						JSONObject object = new JSONObject(response);
						if (object.optString("status").equals("1")) {
							frameLayout.removeAllViews();
							signupView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right));
							frameLayout.addView(otpView);
						}
						if (object.optString("status").equals("0")) {
							Toast.makeText(LoginActivity.this, ""+object.optString("msg"), Toast.LENGTH_SHORT).show();
						}
						if (object.optString("status").equals("-2")) {
							object.optString("msg");
							frameLayout.removeAllViews();
							signupView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_to_right));
							frameLayout.addView(loginView);
						}
					} catch (JSONException e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
				
			}
			
		});
		JSONObject object = new JSONObject();
		object.put("phone", et_number.getText().toString());
		web.setPutdata(object.toString());
		web.setReqType("post", "");
		web.execute(WebServices.SEND_OTP);
	}
	
	private String submit() {
		
		otpString = new StringBuilder();
		otpString.append(etOtp1.getText().toString());
		otpString.append(etOtp2.getText().toString());
		otpString.append(etOtp3.getText().toString());
		otpString.append(etOtp4.getText().toString());
		otpString.append(etOtp5.getText().toString());
		Log.d("Sanjeev", "otpString" + "" + otpString.toString());
		return otpString.toString();
		
	}
	
	
	private void clickforEt() {
		
		etOtp1.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				
			}
			
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				
				if (etOtp1.getText().toString().length() == 1) {
					etOtp1.clearFocus();
					etOtp2.requestFocus();
				}
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
			}
		});
		
		
		etOtp2.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}
			
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				
				if (etOtp2.getText().toString().length() == 1)     //size as per your requirement
				{
					etOtp3.requestFocus();
				}
				
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
				
			}
		});
		etOtp3.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				
			}
			
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				
				if (etOtp3.getText().toString().length() == 1)     //size as per your requirement
				{
					etOtp4.requestFocus();
				}
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
				
			}
		});
		
		etOtp4.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				
			}
			
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				
				if (etOtp4.getText().toString().length() == 1)     //size as per your requirement
				{
					etOtp5.requestFocus();
				}
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
				
			}
		});
		etOtp5.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				
			}
			
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				
				if (etOtp5.getText().toString().length() == 1)     //size as per your requirement
				{
					hideSoftKeyboard(etOtp5);
				}
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
				
			}
		});
		
		
		etOtp2.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_DEL) {
					
					if (etOtp2.getText().toString().length() == 0) {
						etOtp1.requestFocus();
					}
				}
				return false;
			}
		});
		etOtp3.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_DEL) {
					if (etOtp3.getText().toString().length() == 0) {
						etOtp2.requestFocus();
					}
				}
				return false;
			}
		});
		etOtp4.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_DEL) {
					if (etOtp4.getText().toString().length() == 0) {
						etOtp3.requestFocus();
					}
				}
				return false;
			}
		});
		etOtp5.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_DEL) {
					if (etOtp5.getText().toString().length() == 0) {
						etOtp4.requestFocus();
					}
				}
				return false;
			}
		});
		
	}
	
	public void hideSoftKeyboard(EditText editText) {
		if (editText == null)
			return;
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	private void initViews() {
		
		ivNext = (ImageView) loginView.findViewById(R.id.ivNext);
		tvStatement = (TextView) loginView.findViewById(R.id.tvStatement);
		ivNextSignup = (ImageView) signupView.findViewById(R.id.ivNextSignup);
		llAgentCode = (LinearLayout) signupView.findViewById(R.id.llAgentCode);
		checkBox = (CheckBox) signupView.findViewById(R.id.checkBox);
		et_number = (EditText) findViewById(R.id.et_number);
		etOtp1 = (EditText) otpView.findViewById(R.id.etOtp1);
		etOtp2 = (EditText) otpView.findViewById(R.id.etOtp2);
		etOtp3 = (EditText) otpView.findViewById(R.id.etOtp3);
		etOtp4 = (EditText) otpView.findViewById(R.id.etOtp4);
		etOtp5 = (EditText) otpView.findViewById(R.id.etOtp5);
		etOtp6 = (EditText) otpView.findViewById(R.id.pin_hidden_edittext);
		tvResend = (TextView) otpView.findViewById(R.id.tvResend);
		ivNextToOTP = (ImageView) otpView.findViewById(R.id.ivNextToOTP);
		
		
		settingSpannable();
		SpannableString ss = new SpannableString("We just sent you the OTP please enter \n OTP above or Resend");
		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				try {
					getOtp();
				} catch (JSONException e) {
					Log.e(TAG, e.getMessage(), e);
				}
				//Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(true);
				ds.setColor(getResources().getColor(R.color.colorWhite));
			}
		};
		ss.setSpan(clickableSpan, 53, 59, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvResend.setText(ss);
		tvResend.setMovementMethod(LinkMovementMethod.getInstance());
		tvResend.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
		tvResend.setHighlightColor(getResources().getColor(R.color.colorTransparent));
		
	}
	
	private void settingSpannable() {
		
		SpannableString ssPhone = new SpannableString(getResources().getString(R.string.CreateStatement));
		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(true);
				ds.setColor(getResources().getColor(R.color.colorWhite));
			}
		};
		ssPhone.setSpan(clickableSpan, 23, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvStatement.setText(ssPhone);
		tvStatement.setMovementMethod(LinkMovementMethod.getInstance());
		tvStatement.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
		tvStatement.setHighlightColor(getResources().getColor(R.color.colorTransparent));
		
	}
	
	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		
	}
	
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	
}
