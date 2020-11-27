package com.example.photoshop3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class favourite extends AppCompatActivity {

    RecyclerView recyclerView , recyc;
    LottieAnimationView lottieAnimationView;
    TextView animation_textview;

    FirebaseRecyclerOptions<Car> options;
    FirebaseRecyclerAdapter<Car, MyVieHolder> madapter;
    DatabaseReference datafav;

    FirebaseRecyclerOptions<Car> optionsnew;
    FirebaseRecyclerAdapter<Car, MyVieHolder> madapternew;
    DatabaseReference datafavnew;

    ImageView imageView;

    FirebaseUser user;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        imageView=findViewById(R.id.imageViewfav);

        lottieAnimationView=findViewById(R.id.animation);
        animation_textview=findViewById(R.id.animation_textview);

        animation_textview.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);

        //new
        recyclerView=findViewById(R.id.recyclerview_fav);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        //new
        recyc=findViewById(R.id.recyc);
        LinearLayoutManager linearLayoutManager1= new LinearLayoutManager(getApplicationContext());
        linearLayoutManager1.setReverseLayout(true);
        linearLayoutManager1.setStackFromEnd(true);
        recyc.setLayoutManager(linearLayoutManager1);
        recyc.setHasFixedSize(true);



        //favourite_image=findViewById(R.id.favourite_image);

        // String favpostkey = getRef(position).getKey();
        //final String fav= datafav.child(favpostkey).getKey();
       // String avas = getIntent().getStringExtra("favimg");
        //datafav = FirebaseDatabase.getInstance().getReference().child("favourite").child(favuser).child(avas).child("imageurl123");
        //final String favimg= getIntent().getStringExtra("imageurl123");




        bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.favourite);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:startActivity(new Intent(getApplicationContext(),My_Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.all:startActivity(new Intent(getApplicationContext(),people_images.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.wallpaper:startActivity(new Intent(getApplicationContext(),wallepaper.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.favourite:
                        return true;
                    case R.id.home:startActivity(new Intent(getApplicationContext(),imageViewActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });



//        final String favkey= getIntent().getStringExtra("favkey");
  //      FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    //    final String favuser=user.getUid();

        //datafav=FirebaseDatabase.getInstance().getReference().child("favourite").child(favuser).child(favkey);
      /*  datafav.child(favuser).child("favkey").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                   // final String imageurl = snapshot.child("favimage").getValue().toString();

                }
                //LoadAllfavimg();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });*/

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        final String favuser=user.getUid();

        datafav = FirebaseDatabase.getInstance().getReference().child("favourite").child(favuser);
        datafavnew=FirebaseDatabase.getInstance().getReference().child("favourite2").child(favuser);

        LoadDataAll();
        LoadDatanew();

        //    String img =child("favimg").getValue().toString();

    }

    //fav 2
    private void LoadDatanew() {

        optionsnew = new FirebaseRecyclerOptions.Builder<Car>().setQuery(datafavnew, Car.class).build();
        madapternew = new  FirebaseRecyclerAdapter<Car, MyVieHolder>(optionsnew) {
            @Override
            protected void onBindViewHolder(@NonNull  MyVieHolder holder, final int position, @NonNull final Car model) {
                // 2 holder.textView.setText(model.getCarName());
                Picasso.get().load(model.getData2url()).into(holder.imageviewwall);

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //holder.textView.setText(model.getCarName());
                        Intent intent=new Intent(favourite.this , imagesActivity_Fav2.class);
                        intent.putExtra("img_fav_2",getRef(position).getKey());
                        startActivity(intent);
                    }
                });


                animation_textview.setVisibility(View.INVISIBLE);
                lottieAnimationView.setVisibility(View.INVISIBLE);
            }
            @NonNull
            @Override
            public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_v_3, parent, false);
                return new MyVieHolder(v);
            }
        };
        madapternew.startListening();
        recyc.setAdapter(madapternew);
    }


    //image fav 1
    private void LoadDataAll() {

        options = new FirebaseRecyclerOptions.Builder<Car>().setQuery(datafav, Car.class).build();
        madapter = new  FirebaseRecyclerAdapter<Car, MyVieHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull  MyVieHolder holder, final int position, @NonNull final Car model) {
                // 2 holder.textView.setText(model.getCarName());
                Picasso.get().load(model.getImageurl()).into(holder.image_v_fav2);// image_v_fav2

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //holder.textView.setText(model.getCarName());
                        Intent intent=new Intent(favourite.this , imagesActivity_Fav1.class);
                        intent.putExtra("img_fav_1",getRef(position).getKey());
                        startActivity(intent);
                    }
                });

                animation_textview.setVisibility(View.INVISIBLE);
                lottieAnimationView.setVisibility(View.INVISIBLE);
            }
            @NonNull
            @Override
            public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_v_fav2, parent, false); //single_v_fav2
                return new MyVieHolder(v);
            }
        };
        madapter.startListening();
        recyclerView.setAdapter(madapter);
    }




    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}