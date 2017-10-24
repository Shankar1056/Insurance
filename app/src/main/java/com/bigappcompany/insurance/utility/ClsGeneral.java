package com.bigappcompany.insurance.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static com.bigappcompany.insurance.utility.PrefereneceName.APP_PREF;

public class ClsGeneral {
	@SuppressLint("StaticFieldLeak")
	private static Context mContext;



	public static void setPreferences(Context context, String key, String value) {
		mContext = context;
		SharedPreferences.Editor editor = mContext.getSharedPreferences(
				APP_PREF, Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
		
	}

	public static String getPreferences(Context context, String key) {
		mContext = context;
		SharedPreferences prefs = mContext.getSharedPreferences(APP_PREF,
				Context.MODE_PRIVATE);
		return  prefs.getString(key, "");
		
	}
	
	public static void setBoolean(Context context, String key, Boolean value) {
		context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE).edit().putBoolean(key, value)
		    .commit();
	}
	
	
	public static Boolean getBoolean(Context context,String key) {
		return context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE).getBoolean(key, false);
	}
	public static void setInt(Context context, String key, int Values) {
		context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE).edit().putInt(key, Values).commit();
	}
	
	public static int getInt(Context context, String key) {
		return context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE).getInt(key, 0);
	}

	public static void clearPreference(Context context)
	{
		SharedPreferences preferences = context.getSharedPreferences("WED_APP", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}



	

}
