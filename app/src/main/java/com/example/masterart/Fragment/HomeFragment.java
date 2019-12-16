package com.example.masterart.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.masterart.Adapter.VideoAdapter;
import com.example.masterart.Model.Post;
import com.example.masterart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<Post> videoLists;

    private List<String> followingList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        videoLists = new ArrayList<>();
        videoAdapter = new VideoAdapter(getContext(),videoLists);
        recyclerView.setAdapter(videoAdapter);

        checkFollowing();

        return view;
    }

    private void checkFollowing()
    {
        try {
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");

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
    }catch (Exception e)
        {
            Toast.makeText(getContext(), "Error" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void readPosts()
    {
        try {
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
    }catch (Exception e)
    {
        Toast.makeText(getContext(), "Error" + e, Toast.LENGTH_SHORT).show();
    }
    }


}
