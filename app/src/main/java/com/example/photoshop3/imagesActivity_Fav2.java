package com.example.photoshop3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class imagesActivity_Fav2 extends AppCompatActivity {



    private static final int PERMISSION_STORAGE_CODE = 100;
    String downloadurl;

    ImageView imageView;
    TextView textView;
    FloatingActionButton floatingActionButton;
    FloatingActionButton sharefloating , unfav;

    DatabaseReference reference , dataRefnew , favref_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images__fav2);


        floatingActionButton = findViewById(R.id.floatingbtn_download);
        unfav = findViewById(R.id.unfav);
        sharefloating=findViewById(R.id.share_floating);
        // reference = FirebaseDatabase.getInstance().getReference().child("All");

       // unfavourite=findViewById(R.id.unfavourite);


        imageView=findViewById(R.id.images_fav2_imageview);
        textView=findViewById(R.id.images_fav2_textview);


        //new   1 ==
        //dataRefnew = FirebaseDatabase.getInstance().getReference().child("Data2");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        final String favuser=user.getUid();
        dataRefnew=FirebaseDatabase.getInstance().getReference().child("favourite2").child(favuser);

        favref_2=FirebaseDatabase.getInstance().getReference().child("favourite2");





        final String dataurl =getIntent().getStringExtra("img_fav_2");
        dataRefnew.child(dataurl).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    //   2   ====
                    String carname = snapshot.child("data2name").getValue().toString();
                    final String imageurl12 = snapshot.child("data2url").getValue().toString();

                    Picasso.get().load(imageurl12).into(imageView);
                    textView.setText(carname);


                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                                    String permission =(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                    requestPermissions(new String[]{permission} , PERMISSION_STORAGE_CODE);
                                }else {
                                    startDownloadingFav2(imageurl12);
                                }
                            }else {
                                startDownloadingFav2(imageurl12);

                            }
                        }
                    });

                    sharefloating.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent myIntent = new Intent(Intent.ACTION_SEND);
                            myIntent.setType("text/plain");
                            String sharebody =imageurl12;
                            String sharesub = imageurl12;
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


  //      FirebaseUser user12 =FirebaseAuth.getInstance().getCurrentUser();
    //    final String currentUserId_2 = user12.getUid();
      //  final String postkey_2 = dataurl;

        unfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favref_2.child(favuser).child(dataurl).removeValue();
                finish();

            }
        });
    }

    private void startDownloadingFav2(String imageurl12) {

        DownloadManager.Request  request= new DownloadManager.Request(Uri.parse(imageurl12));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    startDownloadingFav2(downloadurl);
                    Toast.makeText(this, "Download start ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Permission granted ", Toast.LENGTH_SHORT).show();
                }

            }
        }


    }
}