package com.example.photoshop3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_IME = 4000;
    private  FirebaseAuth mAuth;

    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lottieAnimationView=findViewById(R.id.animation);
        lottieAnimationView.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
             if (user != null){
                 //user is signed in
                 Intent intent=new Intent(MainActivity.this , imageViewActivity.class);
                 startActivity(intent);
                 finish();
             }else {
                 //user is not signed in
                 Intent intent=new Intent(MainActivity.this , login_Activity.class);
                 startActivity(intent);
                 finish();
             }
            }

        },SPLASH_IME);




    }
}