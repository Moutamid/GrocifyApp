package com.app.buy.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.buy.Model.Medicine;
import com.app.buy.R;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;

import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    private Context context;
    private List<Medicine> medicineList;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.name.setText(medicine.getName());
        if(medicine.getPrice().equals("0"))
        {
            holder.price.setText("GNF" + medicine.getPriceGNF());

        }
        else
        {
            holder.price.setText("CFA " + medicine.getPrice());

        }
        Glide.with(context).load(medicine.getImage()).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductInfoActivity.class);
                Stash.put("current_medicine", medicine);
                context.startActivity(intent);

            }
        });    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
//        , category, , quantity;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
//            category = itemView.findViewById(R.id.txtCategory);
            price = itemView.findViewById(R.id.txtPrice);
//            quantity = itemView.findViewById(R.id.txtQuantity);
            image = itemView.findViewById(R.id.imgMedicine);
        }
    }
    public void updateList(List<Medicine> newList) {
        this.medicineList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

}
