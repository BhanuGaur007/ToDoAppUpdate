package com.example.to_do_app_final.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.to_do_app_final.R;
import com.example.to_do_app_final.fragment.FavFragment;
import com.example.to_do_app_final.fragment.MapGoogle;
import com.example.to_do_app_final.fragment.Profile;
import com.example.to_do_app_final.manager.PrefranceManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity  implements OnMapReadyCallback {
    ImageView ivTOp,ivBottom,iv_add;
    AlertDialog.Builder builder;
    private static final int REQUEST_CODE=101;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    Marker currentMarker;
    Toolbar toolbar;
    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();
//    private GoogleMap mMap;
    String value=" ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ivTOp = findViewById(R.id.iv_top);
        ivBottom = findViewById(R.id.iv_bottom);
        iv_add = findViewById(R.id.iv_add);
        builder = new AlertDialog.Builder(this);

        setTitle("GOOGLE MAP");
//        addGoogleMapGragment(new MapGoogle());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_color)));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Animation animationtop = AnimationUtils.loadAnimation(this,R.anim.top_wave);
        ivTOp.setAnimation(animationtop);
        Animation animationBottom = AnimationUtils.loadAnimation(this,R.anim.bottom_wave);
        ivBottom.setAnimation(animationBottom);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_main);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent intent = new Intent(MapActivity.this, NameOfNotesHolder.class);
                        startActivity(intent);

//                        addFragment(new HomeFragment());
//                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.action_profile:
                       Intent profileIntent = new Intent(MapActivity.this,Profile.class);
                       startActivity(profileIntent);
//                        Intent intent = new Intent(MainActivity.this, Login.class);
//                        startActivity(intent);
                        break;
                    case R.id.action_fav:
                        Intent favIntent = new Intent(MapActivity.this,FavFragment.class);
                        Toast.makeText(MapActivity.this, "Fav", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });


//        for map


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

//        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
//                .findFragmentById(R.id.mapActiveFragment);
//        mapFragment.getMapAsync(this);
    }

    private void fetchLastLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location !=null){
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(),currentLocation.getLatitude()
                    +""+currentLocation.getLongitude(),Toast.LENGTH_LONG).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapActiveFragment);
                    supportMapFragment.getMapAsync(MapActivity.this);
                }
            }
        });
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOutOption:
                new PrefranceManager(MapActivity.this).logOut();
                Intent intent = new Intent(MapActivity.this, Login.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        final LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        final MarkerOptions markerOptions = new MarkerOptions().position(latLng).draggable(true).title("Your Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        googleMap.addMarker(markerOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18f));
        markerOptions.draggable(true);


        Geocoder geocoder = new Geocoder(MapActivity.this);
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if (addressList !=null && addressList.size()>0){
                String locality  = addressList.get(0).getAddressLine(0);
              String  country = addressList.get(0).getCountryName();
              if (!locality.isEmpty() && !country.isEmpty()){
                  Log.i("address",locality+""+country);
              }
            }
        }catch (IOException e){
            e.printStackTrace();
        }


//        mMap = googleMap;
//
//
//        LatLng sydney = new LatLng(28.63196909100572, 77.32800143812293);
//        mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,18f));


    }

//
//    private void addGoogleMapGragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Bundle bundle=new Bundle();
//        bundle.putString("bundle",value);
//        fragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.map_activity_fragment_add, fragment);
//        fragmentTransaction.commit();
//    }
}