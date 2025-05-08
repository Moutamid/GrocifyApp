package com.app.buy.UI;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.app.buy.Adapters.ImagePagerAdapter;
import com.app.buy.R;

import java.util.List;


public class ImageDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        ViewPager viewPager = findViewById(R.id.viewPager);
        List<String> imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        int position = getIntent().getIntExtra("position", 0);
       ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(this, imageUrls);
        viewPager.setAdapter(imagePagerAdapter);
        viewPager.setCurrentItem(position);
    }
}
