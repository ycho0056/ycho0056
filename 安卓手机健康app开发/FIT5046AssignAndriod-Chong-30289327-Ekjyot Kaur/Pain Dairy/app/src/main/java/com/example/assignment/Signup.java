package com.example.assignment;

import android.content.Intent;
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

public class Signup extends AppCompatActivity {
    EditText mEmail,mPassword,mConfirmpassword;
    Button mRegister;
    Button mLogin;
    CheckBox mCheckbox;
    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
        mConfirmpassword=findViewById(R.id.passwordConfirm);
        mRegister=findViewById(R.id.register);
        mLogin=findViewById(R.id.login);
        mCheckbox=findViewById(R.id.Checkbox);
        auth=FirebaseAuth.getInstance();


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                String password2=mConfirmpassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email cannot be empty");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password cannot be empty");
                    return;
                }
                if(password.length()< 6){
                    mPassword.setError("Password must more than 6 character");
                }
                if (!password.equals(password2)){
                    mConfirmpassword.setError("Password must be as same as the above");
                    return;
                }
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Signup.this,"User created",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Signin.class));
                        }
                        else
                        {
                            Toast.makeText(Signup.this,"Can not register!"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Signin.class));
            }
        });

        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mConfirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mConfirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }
}
