package com.example.photoshop3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewActivity extends AppCompatActivity {

    private static final int PERMISSION_STORAGE_CODE = 100;
    String downloadurl;

    ImageView imageView;
    TextView textView;
    FloatingActionButton floatingActionButton;
    FloatingActionButton sharefloating;


    DatabaseReference reference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        imageView=findViewById(R.id.viewActivity_Image);
        textView=findViewById(R.id.ViewActiity_Textview);

        sharefloating=findViewById(R.id.share_floating);

        floatingActionButton = findViewById(R.id.floatingbtn_download);
        reference = FirebaseDatabase.getInstance().getReference().child("Data");

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();


        String carkey= getIntent().getStringExtra("carkey");
        reference.child(carkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    // 1  String carname = snapshot.child("CarName").getValue().toString();
                    final String imageurl = snapshot.child("imageurl").getValue().toString();

                    //for download
                    Picasso.get().load(imageurl).into(imageView);
                   // 2  textView.setText(carname);
                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                                    String permission =(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                    requestPermissions(new String[]{permission} , PERMISSION_STORAGE_CODE);
                                }else {
                                    startDownloading(imageurl);
                                }
                            }else {
                                startDownloading(imageurl);

                            }

                        }
                    });

                    //share
                    sharefloating.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent myIntent = new Intent(Intent.ACTION_SEND);
                            myIntent.setType("text/plain");
                            String sharebody =imageurl;
                            String sharesub = imageurl;
                            myIntent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
                            myIntent.putExtra(Intent.EXTRA_TEXT,sharebody);
                            startActivity(Intent.createChooser(myIntent,"Share Using"));

                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }



    //for downloading image
    private void startDownloading(String imageurl) {

        DownloadManager.Request  request= new DownloadManager.Request(Uri.parse(imageurl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |  DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("download");
        request.setDescription("Downloding image ....");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //new method try
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS,""+ System.currentTimeMillis());
        }else {
        }
        DownloadManager manager =(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        Toast.makeText(this, "Download start ", Toast.LENGTH_SHORT).show();


    }

    //for downloading image
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    startDownloading(downloadurl);
                    Toast.makeText(this, "Download start ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Permission granted ", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
    ///download imag


}

