package com.example.masterart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText user_name,full_name,password,email;
    Button register;
    TextView txt_login;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Initialization();

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please Wait.....!");
                pd.show();

                String str_username = user_name.getText().toString();
                String str_fullname = full_name.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();

                if(TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname)
                        || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password))
                {
                    Toast.makeText(RegisterActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
                } else if(password.length() < 6)
                {
                    Toast.makeText(RegisterActivity.this,"Password must have 6 characters",Toast.LENGTH_SHORT).show();
                }else
                    {
                        Register(str_username,str_fullname,str_email,str_password);
                    }
            }
        });
    }

    private void Register(final String username, final String fullname, String email, String password)
    {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                    HashMap<String , Object> hashMap = new HashMap<>();
                    hashMap.put("ID",userid);
                    hashMap.put("Username",username.toLowerCase());
                    hashMap.put("Fullname",fullname);
                    hashMap.put("bio","");
                    hashMap.put("ImageURL", "https://firebasestorage.googleapis.com/v0/b/masterart-4d543.appspot.com/o/ProfilePicture.png?alt=media&token=f3cb79a8-5cf2-4573-ba1e-163e9c32c4e7");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pd.dismiss();
                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                } else
                    {
                    pd.dismiss();
                        Toast.makeText(RegisterActivity.this,"You can't Register with this email or password",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void Initialization(){
        user_name = findViewById(R.id.username);
        full_name = findViewById(R.id.full_name);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        register = findViewById(R.id.register);
        txt_login = findViewById(R.id.txt_login);
        auth=FirebaseAuth.getInstance();
    }
}