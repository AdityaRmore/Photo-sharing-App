package com.example.photoshop3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.mikhaellopez.circularimageview.CircularImageView;

public class My_Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    TextView idname , idemail ;
    //ImageView idimage;
    ImageView imageView;
    Button signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__profile);

        idname=findViewById(R.id.id_name_signed);
        idemail=findViewById(R.id.id_email_signed);
       // idimage=findViewById(R.id.id_image);

        imageView=findViewById(R.id.id_image);
        signout=findViewById(R.id.sign_out);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        //  final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.account);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:
                        return true;
                    case R.id.all:startActivity(new Intent(getApplicationContext(),people_images.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.wallpaper:startActivity(new Intent(getApplicationContext(),wallepaper.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.favourite:startActivity(new Intent(getApplicationContext(),favourite.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:startActivity(new Intent(getApplicationContext(),imageViewActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });


       idname.setText(currentuser.getDisplayName());
       idemail.setText(currentuser.getEmail());
        Glide.with(this).load(currentuser.getPhotoUrl()).into(imageView);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent=new Intent(My_Profile.this , login_Activity.class);
                startActivity(intent);

                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}