package com.bigappcompany.insurance.fragment;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.activity.HomeScreenActivity;
import com.bigappcompany.insurance.adapter.MyInsuranceAdapter;
import com.bigappcompany.insurance.interfaces.ClickListener;
import com.bigappcompany.insurance.model.MyInsuracePojo;
import com.bigappcompany.insurance.other.RecyclerTouchListener;

import java.util.ArrayList;

public class MyInsuranceFragment extends Fragment {
	
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
		rootView = inflater.inflate(R.layout.myinsurance_fragment, null);
		initViews();
		eventListners();
		settingValues();
		return rootView;
	}

	private void settingValues() {

		ArrayList<MyInsuracePojo> date=new ArrayList<MyInsuracePojo>();
		MyInsuracePojo pojo=new MyInsuracePojo();
		pojo.setHeaderName("Green vegetables");
		date.add(pojo);

		pojo=new MyInsuracePojo();
		pojo.setHeaderName("fruits");
		date.add(pojo);

		pojo=new MyInsuracePojo();
		pojo.setHeaderName("seeds");
		date.add(pojo);

		pojo=new MyInsuracePojo();
		pojo.setHeaderName("green vegetables");
		date.add(pojo);

		Log.d("Test","length"+date.size());

		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
//		recyclerView.setAdapter(mAdapter);
		MyInsuranceAdapter adaptor = new MyInsuranceAdapter(getActivity(),date);
		recyclerView.setAdapter(adaptor);
	}

	private void eventListners() {
		
		rvToggle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				DrawerMenuStatus();

				((HomeScreenActivity)getActivity()).openDrawer();
			}
		});


		recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
			@Override
			public void onClick(View view, int position) {

				int itemPosition = recyclerView.indexOfChild(view);


				Toast.makeText(getActivity(), "is selected"+position, Toast.LENGTH_SHORT).show();
//				Intent intent= new Intent(getActivity(), DetailsActivity.class);
//				startActivity(intent);
			}

			@Override
			public void onLongClick(View view, int position) {

			}
		}));
		
		
	}

	
	private void initViews() {

		rvToggle = (RelativeLayout) rootView.findViewById(R.id.rv_click);
		recyclerView= (RecyclerView) rootView.findViewById(R.id.recyclerView);
		TextView tvHeading = (TextView)rootView.findViewById(R.id.tvHeading);
		tvHeading.setText(getResources().getString(R.string.Myinsurance));
	}
	

	
}
