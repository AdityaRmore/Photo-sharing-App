package com.example.photoshop3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class HomeActivity2 extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 101;
    private ImageView imageviewadd;
    private EditText inputimagename;
    private TextView textviewprogress;
    private ProgressBar progressBar;

    Uri imageuri;
    boolean isImageAdded=false;
    
    Uri imageurlwall;
    boolean IsImageAddedWall=false;

    Uri imageuri2row;
    boolean IsImageAdded2Row = false;

    DatabaseReference dataRef , dataRef_wallpaper        , dataRef_2Row;
    StorageReference storageRef , storageref_wallpaper   ,  dataRef_2Row_s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);


        imageviewadd=findViewById(R.id.imageViewadd);
        inputimagename=findViewById(R.id.inputimagename);
        textviewprogress=findViewById(R.id.textviewprogress);
        progressBar=findViewById(R.id.progressbar);
        Button btnupload = findViewById(R.id.btnupload);
        Button btnuploadWall = findViewById(R.id.btnupload_wallpaper);
        Button btnipload2Row =findViewById(R.id.btnupload_2_row);


        textviewprogress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        dataRef= FirebaseDatabase.getInstance().getReference().child("Data");
        dataRef_wallpaper=FirebaseDatabase.getInstance().getReference().child("Wallpaper");
        dataRef_2Row=FirebaseDatabase.getInstance().getReference().child("Data2");

        //also called  CAR
        storageRef= FirebaseStorage.getInstance().getReference().child("images");
        storageref_wallpaper= FirebaseStorage.getInstance().getReference().child("wallpaper");
        dataRef_2Row_s=FirebaseStorage.getInstance().getReference().child("Data2_S");

        imageviewadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String imagename =inputimagename.getText().toString();
                if (isImageAdded!=false && imagename!= null){
                    uploadImage(imagename);
                }
            }
        });
        //new
        btnuploadWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String imagenamewall = inputimagename.getText().toString();
                if ((IsImageAddedWall != false) && imagenamewall!= null){
                    uploadImageWall(imagenamewall);
                }
            }
        });
        btnipload2Row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity2.this,"Image is uploading please Wait",Toast.LENGTH_SHORT).show();
                final String imagename2row =inputimagename.getText().toString();
                if (IsImageAdded2Row!= false && imagename2row != null){
                    uploadImage2Row(imagename2row);
                }
            }
        });


        
    }

    private void uploadImage2Row(final String imagename2row) {
        //   1  key    generate new
        //   2  data base refrence
        //   3  storage ref for storing images
        //   4  Uri in put file
        //   5  add new hashmap


        textviewprogress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        final String key2Row=dataRef_2Row.push().getKey();
        dataRef_2Row_s.child(key2Row+".jpg").putFile(imageuri2row).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dataRef_2Row_s.child(key2Row+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap=new HashMap();
                        hashMap.put("data2name", imagename2row);
                        hashMap.put("data2url", uri.toString());
                        dataRef_2Row.child(key2Row).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(),imageViewActivity.class));
                            }
                        });
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress= (taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                textviewprogress.setText(progress+" %");
            }
        });


    }

    //new
    private void uploadImageWall(final String imagenamewall) {
        textviewprogress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        final String keywall=dataRef_wallpaper.push().getKey();
        storageref_wallpaper.child(keywall+".jpg").putFile(imageurlwall).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageref_wallpaper.child(keywall+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap=new HashMap();
                        hashMap.put("imgwall", imagenamewall);
                        hashMap.put("imageurlwall", uri.toString());
                        dataRef_wallpaper.child(keywall).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(),wallepaper.class));
                            }
                        });
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress= (taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                textviewprogress.setText(progress+" %");
            }
        });
        
        
    }

    private void uploadImage(final String imagename) {
        textviewprogress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        //get key after push
       final String key=dataRef.push().getKey();
       storageRef.child(key+".jpg").putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.child(key+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        HashMap hashMap=new HashMap();
                        hashMap.put("CarName", imagename);
                        hashMap.put("imageurl", uri.toString());

                        dataRef.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(HomeActivity2.this, "Data succesfull", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),imageViewActivity.class));
                            }
                        });
                    }
                });


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress= (taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                textviewprogress.setText(progress+" %");

            }
        });
    }

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_IMAGE && data != null){
            imageuri=data.getData();
            isImageAdded=true;
            imageviewadd.setImageURI(imageuri);

            imageurlwall=data.getData();
            IsImageAddedWall=true;
            imageviewadd.setImageURI(imageurlwall);

            imageuri2row= data.getData();
            IsImageAdded2Row=true;
            imageviewadd.setImageURI(imageuri2row);



        }
    }


}