package com.app.buy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.app.buy.Model.PharmacyModel;
import com.app.buy.R;
import com.app.buy.UI.PharmacyDetailsActivity;
import com.fxn.stash.Stash;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.GalleryPhotosViewHolder> {

    List<PharmacyModel> pharmacyModelList;
    ImageView productImage;
    TextView producttitle;
    public static String title_name;
    ConstraintLayout Container;
    Context context;

    public PharmacyAdapter(List<PharmacyModel> pharmacyModelList, Context context) {
        this.pharmacyModelList = pharmacyModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public GalleryPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_item, parent, false);
        return new GalleryPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
        Picasso.get().load(pharmacyModelList.get(position).getImage()).into(productImage);
        producttitle.setText(pharmacyModelList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PharmacyModel currentPharmacyModel = pharmacyModelList.get(position);
                Intent intent = new Intent(context, PharmacyDetailsActivity.class);
                Stash.put("currentPharmacyModel", currentPharmacyModel);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return pharmacyModelList.size();
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {


        public GalleryPhotosViewHolder(@NonNull View view) {
            super(view);
            Container = view.findViewById(R.id.MainProductID);
            productImage = view.findViewById(R.id.item_image);
            producttitle = view.findViewById(R.id.item_title);

        }
    }
}
