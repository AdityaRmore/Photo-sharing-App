package com.example.photoshop3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

class MyVieHolder extends RecyclerView.ViewHolder {
    ImageView imageView , imageView3 , imageviewwall , imageViewfav, imageViewfav_2 ,imageViewfav_new , image_v_fav2;
    TextView textView;
    View view;
    Button button , button123;

    //for likes
    Button  likesbutton , likesbutton_2;
    TextView likesdisplay , likesdisplay_2;
    int likescount , likescount_2;
    DatabaseReference likesref , likeref_2;

    //new for fav try
    Button btnfav , btnfav_2;
    DatabaseReference favref , favref_2;



    public MyVieHolder(@NonNull View itemView) {
        super(itemView);

        // 1 textView=itemView.findViewById(R.id.textView_single_View);
        imageView=itemView.findViewById(R.id.image_singleview);
        imageView3=itemView.findViewById(R.id.image_singleview_2);
        imageviewwall=itemView.findViewById(R.id.image_singleview_wall);
        image_v_fav2=itemView.findViewById(R.id.single_v_fav2);

        //new
        button=itemView.findViewById(R.id.download_btn);
        view=itemView;
    }

    public void setLikesbuttonStatus(final String postkey) {
        imageViewfav=itemView.findViewById(R.id.image_singleview_fav);
        likesbutton=itemView.findViewById(R.id.like_btn);
        likesdisplay=itemView.findViewById(R.id.likes_textview);
        likesref= FirebaseDatabase.getInstance().getReference("likes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        final String likes = " likes";

        likesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postkey).hasChild(userId)){
                    likescount=(int)snapshot.child(postkey).getChildrenCount();
                    likesbutton.setBackgroundResource(R.drawable.favorite_24);
                    likesdisplay.setText(Integer.toString(likescount)+ likes);

                }else {
                    likescount=(int)snapshot.child(postkey).getChildrenCount();
                    likesbutton.setBackgroundResource(R.drawable.favorite_border_24);
                    likesdisplay.setText(Integer.toString(likescount)+ likes);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void setLikesbuttonnew(final String postkey_2) {

        imageViewfav_2=itemView.findViewById(R.id.image_singleview_fav);
        likesbutton_2=itemView.findViewById(R.id.like_btn);
        likesdisplay_2=itemView.findViewById(R.id.likes_textview);
        likeref_2=FirebaseDatabase.getInstance().getReference().child("likes2");

        FirebaseUser user_2 = FirebaseAuth.getInstance().getCurrentUser();
        final String userId_2 = user_2.getUid();
        final String likes_2 = " likes";

        likeref_2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(postkey_2).hasChild(userId_2)){
                    likescount_2=(int)snapshot.child(postkey_2).getChildrenCount();
                    likesbutton_2.setBackgroundResource(R.drawable.favorite_24);
                    likesdisplay_2.setText(Integer.toString(likescount_2)+ likes_2);

                }else {
                    likescount_2=(int)snapshot.child(postkey_2).getChildrenCount();
                    likesbutton_2.setBackgroundResource(R.drawable.favorite_border_24);
                    likesdisplay_2.setText(Integer.toString(likescount_2)+ likes_2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void setFavbtnsaver(final String postkey) {
        imageViewfav=itemView.findViewById(R.id.image_singleview_fav);
        btnfav=itemView.findViewById(R.id.btn_favourite);
        favref=FirebaseDatabase.getInstance().getReference().child("favourite");
        FirebaseUser favuser =FirebaseAuth.getInstance().getCurrentUser();
        final String favuserId12 = favuser.getUid();


        favref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(favuserId12).hasChild(postkey)){
                    btnfav.setBackgroundResource(R.drawable.ic_flame_dark);
                }else {
                    btnfav.setBackgroundResource(R.drawable.ic_flame);
                    //Toast.makeText(MyVieHolder,"Image Added to favourite",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void setFavbtnsaver_2(final String favpostkey) {

        imageViewfav_new=itemView.findViewById(R.id.image_singleview_fav);
        btnfav_2=itemView.findViewById(R.id.btn_favourite);
        favref_2=FirebaseDatabase.getInstance().getReference().child("favourite2");

        FirebaseUser favuser_2 =FirebaseAuth.getInstance().getCurrentUser();
        final String favuserId123 = favuser_2.getUid();


             // 1
        favref_2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(favuserId123).hasChild(favpostkey)){
                    btnfav_2.setBackgroundResource(R.drawable.ic_flame_dark);
                }else {
                    btnfav_2.setBackgroundResource(R.drawable.ic_flame);
                    //Toast.makeText(MyVieHolder,"Image Added to favourite",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
