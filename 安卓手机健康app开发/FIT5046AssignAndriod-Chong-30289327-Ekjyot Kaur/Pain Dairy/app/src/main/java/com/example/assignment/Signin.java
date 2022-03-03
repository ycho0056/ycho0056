package com.example.assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signin extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mRegister;
    Button mLogin;
    FirebaseAuth auth;
    CheckBox mCheckbox;
    CheckBox mRemberUsername;
    public static String Email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
        mLogin=findViewById(R.id.signin);
        mRegister=findViewById(R.id.register);
        auth =FirebaseAuth.getInstance();
        mCheckbox = findViewById(R.id.Checkbox);
        mRemberUsername = findViewById(R.id.rememberUsername);

        SharedPreferences sharedPreferences = getSharedPreferences("accountinfo", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("rememberaccount", false)){
            mEmail.setText(sharedPreferences.getString("account", ""));
            mPassword.setText(sharedPreferences.getString("password", ""));
            mRemberUsername.setChecked(true);
        };

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("email cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("password cannot be empty");
                    return;
                }
                if(password.length()< 6){
                    mPassword.setError("Password must more than 6 character");
                }
                if (mRemberUsername.isChecked()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("account", mEmail.getText().toString());
                    editor.putString("password", mPassword.getText().toString());
                    editor.putBoolean("rememberaccount", true);
                    editor.commit();
                }
                else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("rememberaccount", false);
                    editor.commit();

                }
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Email = email;
                            Toast.makeText(Signin.this,"Log in successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(Signin.this,"Can not log in!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Signup.class));
            }
        });
        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
}

