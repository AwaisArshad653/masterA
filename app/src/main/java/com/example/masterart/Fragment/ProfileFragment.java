package com.example.masterart.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.masterart.Adapter.MyVideoAdapter;
import com.example.masterart.EditProfileActivity;
import com.example.masterart.FollowersActivity;
import com.example.masterart.LoginActivity;
import com.example.masterart.Model.Post;
import com.example.masterart.Model.User;
import com.example.masterart.OptionsActivity;
import com.example.masterart.R;
import com.example.masterart.StartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class ProfileFragment extends Fragment {

    ImageView image_profile, options;
    TextView videos,followers,following,fullname,bio,username;
    Button edit_profile;

    RecyclerView recyclerView;
    MyVideoAdapter myVideoAdapter;
    List<Post> postList;

    FirebaseUser firebaseUser;
    String profile_id;
    ImageButton my_videos;


@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    SharedPreferences prefs = getContext().getSharedPreferences("PREFS",Context.MODE_PRIVATE);
    profile_id = prefs.getString("profileid","none");

    image_profile = view.findViewById(R.id.image_profile);
    videos = view.findViewById(R.id.videos);
    followers = view.findViewById(R.id.followers);
    following = view.findViewById(R.id.following);
    fullname = view.findViewById(R.id.full_name);
    bio = view.findViewById(R.id.bio);
    username = view.findViewById(R.id.username);
    my_videos = view.findViewById(R.id.my_videos);
    edit_profile = view.findViewById(R.id.edit_profile);
    options = view.findViewById(R.id.options);

    recyclerView = view.findViewById(R.id.recycler_view1);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    postList = new ArrayList<>();
    myVideoAdapter = new MyVideoAdapter(getContext(),postList);
    recyclerView.setAdapter(myVideoAdapter);

    userinfo();
    getFollowers();
    getNrPosts();
    myVideos();

    if (profile_id.equals(firebaseUser.getUid()))
    {
        edit_profile.setText("Edit Profile");
    }else
        {
            checkFollowing();
        }

    edit_profile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String btn = edit_profile.getText().toString();
            if (btn.equals("Edit Profile"))
            {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }else if(btn.equals("follow"))
            {
                FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(profile_id).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("Follow").child(profile_id).child("followers").child(firebaseUser.getUid()).setValue(true);
                addNotifications();
            }else if (btn.equals("following"))
            {
                FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(profile_id).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Follow").child(profile_id).child("followers").child(firebaseUser.getUid()).removeValue();
            }
        }
    });
    followers.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), FollowersActivity.class);
            intent.putExtra("id",profile_id);
            intent.putExtra("title","followers");
            startActivity(intent);
        }
    });
    following.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), FollowersActivity.class);
            intent.putExtra("id",profile_id);
            intent.putExtra("title","following");
            startActivity(intent);
        }
    });
    options.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), OptionsActivity.class);
            startActivity(intent);
        }
    });

        return view;
    }

    private void addNotifications()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(profile_id);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userid",firebaseUser.getUid());
        hashMap.put("text","Started following you");
        hashMap.put("Video_id","");
        hashMap.put("ispost",false);


        reference.push().setValue(hashMap);
    }

    private void userinfo()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(profile_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(getContext() == null)
                {
                    return;
                }

                User user = dataSnapshot.getValue(User.class);

                Glide.with(getContext()).load(user.getImageurl()).into(image_profile);
                username.setText(user.getUsername());
                fullname.setText(user.getFullname());
                bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkFollowing()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profile_id).exists())
                {
                    edit_profile.setText("following");
                }else
                {
                    edit_profile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFollowers()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(profile_id).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Follow").child(profile_id).child("following");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getNrPosts()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Videos");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Post post = snapshot.getValue(Post.class);
                    if (post.getVideo_uploader().equals(profile_id))
                    {
                        i++;
                    }
                }

                videos.setText(""+i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void myVideos()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Videos");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Post post = snapshot.getValue(Post.class);
                    if (post.getVideo_uploader().equals(profile_id))
                    {
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                myVideoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
