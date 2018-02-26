package com.example.rentit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class PreviewActivity extends AppCompatActivity {

    ViewPager viewPager;
    CustomSwipeAdapter customSwipeAdapter;
    FirebaseStorage storage;
    List<Uri> uri_list=new ArrayList<>();
    TextView description,address,city,price;
    ImageView image90;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        storage=FirebaseStorage.getInstance();
        viewPager=(ViewPager)findViewById(R.id.view_pager);
        description=(TextView)findViewById(R.id.description_preview);
        address=(TextView)findViewById(R.id.address_preview);
        city=(TextView)findViewById(R.id.city_preview);
        price=(TextView)findViewById(R.id.price_preview);
        //image90=(ImageView)findViewById(R.id.image90);

        Bundle extras=getIntent().getExtras();
        String key=extras.getString("key");
        //Toast.makeText(this, key, Toast.LENGTH_SHORT).show();

        StorageReference storageReference=storage.getReference();
        storageReference.child(key+"//image1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri1) {
                //Toast.makeText(PreviewActivity.this, uri1.toString(), Toast.LENGTH_SHORT).show();
                //image90.setImageURI("https://cache.carlsonhotels.com/galleries/radblu/photos/webextra/oulzh/superior-room_gallery/room-1_1280x960.jpg");

            }
        });
        storageReference.child(key+"//image2.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri2) {
                uri_list.add(uri2);
            }
        });

        customSwipeAdapter=new CustomSwipeAdapter(PreviewActivity.this);
        viewPager.setAdapter(customSwipeAdapter);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference rentsReference=databaseReference.child("Rents");
        rentsReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Rent rent=dataSnapshot.getValue(Rent.class);
                description.setText(rent.getDescription());
                address.setText(rent.getAddress());
                city.setText(rent.getCity());
                price.setText(rent.getPhone());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+price.getText().toString()));
                startActivity(i);
            }
        });

    }
}
