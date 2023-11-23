package com.example.groceryshoppingsystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshoppingsystem.Model.HorizontalProductModel;
import com.example.groceryshoppingsystem.Model.favouritesClass;
import com.example.groceryshoppingsystem.R;
import com.example.groceryshoppingsystem.UI.ProductInfoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridproductAdapter extends RecyclerView.Adapter<GridproductAdapter.GalleryPhotosViewHolder> {

    List<HorizontalProductModel> horizontalProductModelList;
    ImageView productImage;
    TextView producttitle, productprice;
    ImageView checkBox;
    List<favouritesClass> favourites;
    CardView Container;
    Context context;

    public GridproductAdapter(List<HorizontalProductModel> horizontalProductModelList, List<favouritesClass> favourites, Context context) {
        this.horizontalProductModelList = horizontalProductModelList;
        this.favourites = favourites;
        this.context = context;
    }
    public void filterList(ArrayList<HorizontalProductModel> filterlist) {

        horizontalProductModelList = filterlist;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public GalleryPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.horizontal_item, parent, false);
        return new GalleryPhotosViewHolder(view);
    }

    //    public void filterList(ArrayList<ResturantModel> filterlist) {
//
//        productModels = filterlist;
//        notifyDataSetChanged();
//    }
    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
        HorizontalProductModel resturantModel = horizontalProductModelList.get(position);
        Picasso.get().load(horizontalProductModelList.get(position).getProductimage()).into(productImage);
        producttitle.setText(horizontalProductModelList.get(position).getProducttitle());
        productprice.setText("Rs. " + horizontalProductModelList.get(position).getProductprice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductInfoActivity.class);
                intent.putExtra("Product Name", horizontalProductModelList.get(position).getProducttitle());
                intent.putExtra("Product Price", horizontalProductModelList.get(position).getProductprice());
                intent.putExtra("Product Image", horizontalProductModelList.get(position).getProductimage());
                intent.putExtra("Product ExpiryDate", horizontalProductModelList.get(position).getdetailsDate());
                intent.putExtra("Product IsFavorite", String.valueOf(horizontalProductModelList.get(position).isChecked()));
                intent.putExtra("Is Offered", "no");

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalProductModelList.size();
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {


        public GalleryPhotosViewHolder(@NonNull View view) {
            super(view);
            Container = view.findViewById(R.id.MainProductID);
            productImage = view.findViewById(R.id.item_image);
            producttitle = view.findViewById(R.id.item_title);
            productprice = view.findViewById(R.id.item_Price);
            checkBox = view.findViewById(R.id.check_box);

        }
    }
}
