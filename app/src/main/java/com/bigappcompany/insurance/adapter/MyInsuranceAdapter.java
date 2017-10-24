package com.bigappcompany.insurance.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.activity.InsuranceDetailsActivity;
import com.bigappcompany.insurance.model.MyInsuracePojo;

import java.util.ArrayList;


public class MyInsuranceAdapter extends RecyclerView.Adapter<MyInsuranceAdapter.ViewHolder> {
	
	private Context context;
	private ArrayList<MyInsuracePojo> updates;
	
	public MyInsuranceAdapter(Context context, ArrayList<MyInsuracePojo> updates) {
		this.context = context;
		this.updates = updates;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myinsurance_item,
		    parent, false);
		
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

//        holder.tvType.setText(this.updates.get(position).getDate());
//        holder.timeTextview.setText(this.videos.get(position).getTime());
//
//        Glide.with(context)
//                .load(this.videos.get(position).getImage())
//                .error(R.mipmap.ic_launcher)
//                .into(holder.imageView);
		
		holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				Intent intent = new Intent(context, InsuranceDetailsActivity.class);
				context.startActivity(intent);
			}
		});
		
	}
	
	@Override
	public int getItemCount() {
		return updates.size();
		
	}
	
	public class ViewHolder extends RecyclerView.ViewHolder {
		
		private Button btnViewDetails;
		private ImageView imageView;
		private TextView tvType;
		private TextView timeTextview;
		private TextView descriptionTextview;
		
		public ViewHolder(View itemView) {
			super(itemView);
//			tvType =itemView.findViewById(R.id.tvType);
			
			btnViewDetails = (Button) itemView.findViewById(R.id.btnViewDetails);
			
		}
	}
}
