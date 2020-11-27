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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class people_images extends AppCompatActivity {

    RecyclerView recyclerView , recyc;
    FirebaseRecyclerOptions<Car> options ;
    FirebaseRecyclerOptions<Car> optionsnew;
    LottieAnimationView lottieAnimationView;
    FirebaseRecyclerAdapter<Car, MyVieHolder> madapter;
    FirebaseRecyclerAdapter<Car, MyVieHolder> madapternew;
    DatabaseReference dataRef , dataRefnew ;

    TextView textView;
    //new

    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_images);

        //mProgressCircle = findViewById(R.id.progress_circle_3);
        //mProgressCircle.setVisibility(View.VISIBLE);

        textView=findViewById(R.id.animation_textview);
        lottieAnimationView=findViewById(R.id.animation);
        lottieAnimationView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);




        recyclerView = findViewById(R.id.recyclerview_3);
        //  recyclerView.setLayoutManager();
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
       // StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
       // recyclerView.setLayoutManager(staggeredGridLayoutManager);
        // GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
       // linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);

        //new
 //       recyc=findViewById(R.id.recyc);
   //     LinearLayoutManager linearLayoutManager1= new LinearLayoutManager(getApplicationContext());
     //   recyc.setLayoutManager(linearLayoutManager1);
       // recyc.setHasFixedSize(true);



        dataRef = FirebaseDatabase.getInstance().getReference().child("All");
        //new
        dataRefnew = FirebaseDatabase.getInstance().getReference().child("Wallpaper");
       // postref=dataRef.child("countpost");



        bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.all);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account:startActivity(new Intent(getApplicationContext(),My_Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.all:
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


        floatingActionButton=findViewById(R.id.floatingbtn_people);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomeActivity3.class));
            }
        });



        LoadData();
       // LoadDataAll();

        // LoadDataAll();
    }

    // for creating such activity we need to
    //  1  options -> new
    //  2  adapter
    //  3  recycler view with its new id
    //  4  and getting imageurl into holder new
    //  5  ** database ref
    //  6



    /*private void LoadDataAll() {


        optionsnew = new FirebaseRecyclerOptions.Builder<Car>().setQuery(dataRefnew, Car.class).build();
        madapternew = new  FirebaseRecyclerAdapter<Car, MyVieHolder>(optionsnew) {
            @Override
            protected void onBindViewHolder(@NonNull  MyVieHolder holder, final int position, @NonNull final Car model) {
                // 2 holder.textView.setText(model.getCarName());
                Picasso.get().load(model.getImageurlwall()).into(holder.imageView3);
               // mProgressCircle.setVisibility(View.INVISIBLE);
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //holder.textView.setText(model.getCarName());
                        Intent intent=new Intent(people_images.this , imageActivity_2.class);
                        intent.putExtra("view_new",getRef(position).getKey());
                        startActivity(intent);
                    }
                });


            }
            @NonNull
            @Override
            public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_v_2, parent, false);
                return new MyVieHolder(v);
            }
        };
        madapternew.startListening();
        recyc.setAdapter(madapternew);
    }*/


    private void LoadData() {
        //String imagekey = getIntent().getStringExtra("keyofposition");
        Query sortpostindecending = dataRef.orderByChild("countpost");
      //  Query sort = dataRef.orderByKey();
      //  Query oskf =FirebaseDatabase.getInstance().getReference().child("All").orderByChild("countpost");
       // sortpostindecending.limitToLast(10000);

        options = new FirebaseRecyclerOptions.Builder<Car>().setQuery(sortpostindecending, Car.class).build();
        madapter = new  FirebaseRecyclerAdapter<Car, MyVieHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull  MyVieHolder holder, final int position, @NonNull final Car model) {
                // 2 holder.textView.setText(model.getCarName());
                Picasso.get().load(model.getPhotourl()).into(holder.imageView3);
               // sortpostindecending.orderByPriority().orderByValue();


                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //holder.textView.setText(model.getCarName());
                        Intent intent=new Intent(people_images.this , ViewActivity_people.class);
                        intent.putExtra("View_Activity_people",getRef(position).getKey());
                        startActivity(intent);
                    }
                });
                lottieAnimationView.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);

            }

            @NonNull
            @Override
            public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_v_2, parent, false);
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