package com.bigappcompany.insurance.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.activity.AddNomineeActivity;
import com.bigappcompany.insurance.adapter.InsurancePlanAdapter;
import com.bigappcompany.insurance.interfaces.CLickReadMore;
import com.bigappcompany.insurance.interfaces.ClickListener;
import com.bigappcompany.insurance.model.MyInsuracePojo;
import com.bigappcompany.insurance.other.RecyclerTouchListener;

import java.util.ArrayList;

public class InsurancePlanFragment extends Fragment implements CLickReadMore{
	
	private View rootView;
	private DrawerArrowDrawable drawerArrowDrawable;
	private float offset;
	private boolean flipped;
	private DrawerLayout drawer;
	private RelativeLayout rvToggle;
	private RecyclerView recyclerView;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.fragment_insurance_plan, null);
		initViews();
		eventListners();
		settingValues();
		return rootView;
	}

	private void settingValues() {

		ArrayList<MyInsuracePojo> date=new ArrayList<MyInsuracePojo>();
		MyInsuracePojo pojo=new MyInsuracePojo();
		pojo.setHeaderName("Compremizing");
		pojo.setIDV("1000");
		pojo.setNCB("20%");
		pojo.setAmount("10000");
		date.add(pojo);

		pojo=new MyInsuracePojo();
		pojo.setHeaderName("Third party");
		pojo.setIDV("2000");
		pojo.setNCB("Nil");
		pojo.setAmount("20000");
		date.add(pojo);


		pojo=new MyInsuracePojo();
		pojo.setHeaderName("Nildip");
		pojo.setIDV("2200");
		pojo.setNCB("20%");
		pojo.setAmount("20000");
		date.add(pojo);

		pojo=new MyInsuracePojo();
		pojo.setHeaderName("Secure");
		pojo.setIDV("2500");
		pojo.setNCB("20%");
		date.add(pojo);

		pojo=new MyInsuracePojo();
		pojo.setHeaderName("Secure plus");
		pojo.setIDV("2800");
		pojo.setAmount("20000");
		pojo.setNCB("20%");
		date.add(pojo);


		Log.d("Test","length"+date.size());

		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
//		recyclerView.setAdapter(mAdapter);
		InsurancePlanAdapter adaptor = new InsurancePlanAdapter(getActivity(),date,this);
		recyclerView.setAdapter(adaptor);
	}

	private void eventListners() {
		
//		rvToggle.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
////				DrawerMenuStatus();
//
//				((HomeScreenActivity)getActivity()).drawerListerns();
//			}
//		});


		recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
			@Override
			public void onClick(View view, int position) {

//				int itemPosition = recyclerView.indexOfChild(view);


				Toast.makeText(getActivity(), "is selected"+position, Toast.LENGTH_SHORT).show();

				Intent intent= new Intent(getActivity(), AddNomineeActivity.class);
				startActivity(intent);
			}

			@Override
			public void onLongClick(View view, int position) {

			}
		}));
		
		
	}

	
	private void initViews() {

//		rvToggle = rootView.findViewById(R.id.rv_click);
		recyclerView=(RecyclerView) rootView.findViewById(R.id.recyclerView);
	}


	@Override
	public void onReadMore(int position) {

		final Dialog dialog = new Dialog(getActivity());
//        Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);

//		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_readmore);
		dialog.setTitle(null);

//		final EditText _forgotEmail = (EditText) dialog.findViewById(R.id.et_forgot_email);
		Button submitButton = (Button) dialog.findViewById(R.id.btnOk);
//
//
		Button dialogButton = (Button) dialog.findViewById(R.id.btnOk);
		dialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}


}
