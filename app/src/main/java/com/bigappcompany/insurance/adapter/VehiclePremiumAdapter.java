package com.bigappcompany.insurance.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.activity.ClickPosition;
import com.bigappcompany.insurance.model.VehiclePremiumModel;

import java.util.ArrayList;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 21 Oct 2017 at 3:58 PM
 */

public class VehiclePremiumAdapter extends RecyclerView.Adapter<VehiclePremiumAdapter.ViewHolder> {
	private Context context;
	private ArrayList<VehiclePremiumModel> updates;
	private int lastSelectedPosition = 0;
	private ClickPosition clickPosition;
	
	public VehiclePremiumAdapter(Context context, ArrayList<VehiclePremiumModel> updates, ClickPosition clickPosition ) {
		this.context = context;
		this.updates = updates;
		this.clickPosition = clickPosition;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_rimium,
		    parent, false);
		
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {

//        holder.tvType.setText(this.updates.get(position).getDate());
//        holder.timeTextview.setText(this.videos.get(position).getTime());
		holder.thirdparty.setChecked(lastSelectedPosition == position);
		holder.premium_amount.setText("RS. " + updates.get(position).getPolicy_final_premium_price());
		
		holder.thirdparty.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				lastSelectedPosition = position;
				notifyDataSetChanged();
				
				clickPosition.pos(position);
			}
		});
		
	}
	
	@Override
	public int getItemCount() {
		return updates.size();
		
	}
	
	public class ViewHolder extends RecyclerView.ViewHolder {
		
		private RadioButton thirdparty;
		private TextView premium_amount;
		
		public ViewHolder(View itemView) {
			super(itemView);
//			tvType =itemView.findViewById(R.id.tvType);
			
			thirdparty = ((AppCompatRadioButton) itemView.findViewById(R.id.thirdparty));
			premium_amount = ((TextView) itemView.findViewById(R.id.premium_amount));
			
			
		}
	}
}
