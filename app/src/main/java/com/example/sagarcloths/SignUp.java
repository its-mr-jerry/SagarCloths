package com.example.sagarcloths;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class SignUp extends AppCompatActivity {

    TextView alreadyAccount;
    EditText email,password,cnfPassword;
    Button signUp;
    String pass;
    private FirebaseAuth auth;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        alreadyAccount = findViewById(R.id.alreadyAccount);
        email = findViewById(R.id.phoneNo_);
        password =findViewById(R.id.password);
        cnfPassword=findViewById(R.id.confirmPassword);
        signUp=findViewById(R.id.createAccountBtn);

        auth = FirebaseAuth.getInstance();

        alreadyAccount.setOnClickListener(view -> finish());
        signUp.setOnClickListener(view -> CreateAccountEmail());
    }
    public void CreateAccountEmail(){
        if (password.getText().toString().equals(cnfPassword.getText().toString())){
            pass = cnfPassword.getText().toString();
            String emaill = email.getText().toString();
            System.out.println(emaill+pass);

            auth.createUserWithEmailAndPassword(emaill,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUp.this, "Account created Successfully", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = auth.getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(SignUp.this, task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(SignUp.this, "Email verification link sent to your Email", Toast.LENGTH_SHORT).show();
                                updateUI(user);
                            } else {
                                Toast.makeText(SignUp.this, task1.getException()+"", Toast.LENGTH_SHORT).show();
                            }

                        });
                    }
                    else{
                        Toast.makeText(SignUp.this, task.getException()+"", Toast.LENGTH_SHORT).show();
                    }
                }});
        }
    }
        private void updateUI(FirebaseUser Account){
        if (Account!=null){
            intent=new Intent(this,AuthenticationActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(SignUp.this, "Something wrong in Creating Account", Toast.LENGTH_SHORT).show();
        }
        }
        private void Authentication(){
            }
    }
