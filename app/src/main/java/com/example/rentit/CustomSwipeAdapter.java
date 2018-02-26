package com.example.rentit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.security.PrivateKey;
import java.util.List;

/**
 * Created by aashish on 9/2/18.
 */

public class CustomSwipeAdapter extends PagerAdapter {

    private Context ctx;
    private LayoutInflater layoutInflater;
    private List<Uri> uri;
    private int[] images={R.drawable.logo,R.drawable.googleg_color};
    public CustomSwipeAdapter(Context context){
        this.ctx=context;

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view=layoutInflater.inflate(R.layout.swipe_layout,container,false);
        ImageView imageView=item_view.findViewById(R.id.image_view);
        //imageView.setImageURI(uri.get(position));
        imageView.setImageResource(images[position]);
        container.addView(item_view);

        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
