package com.bigappcompany.insurance.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigappcompany.insurance.R;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 02 Oct 2017 at 12:55 PM
 */

public class TermsConditions extends Fragment {
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.termscondition,container,false);
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		TextView tvHeading = (TextView)view.findViewById(R.id.tvHeading);
		tvHeading.setText(getResources().getString(R.string.termsncondition));
	}
}
