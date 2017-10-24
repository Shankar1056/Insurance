package com.bigappcompany.insurance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.interfaces.CLickReadMore;
import com.bigappcompany.insurance.model.MyInsuracePojo;

import java.util.ArrayList;


public class InsurancePlanAdapter extends RecyclerView.Adapter<InsurancePlanAdapter.ViewHolder> {

	private Context context;
	private ArrayList<MyInsuracePojo> updates;
	private CLickReadMore cLickReadMore;

	public InsurancePlanAdapter(Context context, ArrayList<MyInsuracePojo> updates,CLickReadMore cLickReadMore) {
		this.context = context;
		this.updates = updates;
		this.cLickReadMore = cLickReadMore;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.insurance_plan_item,
			parent, false);

		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.tvHeaderName.setText(this.updates.get(position).getHeaderName());
		holder.tvIDV.setText(this.updates.get(position).getIDV());
		holder.tvNCB.setText(this.updates.get(position).getNCB());
		holder.btnAmount.setText(this.updates.get(position).getAmount());



		SpannableString ssPhone = new SpannableString(holder.tvDetails.getText().toString());
		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                cLickReadMore.onReadMore(position);
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(false);
				ds.setColor(context.getResources().getColor(R.color.colorOrange));
			}
		};

		ssPhone.setSpan(clickableSpan, 73, 81, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.tvDetails.setText(ssPhone);
		holder.tvDetails.setMovementMethod(LinkMovementMethod.getInstance());
//		holder.tvDetails.setBackgroundColor(context.getResources().getColor(R.color.colorsBlue));
		holder.tvDetails.setHighlightColor(context.getResources().getColor(R.color.colorsBlue));


		holder.btnAmount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {



			}
		});
		
	}
	
	@Override
	public int getItemCount() {
		return updates.size();
		
	}
	
	public class ViewHolder extends RecyclerView.ViewHolder {

		private Button btnAmount;
		private TextView tvHeaderName,tvIDV,tvNCB,tvDetails;
		
		public ViewHolder(View itemView) {
			super(itemView);

			tvNCB =(TextView) itemView.findViewById(R.id.tvNCB);
			tvIDV =(TextView)itemView.findViewById(R.id.tvIDV);
			tvHeaderName =(TextView)itemView.findViewById(R.id.tvHeaderName);
			btnAmount=(Button) itemView.findViewById(R.id.btnAmount);
			tvDetails=(TextView)itemView.findViewById(R.id.tvDetails);

		}
	}
}
