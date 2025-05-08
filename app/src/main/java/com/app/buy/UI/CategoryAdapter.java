package com.app.buy.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.buy.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<String> categoryList;
    private String selectedCategory = "ALL"; // Default category
    private final OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategorySelected(String category);
    }

    public CategoryAdapter(List<String> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String category = categoryList.get(position);
        holder.categoryName.setText(category);

        // Highlight selected category
        if (category.equals(selectedCategory)) {
            holder.categoryName.setBackgroundResource(R.drawable.selected_category_background);
        } else {
            holder.categoryName.setBackgroundResource(R.drawable.category_background);
        }

        holder.itemView.setOnClickListener(v -> {
            selectedCategory = category;
            listener.onCategorySelected(category);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
}
