package me.dlovan.duhokhotelsguider;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.List;

public class MainPage extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
    ProgressBar progressBar;
    public GoogleMap mMap;
    private LatLng latlng;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private FloatingActionButton btnDirection;
    public List<Hotels> listHotels;
    JSONArray jsonOb;
    ArrayList<String> images;

    public static String ServiceURL = "https://dlovan.000webhostapp.com/apps/FHOGM/hotels.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_page);
            listHotels = new ArrayList<>();
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            btnDirection = findViewById(R.id.DirectionButtonID);
            progressBar = findViewById(R.id.progressBar1);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    new getHotels().execute();
    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        //getPermisions();
        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Integer M = Integer.valueOf(marker.getId().substring(1));
               // Toast.makeText(getApplicationContext(),"ID: "+M,Toast.LENGTH_LONG).show();
                Hotels Hs = listHotels.get(M);
                Intent hotelDetail = new Intent(MainPage.this, HotelDetail.class);
                hotelDetail.putExtra("ID",M);
                hotelDetail.putExtra("Name",Hs.getName());
                hotelDetail.putExtra("Address",Hs.getAddress());
                hotelDetail.putExtra("description",Hs.getDesc());
                hotelDetail.putExtra("Phone",Hs.getPhone());
                hotelDetail.putStringArrayListExtra("images",Hs.Images);
                startActivity(hotelDetail);

            }
        });

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_jason));
        mMap.setOnMarkerClickListener(this);

        //initializing center of map
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.864863, 42.990112), 13));
        //on location change
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                latlng = new LatLng(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                mMap.addCircle(new CircleOptions()
                        .center(latlng)
                        .strokeColor(getResources().getColor(R.color.colorAccent))
                        .strokeWidth(3)
                        .radius(100)
                );
            }

            @Override
            public void onProviderDisabled(String provider) {}
        };

        getPermisions();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, locationListener);
        }
    }
    @Override
    public void onInfoWindowClick(Marker marker) { }
    @Override
    public boolean onMarkerClick(Marker marker) { return false;}
    private class getHotels extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(MainPage.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            try {
                String jsonStr = sh.makeServiceCall(ServiceURL);
                jsonOb = new JSONArray(jsonStr);
                for (int i = 0; i < jsonOb.length(); i++) {
                    Hotels Hs = new Hotels();
                    Hs.setID(jsonOb.getJSONObject(i).getInt("id"));
                    Hs.setName(jsonOb.getJSONObject(i).getString("name"));
                    Hs.setAddress(jsonOb.getJSONObject(i).getString("address"));
                    Hs.setLat(jsonOb.getJSONObject(i).getDouble("lat"));
                    Hs.setLng(jsonOb.getJSONObject(i).getDouble("lng"));
                    Hs.setPhone(jsonOb.getJSONObject(i).getString("phone"));
                    Hs.setDesc(jsonOb.getJSONObject(i).getString("description"));
                    JSONArray jsonImages = jsonOb.getJSONObject(i).getJSONArray("images");
                    images = new ArrayList<>();
                      for(int j=0;j<jsonImages.length();j++) {
                          images.add(jsonImages.get(j).toString());
                      }
                    Hs.setImages(images);
                    listHotels.add(i, Hs);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            for (Hotels h : listHotels) {
                Marker m = mMap.addMarker(new MarkerOptions()
                        .title(h.getName())
                        .position(new LatLng(h.getLat(), h.getLng()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                m.setTag(h.getID());
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void getPermisions(){
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
                //Ask for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                // we have permission!
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

}
