package com.app.buy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.buy.R;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;


import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {

    private List<String> imageUrls;
    private Context context;

    public ImagePagerAdapter(Context context, List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_pager, container, false);
        PhotoView photoView = view.findViewById(R.id.photoView);
        Glide.with(context).load(imageUrls.get(position)).into(photoView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
