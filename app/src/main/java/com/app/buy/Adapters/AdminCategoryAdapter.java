package com.app.buy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.buy.Model.Product;
import com.app.buy.R;
import com.app.buy.UI.ProductsFragment;
import com.fxn.stash.Stash;

import java.util.Collections;
import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.ProductViewHolder> {

    public Context context;
    private List<Product> adminProducts;
    private AdminCategoryAdapter.onItemClickListener itemListener;
    private AdminCategoryAdapter.onLongClickListener longListener;

    public AdminCategoryAdapter(Context context, List<Product> adminProducts) {
        this.context = context;
        this.adminProducts = adminProducts;
    }

    public void setOnItemClickListener(AdminCategoryAdapter.onItemClickListener listener) {
        itemListener = listener;
    }



    public void addList(List<Product> list) {
        adminProducts.clear();
        Collections.copy(adminProducts, list);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_category, parent, false);
        return new ProductViewHolder(v, itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.name.setText(adminProducts.get(position).getCategory());

    }

    @Override
    public int getItemCount() {
        return adminProducts.size();
    }

    public interface onItemClickListener {
        void onItemClick(int pos);
    }

    public interface onLongClickListener {
        void onItemLongClick(int pos);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ProductViewHolder(@NonNull View itemView, final AdminCategoryAdapter.onItemClickListener itemlistener) {
            super(itemView);
            name = itemView.findViewById(R.id.AdapterProductName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemlistener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            Stash.put("current_category", name.getText().toString());
                            itemlistener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
