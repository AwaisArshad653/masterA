package com.example.masterart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.masterart.Fragment.HomeFragment;
import com.example.masterart.Fragment.SearchFragment;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

import javax.xml.transform.Result;

public class PostActivity extends Activity {

    public final int VIDEO_REQUEST_CODE = 101;
    public Uri video_uri;
    String myUri="";
    public StorageReference videoRef;
    public FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        videoRef = FirebaseStorage.getInstance().getReference().child("Video");
        storageRef.child("/videos/" + storageRef + "/video.mp4");
        View view = null;
        captureVideo(view);

    }

    public void captureVideo(View view)
    {
        Intent camera_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(camera_intent,VIDEO_REQUEST_CODE);
    }

    /*private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }*/

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            video_uri = data.getData();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Save Video");
            if(video_uri!=null)
            {
                if (requestCode == VIDEO_REQUEST_CODE) {
                    if (resultCode == RESULT_OK) {
                        final StorageReference fileReference = videoRef.child(System.currentTimeMillis() + "." + video_uri);
                        UploadTask uploadTask = videoRef.putFile(video_uri);
                        Uri downloaduri = video_uri;
                        myUri = downloaduri.toString();


                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Videos");

                        String videoid = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("Video_url", videoid);
                        hashMap.put("Video", myUri);
                        hashMap.put("Video_uploader", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        reference.child(videoid).setValue(hashMap);
                        progressDialog.dismiss();
                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                        Toast.makeText(PostActivity.this, "Upload Finish", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (requestCode == RESULT_CANCELED){
                        Toast.makeText(this, "Video Record cancelled", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Failed to record video", Toast.LENGTH_SHORT).show();
                    }
                }
                /*final StorageReference fileReference = videoRef.child(System.currentTimeMillis() + "." + getFileExtension(video_uri));
                UploadTask uploadTask = videoRef.putFile(video_uri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        return fileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloaduri = task.getResult();
                            myUri = downloaduri.toString();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Videos");

                            String videoid = reference.push().getKey();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("Video_url", videoid);
                            hashMap.put("Video", myUri);
                            hashMap.put("Video_uploader", FirebaseAuth.getInstance().getCurrentUser().getUid());

                            reference.child(videoid).setValue(hashMap);
                            progressDialog.dismiss();
                            startActivity(new Intent(PostActivity.this, MainActivity.class));
                            Toast.makeText(PostActivity.this, "Upload Finish", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(PostActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
    }

}
