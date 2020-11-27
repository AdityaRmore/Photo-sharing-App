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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewActivity_wallpaper extends AppCompatActivity {


    private static final int PERMISSION_STORAGE_CODE = 100;
    String downloadurl;

    ImageView imageView;
    TextView textView;
    FloatingActionButton floatingActionButton , sharefloating;

    DatabaseReference reference , dataRefnew;
    FirebaseStorage firebaseStorage ;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallpaper);






        floatingActionButton = findViewById(R.id.floatingbtn_download);
        sharefloating=findViewById(R.id.share_floating);
       // reference = FirebaseDatabase.getInstance().getReference().child("All"); //1
        //new
        dataRefnew = FirebaseDatabase.getInstance().getReference().child("Wallpaper");

        imageView=findViewById(R.id.viewActivity_Image_wallpaper);//viewActivity_Image_wallpaper
        textView=findViewById(R.id.viewActivity_Textview_wallapaper);//ViewActiity_Textview_wallpaper

        String View_Activity_people= getIntent().getStringExtra("ViewActivity_Wallpaper");
        dataRefnew.child(View_Activity_people).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String carname = snapshot.child("imgwall").getValue().toString();
                    final String imageurl = snapshot.child("imageurlwall").getValue().toString();

                    //for download
                    Picasso.get().load(imageurl).into(imageView);
                    textView.setText(carname);
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
}