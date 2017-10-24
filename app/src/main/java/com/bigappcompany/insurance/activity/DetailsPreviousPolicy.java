package com.bigappcompany.insurance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.adapter.DetailsPreviousPolicyAdapter;
import com.bigappcompany.insurance.interfaces.CLickReadMore;
import com.bigappcompany.insurance.model.DetailsPreviousPolicyModel;
import com.bigappcompany.insurance.network.Download_web;
import com.bigappcompany.insurance.utility.ClsGeneral;
import com.bigappcompany.insurance.utility.OnTaskCompleted;
import com.bigappcompany.insurance.utility.PrefereneceName;
import com.bigappcompany.insurance.utility.Utility;
import com.bigappcompany.insurance.utility.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 27 Sep 2017 at 1:34 PM
 */

public class DetailsPreviousPolicy extends AppCompatActivity {
	private RecyclerView rvDetailsPolicy;
	private static final String TAG = "DetailsPreviousPolicy";
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailpreviouspolicy);
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
		    .setDefaultFontPath("fonts/Celias.otf")
		    .setFontAttrId(R.attr.fontPath)
		    .build()
		);
		initWidget();
		getPoilicyList();
	}
	
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}
	
	
	private void initWidget() {
		TextView tvHeading = (TextView) findViewById(R.id.tvHeading);
		rvDetailsPolicy = (RecyclerView) findViewById(R.id.rvDetailsPolicy);
		rvDetailsPolicy.setHasFixedSize(true);
		rvDetailsPolicy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		
		tvHeading.setText(getResources().getString(R.string.DetailPrevious));
		findViewById(R.id.rvClick).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
	
	private void getPoilicyList() {
		final ArrayList<DetailsPreviousPolicyModel> list = new ArrayList<>();
		Utility.showDailog(DetailsPreviousPolicy.this,getResources().getString(R.string.pleasewait));
		Download_web web = new Download_web(DetailsPreviousPolicy.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				if (response.length()>0)
				{
					if (response.length() > 0) {
						Utility.closeDialog();
						try {
							JSONObject object = new JSONObject(response);
							if (object.optString("status").equals("1")) {
								JSONArray array = object.getJSONArray("data");
								for (int i=0;i<array.length();i++)
								{
									JSONObject jo = array.getJSONObject(i);
									list.add(new DetailsPreviousPolicyModel(jo.getString("insurance_provider_id"),jo.getString
									    ("insurance_provider_name")));
								}
								
								setadapter(list);
							}
							if (object.optString("status").equals("0")) {
								Toast.makeText(DetailsPreviousPolicy.this, ""+object.optString("msg"), Toast
								    .LENGTH_SHORT).show();
							}
							if (object.optString("status").equals("-2")) {
								Toast.makeText(DetailsPreviousPolicy.this, ""+object.optString("msg"), Toast.LENGTH_SHORT).show();
								startActivity(new Intent(DetailsPreviousPolicy.this,
								    LoginActivity.class));
								finish();
							}
						} catch (JSONException e) {
							Log.e(TAG, e.getMessage(), e);
						}
					}
				}
			}
		});
		
		web.setReqType("get", ClsGeneral.getPreferences(DetailsPreviousPolicy
		    .this,PrefereneceName.SESSION_TOKEN));
		web.execute(WebServices.GET_POLICY_PROVIDER);
		
		
	}
	
	private void setadapter(final ArrayList<DetailsPreviousPolicyModel> list) {
		DetailsPreviousPolicyAdapter adapter = new DetailsPreviousPolicyAdapter(DetailsPreviousPolicy.this, list,
		    new CLickReadMore() {
			    @Override
			    public void onReadMore(int position) {
				    startActivity(new Intent(DetailsPreviousPolicy.this, PolicyDetails.class).putExtra
					("name", list.get(position).getName()));
			    }
		    });
		
		rvDetailsPolicy.setAdapter(adapter);
	}
}
