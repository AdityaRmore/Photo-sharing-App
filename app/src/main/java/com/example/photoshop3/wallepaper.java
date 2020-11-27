package com.example.photoshop3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class wallepaper extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar mProgressCircle;

    FirebaseRecyclerOptions<Car> options;
    FirebaseRecyclerAdapter<Car, MyVieHolder> madapter;
    DatabaseReference dataRef;
    StorageReference storageReference;


    //new

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallepaper);


        mProgressCircle = findViewById(R.id.progress_circle_wall);
        mProgressCircle.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recyclerview_wall);
        //GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
      //  StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
      //  recyclerView.setLayoutManager(staggeredGridLayoutManager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        dataRef = FirebaseDatabase.getInstance().getReference().child("Wallpaper");
        storageReference = FirebaseStorage.getInstance().getReference().child("wallpaper");


        bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.wallpaper);
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
                    case R.id.wallpaper:
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

        LoadDataAll();

    }

    private void LoadDataAll() {

        options = new FirebaseRecyclerOptions.Builder<Car>().setQuery(dataRef, Car.class).build();
        madapter = new  FirebaseRecyclerAdapter<Car, MyVieHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull  MyVieHolder holder, final int position, @NonNull final Car model) {
                // holder.textView.setText(model.getCarName());
                Picasso.get().load(model.getImageurlwall()).into(holder.imageviewwall);



                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //holder.textView.setText(model.getCarName());
                        Intent intent=new Intent(wallepaper.this , ViewActivity_wallpaper.class);
                        intent.putExtra("ViewActivity_Wallpaper",getRef(position).getKey());
                        startActivity(intent);
                    }
                });
                /*holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // holder.textView.setText(model.getCarName());
                        Intent intent=new Intent(people_images.this , ViewActivity.class);
                        intent.putExtra("carkey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });*/
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
            @NonNull
            @Override
            public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_v_3, parent, false);
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