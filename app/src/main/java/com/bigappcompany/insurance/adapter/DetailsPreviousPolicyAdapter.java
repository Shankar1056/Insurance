package com.bigappcompany.insurance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.interfaces.CLickReadMore;
import com.bigappcompany.insurance.model.DetailsPreviousPolicyModel;

import java.util.ArrayList;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 27 Sep 2017 at 2:11 PM
 */

public class DetailsPreviousPolicyAdapter extends RecyclerView.Adapter<DetailsPreviousPolicyAdapter.ViewHolder> {
	
	private Context context;
	private ArrayList<DetailsPreviousPolicyModel> updates;
	private CLickReadMore cLickReadMore;
	
	public DetailsPreviousPolicyAdapter(Context context, ArrayList<DetailsPreviousPolicyModel> updates,CLickReadMore cLickReadMore) {
		this.context = context;
		this.updates = updates;
		this.cLickReadMore = cLickReadMore;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detailspre_pol_itemrow,
		    parent, false);
		
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		
		holder.tvHeaderName.setText(this.updates.get(position).getName());
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				cLickReadMore.onReadMore(position);
			}
		});
		
	}
	
	@Override
	public int getItemCount() {
		return updates.size();
		
	}
	
	public class ViewHolder extends RecyclerView.ViewHolder {
		
		private TextView tvHeaderName;
		
		public ViewHolder(View itemView) {
			super(itemView);
			
			tvHeaderName =(TextView) itemView.findViewById(R.id.tv_policyname);
			
		}
	}
}
