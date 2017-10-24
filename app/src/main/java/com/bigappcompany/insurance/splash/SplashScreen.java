package com.bigappcompany.insurance.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.activity.HomeScreenActivity;
import com.bigappcompany.insurance.activity.LoginActivity;
import com.bigappcompany.insurance.utility.ClsGeneral;
import com.bigappcompany.insurance.utility.PrefereneceName;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 13 Oct 2017 at 12:34 PM
 */

public class SplashScreen extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.splash);
		Thread timer = new Thread() {
			public void run() {
				try {
					checkNmovetoNextActivity();
					
					sleep(3000);
				} catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					
					
				}
			}
		};
		timer.start();
	}
	
	private void checkNmovetoNextActivity() {
		Boolean a = ClsGeneral.getBoolean(SplashScreen.this, PrefereneceName.USER_EXIST);
		if (ClsGeneral.getBoolean(SplashScreen.this, PrefereneceName.USER_EXIST))
		{
			Intent intent = new Intent(SplashScreen.this, HomeScreenActivity.class);
			startActivity(intent);
			finishAffinity();
		}
		else
		{
			Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
			startActivity(intent);
			finishAffinity();
		}
		
	}
}
