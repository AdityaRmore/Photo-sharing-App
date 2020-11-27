package com.example.photoshop3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class imageViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSION_STORAGE_CODE=1000;
    String downloadurl , downloadurl_2;

    BottomNavigationView bottomNavigationView;
    LottieAnimationView animationView;
    TextView animation_textview;

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    FirebaseRecyclerOptions<Car> optios;
    FirebaseRecyclerAdapter<Car, MyVieHolder> adapter;
    DatabaseReference dataRef , likesreference , likeref_2;

    FirebaseUser currentuserphoto;
    Boolean likechecker = false;//likes
    Boolean likechecker_2 = false;
    Boolean favbuttonchecker = false;     //new fav try
    Boolean favbuttonchecker_2= false;
    DatabaseReference favref , favref_2;

    //drawer
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mtoggle;

    //new
    RecyclerView recyc;
    DatabaseReference dataRefnew;
    FirebaseRecyclerOptions<Car> optionsnew;
    FirebaseRecyclerAdapter<Car, MyVieHolder> madapternew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        //drawer
        drawerLayout=findViewById(R.id.drawer_layout);
        mtoggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);



        animation_textview=findViewById(R.id.animation_textview);
        animation_textview.setVisibility(View.VISIBLE);
        animationView=findViewById(R.id.animation);
        animationView.setVisibility(View.VISIBLE);

        dataRef = FirebaseDatabase.getInstance().getReference().child("Data");
        likesreference=FirebaseDatabase.getInstance().getReference().child("likes");
        favref=FirebaseDatabase.getInstance().getReference().child("favourite");
        favref_2=FirebaseDatabase.getInstance().getReference().child("favourite2");

        likeref_2=FirebaseDatabase.getInstance().getReference().child("likes2");

        dataRefnew = FirebaseDatabase.getInstance().getReference().child("Data2");

        currentuserphoto=FirebaseAuth.getInstance().getCurrentUser();


        bottomNavigationView=findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.home);
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
                    case R.id.favourite:startActivity(new Intent(getApplicationContext(),favourite.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;

                }
                return false;
            }
        });



        recyclerView = findViewById(R.id.recyclerview);
       // GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
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

        LoadData();
        LoadAllData();


    }

    private void LoadAllData() {


            optionsnew = new FirebaseRecyclerOptions.Builder<Car>().setQuery(dataRefnew, Car.class).build();
            madapternew = new  FirebaseRecyclerAdapter<Car, MyVieHolder>(optionsnew) {
                @Override
                protected void onBindViewHolder(@NonNull  MyVieHolder holder, final int position, @NonNull final Car model) {
                    // 2 holder.textView.setText(model.getCarName());
                    Picasso.get().load(model.getData2url()).into(holder.imageView);
                    // mProgressCircle.setVisibility(View.INVISIBLE);
                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //holder.textView.setText(model.getCarName());
                            Intent intent=new Intent(imageViewActivity.this , imageActivity_2.class);
                            intent.putExtra("view_new",getRef(position).getKey());
                            startActivity(intent);
                        }
                    });

                    //likes
                    FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
                    final String currentUserId_2 = user.getUid();
                    final String postkey_2 = getRef(position).getKey();

                    holder.setLikesbuttonnew(postkey_2);
                    holder.likesbutton_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            likechecker_2=true;
                            likeref_2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (likechecker_2.equals(true)){
                                        if (snapshot.child(postkey_2).hasChild(currentUserId_2)){
                                            likeref_2.child(postkey_2).child(currentUserId_2).removeValue();
                                            likechecker_2=false;
                                        }else {
                                            likeref_2.child(postkey_2).child(currentUserId_2).setValue(true);
                                            likechecker_2=false;
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });

                        }
                    });

                    //favourite
                    FirebaseUser favuser =FirebaseAuth.getInstance().getCurrentUser();
                    final String favCurrentUserId = favuser.getUid();
                    final String favpostkey = getRef(position).getKey();
                    holder.setFavbtnsaver_2(favpostkey);
                    holder.btnfav_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            favbuttonchecker_2=true;
                            favref_2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (favbuttonchecker_2.equals(true)){
                                        if (snapshot.child(favCurrentUserId).hasChild(favpostkey)){
                                            favref_2.child(favCurrentUserId).child(favpostkey).removeValue();
                                            //favref.child(favpostkey).getKey();
                                            FavRemove();
                                            //Toast.makeText(this,"fff",Toast.LENGTH_SHORT).show();
                                            favbuttonchecker_2=false;
                                        }else {
                                            favref_2.child(favCurrentUserId).child(favpostkey).child("data2url").setValue(model.getData2url());
                                            favref_2.child(favCurrentUserId).child(favpostkey).child("data2name").setValue(model.getData2name());
                                            //favref.child(favpostkey).getKey();
                                            FavAdd();
                                            favbuttonchecker_2=false;
                                        }

                                    }else { }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    });


                    //download
                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                                    String permission =(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                    requestPermissions(new String[]{permission} , PERMISSION_STORAGE_CODE);
                                }else {
                                    downloadurl_2=getItem(position).getData2url();
                                    startDownloading(downloadurl_2);
                                }
                            }else {
                                downloadurl_2=getItem(position).getData2url();
                                startDownloading(downloadurl_2);

                            }
                        }
                    });

                    animationView.setVisibility(View.INVISIBLE);
                    animation_textview.setVisibility(View.INVISIBLE);
                }
                @NonNull
                @Override
                public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
                    return new MyVieHolder(v);
                }
            };
            madapternew.startListening();
            recyc.setAdapter(madapternew);
    }


    private void LoadData() {
        optios = new FirebaseRecyclerOptions.Builder<Car>().setQuery(dataRef, Car.class).build();
        adapter = new FirebaseRecyclerAdapter<Car, MyVieHolder>(optios) {
            @Override
            protected void onBindViewHolder(@NonNull  MyVieHolder holder, final int position, @NonNull final Car model) {
                // 2 holder.textView.setText(model.getCarName());
                Picasso.get().load(model.getImageurl()).into(holder.imageView);

                //new or likes
                FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
                final String currentUserId = user.getUid();
                final String postkey = getRef(position).getKey();

                holder.setLikesbuttonStatus(postkey);
                holder.likesbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        likechecker=true;
                        likesreference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (likechecker.equals(true)){
                                    if (snapshot.child(postkey).hasChild(currentUserId)){
                                        likesreference.child(postkey).child(currentUserId).removeValue();
                                        likechecker=false;
                                    }else {
                                        likesreference.child(postkey).child(currentUserId).setValue(true);
                                        likechecker=false;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }
                });

                //for favourite fav 1
                FirebaseUser favuser =FirebaseAuth.getInstance().getCurrentUser();
                final String favCurrentUserId = favuser.getUid();
                final String favpostkey = getRef(position).getKey();
                final String fav= favref.child(favpostkey).getKey();

                holder.setFavbtnsaver(favpostkey);
                holder.btnfav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        favbuttonchecker=true;
                        favref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (favbuttonchecker.equals(true)){
                                    if (snapshot.child(favCurrentUserId).hasChild(favpostkey)){
                                        favref.child(favCurrentUserId).child(favpostkey).removeValue();
                                        //favref.child(favpostkey).getKey();
                                        Intent intent=new Intent(imageViewActivity.this , favourite.class);
                                        intent.putExtra("favkey",getRef(position).getKey());
                                        startActivity(intent);
                                        FavRemove();
                                        //Toast.makeText(this,"fff",Toast.LENGTH_SHORT).show();
                                        favbuttonchecker=false;
                                    }else {
                                        favref.child(favCurrentUserId).child(favpostkey).child("imageurl").setValue(model.getImageurl());
                                        favref.child(favCurrentUserId).child(favpostkey).child("CarName").setValue(model.getCarName());
                                        //favref.child(favpostkey).getKey();
                                        Intent intent=new Intent(imageViewActivity.this , favourite.class);
                                        intent.putExtra("favkey",getRef(position).getKey());
                                        startActivity(intent);
                                        FavAdd();
                                        favbuttonchecker=false;
                                    }

                                }else { }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                });

                //for download
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if (checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                                String permission =(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                requestPermissions(new String[]{permission} , PERMISSION_STORAGE_CODE);
                            }else {
                                downloadurl=getItem(position).getImageurl();
                                startDownloading(downloadurl);
                            }
                        }else {
                            downloadurl=getItem(position).getImageurl();
                            startDownloading(downloadurl);

                        }
                    }
                });

                ///open new activity
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String postkey = getRef(position).getKey();
                        //holder.textView.setText(model.getCarName());
                        Intent intent=new Intent(imageViewActivity.this , ViewActivity.class);
                        intent.putExtra("carkey",postkey);
                        startActivity(intent);
                    }
                });
                //mProgressCircle.setVisibility(View.INVISIBLE);
                animationView.setVisibility(View.INVISIBLE);
                animation_textview.setVisibility(View.INVISIBLE);

            }

            @NonNull
            @Override
            public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
                return new MyVieHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    //fav add
    private void FavAdd() {
        Toast.makeText(this,"Image Added to Favourie",Toast.LENGTH_SHORT).show();

    }
    //fav removed
    private void FavRemove() {
        Toast.makeText(this,"Image Removed from Favourie",Toast.LENGTH_SHORT).show();
    }


    // for downloading
    private void startDownloading(String downloadurl) {

        DownloadManager.Request  request= new DownloadManager.Request(Uri.parse(downloadurl));
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

    // for download
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    startDownloading(downloadurl);
                    startDownloading(downloadurl_2);
                    Toast.makeText(this, "Download start ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Permission granted ", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this,"dkf", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    //navigation
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    //for navigation
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        if (id== R.id.home_drawer){
            Toast.makeText(this,"this is home",Toast.LENGTH_SHORT).show();

        }
        if (id==R.id.myProfile_drawer){
            startActivity(new Intent(getApplicationContext(), login_admin.class));

        }
        if (id==R.id.instagrem_drawer){
            gotoUrl("https://www.instagram.com/aditya_.more/");
        }
        if (id==R.id.visitus){
            gotoUrl("https://sites.google.com/view/wall-s/home");

        }
        if (id==R.id.shareapp_drawer){

            //gotoUrl("https://sites.google.com/view/wall-s/home");
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String sharebody ="https://sites.google.com/view/wall-s/home";
            String sharesub = " https://sites.google.com/view/wall-s/home";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
            myIntent.putExtra(Intent.EXTRA_TEXT,sharebody);
            startActivity(Intent.createChooser(myIntent,"Share Using"));
        }
        if (id==R.id.exit_drawer){
            finish();
        }

        return false;
    }

    //for navigation
    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));

    }

    //for navigation
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawermenu,menu);
        return true;
    }*/


}