package com.example.sagarcloths;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

public class OTPverification extends AppCompatActivity {
    private TextView otpSentOn,timer;
    String phoneNO;
    EditText otp;
    private Button submit, resend;
    String otpBackend;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        otpSentOn = findViewById(R.id.otpSentOn);
        submit = findViewById(R.id.submit);
        resend = findViewById(R.id.resend);
        otp = findViewById(R.id.otp);
        timer=findViewById(R.id.timer);

        auth=FirebaseAuth.getInstance();

        phoneNO = getIntent().getStringExtra("phoneNo.");
        otpSentOn.setText(phoneNO);
        otpBackend = getIntent().getStringExtra("otp");
        timer();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otp.toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "OTP can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (otpBackend == null) {
                        Toast.makeText(getApplicationContext(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                    } else {
                        PhoneAuthCredential phoneAuth = PhoneAuthProvider.getCredential(otpBackend, otp.getText().toString());
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuth).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(OTPverification.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resend.setClickable(false);
                timer();
                createAccountWithPhone();
            }
        });
        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String newOtpBackend, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                otpBackend=newOtpBackend;
                Toast.makeText(getApplicationContext(), "OTP sent", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        }
        ;

    }
    private void createAccountWithPhone() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNO)       // Phone number to verify
                        .setTimeout(45L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private void timer(){
        new CountDownTimer(45000, 1000) {
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                long sec = (millisUntilFinished / 1000) % 60;
                timer.setText("Resend after "+f.format(sec));
            }
            public void onFinish() {
                timer.setText("00");
                resend.setClickable(true);
            }
        }.start();
    }

}