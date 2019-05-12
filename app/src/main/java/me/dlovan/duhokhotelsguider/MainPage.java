package me.dlovan.duhokhotelsguider;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Path;
import android.icu.text.IDNA;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class MainPage extends FragmentActivity implements
                                                    OnMapReadyCallback,
                                                    GoogleMap.OnMarkerClickListener,
                                                    GoogleMap.OnInfoWindowClickListener {
    ProgressBar progressBar;
    public GoogleMap mMap;
    private LatLng latlng;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private String currentDistance, currentDuration;
    private MarkerOptions YourMarker;
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

        progressBar = findViewById(R.id.progressBar1);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        new getHotels().execute();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        requestMultiplePermissions();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {  return; }
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
                hotelDetail.putExtra("Distance",currentDistance);
                hotelDetail.putExtra("Duration",currentDuration);
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

        if (mMap != null) {
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location arg0) {
                        YourMarker = new MarkerOptions();
                        YourMarker.position(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
                }
            });
        }

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
    public boolean onMarkerClick(Marker marker) {

        String serverKey="AIzaSyDZ4SUzFsBLAf-0fp3J6vwHUVmdp6tB5gc";
        GoogleDirection.withServerKey(serverKey)
                .from(YourMarker.getPosition()) //YourMarker.getPosition()
                .to(marker.getPosition())
                .unit(Unit.METRIC)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            String distance = distanceInfo.getText();
                            String duration = durationInfo.getText();
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(getApplicationContext(), directionPositionList, 5, Color.RED);
                            mMap.addPolyline(polylineOptions);
                            currentDistance = distance;
                            currentDuration = duration;
                        }
                        else{
                            Log.d("Direction","error:"+direction.getStatus().toString());
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.d("Direction","Direction Fail");
                        t.getMessage();
                    }
                });
        return false;
    }
    private class getHotels extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
    private void requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    private void openSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
        builder.setTitle("Required Permissions");
        builder.setMessage("This app require permission to use awesome feature. Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

}
