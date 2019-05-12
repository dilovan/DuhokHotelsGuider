package me.dlovan.duhokhotelsguider;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class HotelDetail extends AppCompatActivity {
    private final int CALL_REQUEST = 100;
    TextView txtName;
    TextView txtAddress;
    TextView txtPhone;
    TextView txtDesc;
    TextView txtDirection,txtDuration;
    ScrollView scrollView;
    ImageView iv;
    ImageView im1,im2,im3,im4;
    FloatingActionButton back;
    FloatingActionButton call;
    Bundle extras;
    String imagePath;
    String Name;
    String Address;
    String Phone;
    String Description;
    String Distance,Duration;
    Integer ID;
    ArrayList<String> images;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        images = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar1);
        extras = getIntent().getExtras();
        if(extras !=null){
            imagePath = extras.getString("Image");
            ID        = extras.getInt("ID");
            Name      = extras.getString("Name");
            Address   = extras.getString("Address");
            Phone     = extras.getString("Phone");
            Description = extras.getString("description");
             Distance = extras.getString("Distance");
             Duration = extras.getString("Duration");
            images.addAll(extras.getStringArrayList("images"));
        }

        Log.e("IMGS",images.toString());

        txtName    = findViewById(R.id.textViewName);
        txtPhone   = findViewById(R.id.textViewPhone);
        txtAddress = findViewById(R.id.textViewAddress);
        txtDesc    = findViewById(R.id.textViewDesc);
        scrollView = findViewById(R.id.scrollView1);
        iv         = findViewById(R.id.imageView2);
        txtDirection = findViewById(R.id.textViewDirection);
        txtDuration = findViewById(R.id.textViewDuration);

        call       = findViewById(R.id.CallButtonID);
        im1  = findViewById(R.id.img1);
        im2  = findViewById(R.id.img2);
        im3  = findViewById(R.id.img3);
        im4  = findViewById(R.id.img4);
        ImageView[] imgs = {im1,im2,im3,im4};


        Glide.with(iv)
                .load(images.get(0))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .centerCrop()
                //.placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(iv);

        for(int i=0;i<imgs.length;i++) {
            Glide.with(imgs[i])
                    .load(images.get(i+1))
                    .centerCrop()
                    //.placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imgs[i]);
        }
        txtName.setText(Name);
        txtAddress.setText(Address);
        txtPhone.setText(Phone);
        txtDesc.setText(Description);
        txtDuration.setText(Duration);
        txtDirection.setText(Distance);

            im1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scrollView.smoothScrollTo(0, 0);
                    Glide.with(iv)
                            .load(images.get(1))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .centerCrop()
                            //.placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(iv);
                }
            });

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0, 0);
                Glide.with(iv)
                        .load(images.get(2))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .centerCrop()
                        //.placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(iv);
            }
        });
        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0, 0);
                Glide.with(iv)
                        .load(images.get(3))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .centerCrop()
                        //.placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(iv);
            }
        });
        im4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0, 0);
                Glide.with(iv)
                        .load(images.get(4))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .centerCrop()
                        //.placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(iv);
            }
        });
        back = findViewById(R.id.BackButtonID);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HotelDetail.this,MainPage.class));
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneNumber();
            }
        });
    }
    public void callPhoneNumber() {
        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HotelDetail.this, new String[]{android.Manifest.permission.CALL_PHONE}, CALL_REQUEST);
                    return;
                }
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + Phone));
            startActivity(callIntent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        if(requestCode == CALL_REQUEST)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber();
            }
            else
            {
                Toast.makeText(HotelDetail.this, getResources().getString(R.string.call_permission_denied_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
