package com.app.buy.UI;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.app.buy.R;

public class FullImageActivity extends Activity
{
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        imageView = (ImageView)findViewById(R.id.imageView);
        String productImage = getIntent().getStringExtra("ProductImage");
        Glide.with(this).load(productImage).into(imageView);
    }
}
