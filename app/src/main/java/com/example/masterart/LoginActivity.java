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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button login_btn;
    TextView txt_signup;

    FirebaseAuth auth;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Initialization();

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage("Please Wait...!");
                    pd.show();

                    String str_email = email.getText().toString();
                    String str_password = password.getText().toString();

                    if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)) {
                        Toast.makeText(LoginActivity.this, "All Fields are required", Toast.LENGTH_SHORT).show();
                    } else {
                        auth.signInWithEmailAndPassword(str_email, str_password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").
                                            child(auth.getCurrentUser().getUid());

                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            pd.dismiss();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            pd.dismiss();
                                        }
                                    });
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(LoginActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }catch (Exception e)
                {
                    Toast.makeText(LoginActivity.this, "Error: "+ e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    void Initialization(){
        email = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        txt_signup = findViewById(R.id.txt_sign_up);

        auth = FirebaseAuth.getInstance();
    }
}
