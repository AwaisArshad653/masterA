package com.example.masterart.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.masterart.CommentsActivity;
import com.example.masterart.FollowersActivity;
import com.example.masterart.Fragment.ProfileFragment;
import com.example.masterart.Model.Post;
import com.example.masterart.Model.User;
import com.example.masterart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;




public class VideoAdapter extends PagerAdapter{

    public Context mContext;
    private LayoutInflater layoutInflater;
    public List<Post> mVideo;
    public MediaController mediaControls;
    public ImageView image_profile,like,comment;
    public TextView likes,publisher,comments;
    public VideoView videoView;

    private FirebaseUser firebaseUser;

    public VideoAdapter(Context context, List<Post> mPost) {
        this.mContext = context;
        this.mVideo = mPost;
    }

    @Override
    public int getCount() {
        return mVideo.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position)
    {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_item ,null);
        mediaControls = new MediaController(mContext);
        MediaStore mediaStore;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post = mVideo.get(position);
        image_profile = view.findViewById(R.id.image_profile);
        videoView = view.findViewById(R.id.video_id);
        like = view.findViewById(R.id.like);
        likes = view.findViewById(R.id.likes);
        publisher = view.findViewById(R.id.publisher);
        comment = view.findViewById(R.id.comment);
        comments = view.findViewById(R.id.comments);
        VideoView videoView = view.findViewById(R.id.video_id);
        videoView.setMediaController(mediaControls);
        videoView.setVideoURI(Uri.parse(post.getVideo()));
        videoView.requestFocus();
        videoView.start();

        if (!videoView.isPlaying())
        {
            Toast.makeText(mContext, "Please wait", Toast.LENGTH_SHORT).show();
        }

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
            }
        });

        publisherInfo(image_profile,publisher,post.getVideo_uploader());
        isLiked(post.getVideo_id(),like);
        nrLikes(likes,post.getVideo_id());
        getComments(post.getVideo_id(),comments);
        like.setTag("like");

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getVideo_uploader());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
            }
        });

        publisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid",post.getVideo_uploader());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
            }
        });


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (like.getTag().equals("like"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getVideo_id()).child(firebaseUser.getUid()).setValue(true);
                    addNotifications(post.getVideo_uploader(),post.getVideo_id());
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getVideo_id()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("Video_id",post.getVideo_id());
                intent.putExtra("Video_uploader",post.getVideo_id());
                mContext.startActivity(intent);
            }
        });
        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("Video_id",post.getVideo_id());
                intent.putExtra("Video_uploader",post.getVideo_id());
                mContext.startActivity(intent);
            }
        });

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FollowersActivity.class);
                intent.putExtra("id",post.getVideo_id());
                intent.putExtra("title","likes");
                mContext. startActivity(intent);
            }
        });

        ViewPager viewPager = (ViewPager)container;
        viewPager.addView(view);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container,int position,@NonNull Object object)
    {
        ViewPager viewPager = (ViewPager)container;
        View view = (View)object;
        viewPager.removeView(view);
    }

    private void getComments(String videoid, final TextView comments)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(videoid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void isLiked(String videoid, final ImageView likeView)
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(videoid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists())
                {
                    likeView.setImageResource(R.drawable.ic_liked);
                    likeView.setTag("liked");
                }
                else {
                    likeView.setImageResource(R.drawable.ic_like);
                    likeView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void addNotifications(String userid,String postid)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userid",firebaseUser.getUid());
        hashMap.put("text","liked your post");
        hashMap.put("Video_id",postid);
        hashMap.put("ispost",true);


        reference.push().setValue(hashMap);
    }

    private void nrLikes(final TextView likes, String videoid)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(videoid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText("Likes "+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void publisherInfo(final ImageView image_profile, final TextView publisher, String userid)
    {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    Glide.with(mContext).load(user.getImageurl()).into(image_profile);
                    publisher.setText(user.getUsername());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
}
