package com.example.masterart.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.masterart.Adapter.VideoAdapter;
import com.example.masterart.Model.Post;
import com.example.masterart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    private ViewPager viewPager;
    private List<Post> videoLists;
    private Context context;
    VideoAdapter videoAdapter = new VideoAdapter(context, videoLists);

    private List<String> followingList;
    public String user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = view.findViewById(R.id.myViewPager);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        videoLists = new ArrayList<>();
        videoAdapter = new VideoAdapter(getContext(),videoLists);
        viewPager.setAdapter(videoAdapter);


        checkUnFollowing();

        return view;
    }
    private void checkFollowing()
    {
            followingList = new ArrayList<>();
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    followingList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        followingList.add(snapshot.getKey());
                    }
                    readPosts();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private void checkUnFollowing()
    {
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                        followingList.add(snapshot.getKey());
                }
                    readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readPosts()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Videos");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                videoLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Post video = snapshot.getValue(Post.class);
                    for (String id : followingList)
                    {
                        if (video.getVideo_uploader().equals(id))
                        {
                            videoLists.add(video);
                        }
                    }
                }
                videoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
