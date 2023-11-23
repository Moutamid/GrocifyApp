package com.example.groceryshoppingsystem.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshoppingsystem.Model.CategoryModel;
import com.example.groceryshoppingsystem.R;
import com.squareup.picasso.Picasso;
import java.util.Collections;
import java.util.List;

public class AdminCatogryAdapter extends RecyclerView.Adapter<AdminCatogryAdapter.CatogryViewHolder> {
    private Context context;
    private List<CategoryModel> CatogryList;
    private AdminOfferAdapter.onItemClickListener itemListener;
    private AdminOfferAdapter.onLongClickListener longListener;

    public interface onItemClickListener{
        void onItemClick(int pos);
    }
    public interface onLongClickListener{
        void onItemLongClick(int pos);
    }

    public void setOnItemClickListener(AdminOfferAdapter.onItemClickListener listener)
    {
        itemListener = listener;
    }

    public void setOnLongClickListener(AdminOfferAdapter.onLongClickListener listener)
    {
        longListener = listener;
    }

    public AdminCatogryAdapter(Context context, List<CategoryModel> CatogryList) {
        this.context = context;
        this.CatogryList = CatogryList;
    }

    public void addList(List<CategoryModel> list)
    {
        CatogryList.clear();
        Collections.copy(CatogryList , list);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CatogryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_salesman_list , parent , false);
        return new CatogryViewHolder(v , itemListener , longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CatogryViewHolder holder, int position) {

        holder.img.setAnimation(AnimationUtils.loadAnimation(context , R.anim.fade_transition_animation));
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context , R.anim.fade_scale_animation));
        Picasso.get().load(CatogryList.get(position).getImage()).centerCrop().fit().into(holder.img);
        holder.name.setText(CatogryList.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return CatogryList.size();
    }

    public static class CatogryViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name ;
        CardView cardView;
        public CatogryViewHolder(@NonNull View itemView, final AdminOfferAdapter.onItemClickListener itemlistener , final AdminOfferAdapter.onLongClickListener longClickListener) {
            super(itemView);

            img = itemView.findViewById(R.id.adapterCatogryImage);
            name = itemView.findViewById(R.id.AdapterCatogryName);
            cardView = itemView.findViewById(R.id.CatogryCardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemlistener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            itemlistener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(longClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                            longClickListener.onItemLongClick(position);
                    }
                    return false;
                }
            });
        }
    }
}
