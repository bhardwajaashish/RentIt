package com.example.rentit;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayRents extends AppCompatActivity implements LocationListener{

    MapView mMapView;
    private GoogleMap googleMap;
    LocationManager locationManager;
    String provider_info;
    Context context=DisplayRents.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_rents);
        mMapView = (MapView)findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(DisplayRents.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(DisplayRents.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DisplayRents.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DisplayRents.this,
                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                            123);
                    return;
                }
                googleMap.setMyLocationEnabled(true);


                locationManager=(LocationManager) context.getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();


                Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

                //CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(15).build();
                //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference rentsReference=databaseReference.child("Rents");

        rentsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<List> list=new ArrayList<>();
                for(DataSnapshot rentSnapshot: dataSnapshot.getChildren()){
                    List<String> item =new ArrayList<>();
                    Rent rents=rentSnapshot.getValue(Rent.class);
                    item.add(rents.getCity());
                    item.add(rents.getAddress());
                    item.add(rents.getDescription());
                    item.add(rents.getPhone());
                    item.add(rentSnapshot.getKey());
                    list.add(item);
                }
                CustomListView customListView=new CustomListView(DisplayRents.this,list);
                ListView scroll=(ListView)findViewById(R.id.scroll);
                scroll.setAdapter(customListView);
                scroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(DisplayRents.this,PreviewActivity.class);
                        TextView text=(TextView)view.findViewById(R.id.hidden);
                        intent.putExtra("key",text.getText().toString());
                        context.startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public class CustomListView extends ArrayAdapter<List> {
        private Activity context;
        List<List> list;

        public CustomListView(Activity context,List<List> list) {
            super(context, R.layout.list_item, list);
            this.context=context;
            this.list=list;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View listViewItem=inflater.inflate(R.layout.list_item,null,true);
            TextView textView1=(TextView)listViewItem.findViewById(R.id.textView1);
            TextView textView2=(TextView)listViewItem.findViewById(R.id.textView2);
            TextView textView3=(TextView)listViewItem.findViewById(R.id.textView3);
            TextView textView4=(TextView)listViewItem.findViewById(R.id.textView4);
            TextView hidden=(TextView)listViewItem.findViewById(R.id.hidden);
            List<String> item=list.get(position);
            textView1.setText(item.get(2).toString());
            textView2.setText(item.get(1).toString());
            textView3.setText(item.get(0).toString());
            textView4.setText(item.get(3).toString());
            hidden.setText(item.get(4).toString());
            return listViewItem;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Cannot show the current location until permission is not granted", Toast.LENGTH_SHORT).show();

                } else {
                    LatLng uiet = new LatLng(30.7478078, 76.7571624);
                    googleMap.addMarker(new MarkerOptions().position(uiet).title("Uiet Chandigarh").snippet("Location of node 1"));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(uiet).zoom(15).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                return;
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
