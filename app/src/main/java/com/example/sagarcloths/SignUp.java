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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.ktx.Firebase;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

    TextView alreadyAccount;
    EditText email, password, cnfPassword;
    Button signUp;
    String pass, phone;
    private FirebaseAuth auth;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        alreadyAccount = findViewById(R.id.alreadyAccount);
        email = findViewById(R.id.emailId);
        password = findViewById(R.id.password);
        cnfPassword = findViewById(R.id.confirmPassword);
        signUp = findViewById(R.id.createAccountBtn);

        auth = FirebaseAuth.getInstance();

        alreadyAccount.setOnClickListener(view -> finish());
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp.setVisibility(View.INVISIBLE);
                String emailId = email.getText().toString();

                int a = 0;
                for (int n = 0; n <= emailId.length() - 1; n++) {
                    if (!Character.isDigit(emailId.charAt(n))) {
                        a++;
                        break;
                    }
                }

                if (emailId.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Email or Phone no.", Toast.LENGTH_SHORT).show();
                }

                else {
                    createAccountEmail();
                    signUp.setVisibility(View.VISIBLE);

                }
            }
        });


    }

    public void createAccountEmail() {
        if (password.getText().toString().equals(cnfPassword.getText().toString())) {
            pass = cnfPassword.getText().toString();
            String emaill = email.getText().toString();
            System.out.println(emaill + pass);

            auth.createUserWithEmailAndPassword(emaill, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                                Toast.makeText(SignUp.this, task1.getException() + "", Toast.LENGTH_SHORT).show();
                            }

                        });
                    } else {
                        Toast.makeText(SignUp.this, task.getException() + "", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void updateUI(FirebaseUser Account) {
        if (Account != null) {
            intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(SignUp.this, "Something wrong in Creating Account", Toast.LENGTH_SHORT).show();
        }
    }



}
