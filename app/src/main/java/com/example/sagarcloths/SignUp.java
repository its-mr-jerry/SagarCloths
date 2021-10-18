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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.ktx.Firebase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

    private static EditText Name;
    private TextView alreadyAccount;
   private EditText email;
   private EditText password;
   private EditText cnfPassword;
    Button signUp;
    String pass;
    private FirebaseAuth auth;
    Intent intent;

    public static EditText getName() {
        return Name;
    }


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
        Name = findViewById(R.id.Name);

        auth = FirebaseAuth.getInstance();

        alreadyAccount.setOnClickListener(view -> finish());
        signUp.setOnClickListener(view -> {
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
            else if (Name.getText().toString().isEmpty()){
                Toast.makeText(this, "Name Can't be Empty", Toast.LENGTH_SHORT).show();
            }
            else {
                createAccountEmail();
                signUp.setVisibility(View.VISIBLE);

            }
        });


    }

    public void createAccountEmail() {
        if (password.getText().toString().equals(cnfPassword.getText().toString())) {
            pass = cnfPassword.getText().toString();
            String emaill = email.getText().toString();

            auth.createUserWithEmailAndPassword(emaill, pass).addOnCompleteListener(task -> {
                FirebaseUser user = auth.getCurrentUser();
                if (task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Account created Successfully", Toast.LENGTH_SHORT).show();
                    assert user != null;
                    user.sendEmailVerification().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Email verification link sent to your Email", Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            Toast.makeText(SignUp.this, Objects.requireNonNull(task1.getException()).getMessage() + "", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (task.getException() instanceof FirebaseAuthUserCollisionException && Objects.requireNonNull(user).isEmailVerified()) {
                    Toast.makeText(getApplicationContext(), "You arlready have an account", Toast.LENGTH_SHORT).show();
                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    assert user != null;
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Verification Link sent to your Email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }

    private void updateUI(FirebaseUser Account) {

        if (Account != null) {
            intent = new Intent(this,UserInfo.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(SignUp.this, "Something wrong in Creating Account", Toast.LENGTH_SHORT).show();
        }
    }


}
