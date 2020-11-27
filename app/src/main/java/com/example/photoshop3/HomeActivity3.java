package com.example.photoshop3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class HomeActivity3 extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 101;
    private ImageView imageviewadd;
    private EditText inputimagename;
    private TextView textviewprogress;
    private ProgressBar progressBar;

    Uri imageuri3;
    boolean IsImageAdded;

    DatabaseReference dataRef3 ;
    StorageReference storageRef3;

    int maxid=1000;
    long countpost= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);


        imageviewadd=findViewById(R.id.imageViewadd_3);
        inputimagename=findViewById(R.id.inputimagename_3);
        textviewprogress=findViewById(R.id.textviewprogress_3);
        progressBar=findViewById(R.id.progressbar_3);
        Button btnupload = findViewById(R.id.btnupload_3);


        textviewprogress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        dataRef3= FirebaseDatabase.getInstance().getReference().child("All");//also called  CAR
        storageRef3= FirebaseStorage.getInstance().getReference().child("All_images");

    //    postref=FirebaseDatabase.getInstance().getReference();

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
                Toast.makeText(HomeActivity3.this,"Image is uploading please Wait",Toast.LENGTH_LONG).show();
                final String imagename =inputimagename.getText().toString();
                if (IsImageAdded!=false && imagename!= null){

                    uploadImage(imagename);
                }
            }
        });

    }
    // postref  ,  userref    == database ref
    // postimagesref == storage ref
    // validate post info ==== on update post button

    private void uploadImage(final String imagename) {
        textviewprogress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        final String key3=dataRef3.push().getKey();
        final String url= storageRef3.child(key3+".jpg").getDownloadUrl().toString();


        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        final String favuser=user.getUid();

        dataRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    countpost=snapshot.getChildrenCount();
                }else {
                    countpost=0;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        storageRef3.child(key3+".jpg").putFile(imageuri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storageRef3.child(key3+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap=new HashMap();
                        hashMap.put("photo", imagename);
                        hashMap.put("photourl", uri.toString());
                        hashMap.put("countpost", countpost);
                        hashMap.put("user", favuser );
                   //     Intent intent=new Intent(HomeActivity3.this,people_images.class);
                     //   intent.putExtra("keyofposition",key3);

                        dataRef3.child(key3).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Toast.makeText(HomeActivity2.this, "Data succesfull", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),people_images.class));
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
            imageuri3=data.getData();
            IsImageAdded=true;
            imageviewadd.setImageURI(imageuri3);
        }
    }
}