package com.example.groceryshoppingsystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshoppingsystem.Model.favouritesClass;
import com.example.groceryshoppingsystem.R;
import com.example.groceryshoppingsystem.UI.CategoryActivity;
import com.example.groceryshoppingsystem.UI.ProductInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Favourite_Recycler_View extends RecyclerView.Adapter<Favourite_Recycler_View.ViewHolder> {
    private List<favouritesClass> horizontalProductModelList;
Context context;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;

    public Favourite_Recycler_View(List<favouritesClass> horizontalProductModelList, Context context) {
        this.horizontalProductModelList = horizontalProductModelList;
        this.context = context;
    }

    public Favourite_Recycler_View(List<favouritesClass> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;
    }

    @NonNull
    @Override
    public Favourite_Recycler_View.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Favourite_Recycler_View.ViewHolder holder, int position) {
        final favouritesClass horizontalProductModel = horizontalProductModelList.get(position);

        holder.producttitle.setText(horizontalProductModel.getProducttitle());
        holder.productprice.setText(horizontalProductModel.getProductprice());
        Log.d("data", horizontalProductModel.getProductprice());
        Picasso.get().load(horizontalProductModel.getProductimage()).into(holder.productImage);
//        holder.checkBox.setImageResource(R.drawable.ic_baseline_favorite_24);

//        holder.PrContainer.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));
        holder.productImage.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));
        holder.producttitle.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));
        holder.productprice.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));
//        holder.ProductExpiryDate.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));
//        holder.checkBox.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));
//        holder.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth = FirebaseAuth.getInstance();
//                CurrentUser = mAuth.getCurrentUser();
//                UserId = CurrentUser.getUid();
//
//                if(product.getIsFavorite()){
//                    holder.PrFavoriteImage.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);
//                    product.setFavorite(false);
//                    //here delete isFavorite from firebase
//                    DatabaseReference x= FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp").child("favourites").child(UserId);
//                    x.child(product.getProductName()).removeValue();
//                }
//                else{
//                    holder.PrFavoriteImage.setImageResource(R.drawable.ic_baseline_favorite_24);
//                    product.setFavorite(true);
//                    //here save isFavorite in firebase
//                    DatabaseReference x= FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp").child("favourites").child(UserId).child(product.getProductName());
//                    x.child("checked").setValue(true);
//                    x.child("productimage").setValue(product.getProductImage());
//                    x.child("productprice").setValue("PKR "+product.getProductPrice());
//                    x.child("producttitle").setValue(product.getProductName());
//
//                }
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProductInfoActivity.class);
                intent.putExtra("Product Name",horizontalProductModel.getProducttitle());
                intent.putExtra("Product Price",horizontalProductModel.getProductprice());
                intent.putExtra("Product Image",horizontalProductModel.getProductimage());
                intent.putExtra("Product ExpiryDate","horizontalProductModel");
                intent.putExtra("Product IsFavorite",String.valueOf(horizontalProductModel.isChecked()));
                intent.putExtra("Is Offered","no");


//                Intent intent = new Intent(context, ProductInfoActivity.class);
//                intent.putExtra("Product Name",(horizontalProductModel.getProducttitle()));
//                intent.putExtra("Product Price",(horizontalProductModel.getProductprice())+"");
//                intent.putExtra("Product Image",(horizontalProductModel.getProductimage()));
//                intent.putExtra("Product ExpiryDate","ProductNExpiryDate");
//                intent.putExtra("Product IsFavorite","false");
//                intent.putExtra("Is Offered","yes");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalProductModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView producttitle, productprice;


        public ViewHolder(@NonNull View view) {
            super(view);
            productImage = view.findViewById(R.id.PrImage);
            producttitle = view.findViewById(R.id.PrName);
            productprice = view.findViewById(R.id.PrPrice);
        }
    }

}