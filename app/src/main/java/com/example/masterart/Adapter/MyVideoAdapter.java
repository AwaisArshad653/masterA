package com.example.masterart.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masterart.Model.Post;
import com.example.masterart.R;

import java.util.List;

public class MyVideoAdapter extends RecyclerView.Adapter<MyVideoAdapter.Viewholder>{

    private Context context;
    private List<Post> mPosts;
    public VideoView post_video;
    public MediaController mediaControls;

    public MyVideoAdapter(Context context, List<Post> mPosts) {
        this.context = context;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.videos_item,parent,false);
        return new MyVideoAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        final Post post = mPosts.get(position);

        //mediaControls = new MediaController(context);
        //post_video.setMediaController(mediaControls);
        post_video.setVideoURI(Uri.parse(post.getVideo()));
        post_video.requestFocus();
        post_video.start();
    }
    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            post_video = itemView.findViewById(R.id.post_video);

        }
    }
}
