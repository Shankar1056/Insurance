package com.bigappcompany.insurance.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bigappcompany.insurance.R;
import com.firebase.digitsmigrationhelpers.AuthMigrator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 16 Oct 2017 at 6:02 PM
 */

public class login extends AppCompatActivity {
	public static final int RC_SIGN_IN = 1;
	private static final String TAG = "LoginActivity";
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firebase_login);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				checksession();
				
			}
		}, 3000);
	}
	
	private void checksession() {
		AuthMigrator.getInstance().migrate(true).addOnSuccessListener(this,
		    new OnSuccessListener() {
			    
			    @Override
			    public void onSuccess(Object o) {
				    FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
				    if (u != null) {
					    // Either a user was already logged in or token exchange succeeded
					    Log.d("MyApp", "Digits id preserved:" + u.getUid());
					    Log.d("MyApp", "Digits phone number preserved: " + u.getPhoneNumber());
					    //  login(u.getPhoneNumber().toString(), "0");
					    /*if (!getSharedPreferences(JsonParser.APP_NAME, MODE_PRIVATE).getBoolean(JsonParser.GO, false)) {
						    login(u.getPhoneNumber().toString());
					    } else {
						    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
						    startActivity(i);
						    finish();
					    }*/
				    } else {
					    // No tokens were found to exchange and no Firebase user logged in.
					    /*startActivityForResult(
						AuthUI.getInstance()
						    .createSignInIntentBuilder()
						    .setProviders(Arrays.asList(
							new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
						    
						    ))
						    .build(),
						RC_SIGN_IN);*/
				    }
			    }
		    }).addOnFailureListener(this,
		    new OnFailureListener() {
			    @Override
			    public void onFailure(@NonNull Exception e) {
				    e.printStackTrace();
				    // Error migrating Digits token
			    }
		    });
		
		
	}
	
	/*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			IdpResponse response = IdpResponse.fromResultIntent(data);
			if (resultCode == ResultCodes.OK) {
				login(response.getPhoneNumber());
			} else {
				if (response == null) {
					Log.e("Login", "Login canceled by User");
				}
				if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
					Log.e("Login", "No Internet Connection");
				}
				if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
					Log.e("Login", "Unknown Error");
				}
			}
			Log.e("Login", "Unknown sign in response");
		}
	}*/
	
	
}
