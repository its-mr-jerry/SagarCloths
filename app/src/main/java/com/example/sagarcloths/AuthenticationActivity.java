package com.example.sagarcloths;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.ktx.Firebase;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class AuthenticationActivity extends AppCompatActivity {
    private TextView createAccount, forgetPassword, PhoneSignIn;
    private EditText email, password, resetPassTxt,PhoneNo;
    private FirebaseAuth auth;
    private Button signInBtn, reset, cancel, GetOtp;
    private GoogleSignInClient mGoogleSignInClient;
    Intent intent;
    ImageView googleBtn,facebookBtn;
    String enterMail;
    RelativeLayout authLayout;
    int RC_SIGN_IN=9001,a;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_authentication);

        createAccount = findViewById(R.id.createAccount);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signInBtn = findViewById(R.id.signInBtn);
        forgetPassword = findViewById(R.id.forgotPass);
        googleBtn = findViewById(R.id.googleBtn);
        PhoneSignIn = findViewById(R.id.PhoneSignIn);
        authLayout= findViewById(R.id.mainLayout);
        facebookBtn=findViewById(R.id.facebookBtn);

        auth = FirebaseAuth.getInstance();
      // Buttons
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthenticationActivity.this, SignUp.class);
                startActivity(intent);

            }
        });
        signInBtn.setOnClickListener(view -> verification());
        forgetPassword.setOnClickListener(view -> showDialogue());
        PhoneSignIn.setOnClickListener(view -> phoneDialoge());
        googleBtn.setOnClickListener(view -> {
            signIn();
            a++;
        });
        facebookBtn.setOnClickListener(view ->{
            mCallbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(),"Log IN Canceld", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } );



        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
               auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           FirebaseUser user =task.getResult().getUser();
                           updateUI(user);
                           startActivity(intent);
                       }
                       else {
                           Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   }
               });
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                GetOtp.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCodeSent(@NonNull String otpCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                GetOtp.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getApplicationContext(), OTPverification.class);
                intent.putExtra("otp", otpCode);
                intent.putExtra("phoneNo.", PhoneNo.getText().toString());
                startActivity(intent);
            }
        }
        ;
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("824443235397-jsutn1vsnsnkg6hpuhiiltkfrfc0nrtn.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Snackbar.make(authLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                    }});}
    private void updateUI(FirebaseUser Account) {
        if (Account != null) {
            intent = new Intent(AuthenticationActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),"Something wrong in Creating Account", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser!=null && currentUser.isEmailVerified() || !currentUser.getPhoneNumber().isEmpty()){
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void verification() {

        auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(AuthenticationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user.isEmailVerified()) {
                        intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AuthenticationActivity.this, "Your Email is not verified", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AuthenticationActivity.this, "Wrong credential", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showDialogue() {
        final AlertDialog.Builder resetPassword = new AlertDialog.Builder(AuthenticationActivity.this);
        View myView = getLayoutInflater().inflate(R.layout.resetpassword, null);

        resetPassTxt = myView.findViewById(R.id.resetPass);
        reset = myView.findViewById(R.id.reset);
        cancel = myView.findViewById(R.id.cancel);
        resetPassword.setView(myView);
        final AlertDialog alertDialog = resetPassword.create();
        alertDialog.setCanceledOnTouchOutside(false);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset.setVisibility(View.INVISIBLE);
                enterMail = resetPassTxt.getText().toString();


                if (enterMail.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Email or Phone no.", Toast.LENGTH_SHORT).show();
                    reset.setVisibility(View.VISIBLE);
                } else {
                    auth.sendPasswordResetEmail(enterMail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Password Reset link sent to your Email", Toast.LENGTH_SHORT).show();

                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage() + "", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        cancel.setOnClickListener(view -> alertDialog.dismiss());

        alertDialog.show();
    }

    private void phoneDialoge() {
        final AlertDialog.Builder mobileSignIn = new AlertDialog.Builder(AuthenticationActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.phone_auth, null);

        PhoneNo = mView.findViewById(R.id.PhoneNo);
        GetOtp = mView.findViewById(R.id.getOtp);
        mobileSignIn.setView(mView);
        final AlertDialog alertDialog = mobileSignIn.create();
        alertDialog.setCanceledOnTouchOutside(false);

        GetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PhoneNo.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Phone Number can't be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    GetOtp.setClickable(false);
                createAccountWithPhone();}
            }
        });
        alertDialog.show();

    }

    private void createAccountWithPhone() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(PhoneNo.getText().toString())       // Phone number to verify
                        .setTimeout(45L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(a==1){
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            a=0;
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

            }
        }}
        else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(AuthenticationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }



}