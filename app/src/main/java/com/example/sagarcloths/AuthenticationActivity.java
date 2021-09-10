package com.example.sagarcloths;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class AuthenticationActivity extends AppCompatActivity {
   private TextView createAccount,forgetPassword;
    private EditText email,password,resetPassTxt;
   private FirebaseAuth auth;
   private Button signInBtn,reset,cancel;
   Intent intent;
   String enterMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_authentication);

        createAccount= findViewById(R.id.createAccount);
        email = findViewById(R.id.email);
        password= findViewById(R.id.password);
        signInBtn = findViewById(R.id.signInBtn);
        forgetPassword = findViewById(R.id.forgotPass);

        auth=FirebaseAuth.getInstance();



        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthenticationActivity.this,SignUp.class);
                startActivity(intent);

            }
        });

        signInBtn.setOnClickListener(view -> verification());
        forgetPassword.setOnClickListener(view -> showDialogue() );}

           private void verification(){

               auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(AuthenticationActivity.this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           FirebaseUser user = auth.getCurrentUser();
                           if (user.isEmailVerified()){
                               intent = new Intent(AuthenticationActivity.this,MainActivity.class);
                               startActivity(intent);
                           }
                           else {
                               Toast.makeText(AuthenticationActivity.this, "Your Email is not verified", Toast.LENGTH_SHORT).show();
                           }
                       }
                       else{
                           Toast.makeText(AuthenticationActivity.this, "Wrong credential", Toast.LENGTH_SHORT).show();
                       }
                   }
               });


    }

          private void showDialogue(){
          final AlertDialog.Builder resetPassword = new AlertDialog.Builder(AuthenticationActivity.this);
          View myView = getLayoutInflater().inflate(R.layout.resetpassword,null);

           resetPassTxt =myView.findViewById(R.id.resetPass);
          reset = myView.findViewById(R.id.reset);
          cancel = myView.findViewById(R.id.cancel);
          resetPassword.setView(myView);
          final AlertDialog alertDialog = resetPassword.create();
          alertDialog.setCanceledOnTouchOutside(false);
              enterMail = resetPassTxt.getText().toString();

          resetPassTxt.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

              }

              @Override
              public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

              }

              @Override
              public void afterTextChanged(Editable editable) {
                  if (Character.isDigit(enterMail.indexOf(0))){
                      resetPassTxt.setText("+"+91+enterMail);
                      enterMail = resetPassTxt.getText().toString();
                  }
              }
          });


          reset.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
               if (enterMail.isEmpty()){
                   Toast.makeText(getApplicationContext(), "Enter Email or Phone no.", Toast.LENGTH_SHORT).show();
               }
               auth.sendPasswordResetEmail(enterMail).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                       Toast.makeText(getApplicationContext(),"Password Reset link sent to your Email",Toast.LENGTH_SHORT).show();

                       alertDialog.dismiss();
                   }
                       else {
                           Toast.makeText(getApplicationContext(),task.getException()+"",Toast.LENGTH_SHORT).show();

                       }
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getApplicationContext(), e.getMessage()+"", Toast.LENGTH_SHORT).show();
                   }
               });
              }
          });

          cancel.setOnClickListener(view -> alertDialog.dismiss());

          alertDialog.show();

    }
}