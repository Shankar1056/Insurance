package com.bigappcompany.insurance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.fragment.HavePolicyFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jacek Kwiecień on 08.11.2016.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private final Context context;
    private final List<File> imagesFiles;
    private final HavePolicyFragment imageAmenities;
    private ArrayList<String> textname;

    public ImagesAdapter(Context context, List<File> imagesFiles, ArrayList<String> textname,HavePolicyFragment imageAmenities) {
        this.context = context;
        this.imagesFiles = imagesFiles;
        this.imageAmenities =imageAmenities;
        this.textname = textname;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_view_image,
            parent, false);
    
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            Picasso.with(context)
                    .load(imagesFiles.get(position))
                    .fit()
                    .centerCrop()
                    .into(holder.image_view);
       


        holder.crossbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAmenities.delete(position,textname.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesFiles.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image_view, crossbutton;
       

        public ViewHolder(View itemView) {
            super(itemView);
            image_view = (ImageView)itemView.findViewById(R.id.image_view);
            crossbutton = (ImageView) itemView.findViewById(R.id.crossbutton);
        }


    }
    
    
}
