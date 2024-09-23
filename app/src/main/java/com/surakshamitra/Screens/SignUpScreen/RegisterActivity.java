package com.surakshamitra.Screens.SignUpScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.surakshamitra.LoginActivity;
import com.surakshamitra.MainActivity;
import com.surakshamitra.R;
import com.surakshamitra.userDetails;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText Name, Email, Password, MobileNo, ConfirmPassword;
    Button Register;
    ProgressBar progressBar;
    ConstraintLayout loginBack;
    FirebaseAuth auth;
    TextView loginIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toast.makeText(RegisterActivity.this, "You Can register Now", Toast.LENGTH_SHORT).show();

        loginIcon = findViewById(R.id.loginIcon);
        Name = findViewById(R.id.edtName);
        Email = findViewById(R.id.edtEmail);
        Password = findViewById(R.id.edtPassword);
        MobileNo = findViewById(R.id.edtMobile);
        ConfirmPassword = findViewById(R.id.edtConfirmPassword);
        Register = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progress);
        auth = FirebaseAuth.getInstance();
        loginBack = findViewById(R.id.loginBack);

        loginIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                Toast.makeText(RegisterActivity.this, "Login Page", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String username = String.valueOf(Name.getText());
                String userEmail = String.valueOf(Email.getText());
                String userPassword = String.valueOf(Password.getText());
                String mobile = String.valueOf(MobileNo.getText());
                String passwordConfirm = String.valueOf(ConfirmPassword.getText());

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                    Name.setError("Name Is Required");
                    Name.requestFocus();
                    return;
                } else if (!username.matches("[a-zA-Z]+")) {
                    Toast.makeText(RegisterActivity.this, "Name should contain only letters", Toast.LENGTH_SHORT).show();
                    Name.setError("Name should contain only letters");
                    Name.requestFocus();
                    return;
                }

                // Validate email
                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    Email.setError("Email is required");
                    Email.requestFocus();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    Email.setError("Valid email is required");
                    Email.requestFocus();
                    return;
                }

                // Validate mobile number
                if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Mobile Number", Toast.LENGTH_SHORT).show();
                    MobileNo.setError("Mobile No. is required");
                    MobileNo.requestFocus();
                    return;
                } else if (mobile.length() != 10 || !TextUtils.isDigitsOnly(mobile)) {
                    Toast.makeText(RegisterActivity.this, "Mobile number should be 10 digits long", Toast.LENGTH_SHORT).show();
                    MobileNo.setError("Invalid mobile number");
                    MobileNo.requestFocus();
                    return;
                }

                // Validate password
                if (TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    Password.setError("Password is required");
                    Password.requestFocus();
                    return;
                } else if (userPassword.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    Password.setError("Password is too short");
                    Password.requestFocus();
                    return;
                }

                // Validate password confirmation
                if (!userPassword.equals(passwordConfirm)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    ConfirmPassword.setError("Passwords do not match");
                    ConfirmPassword.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                Register.setVisibility(View.GONE);
                loginBack.setVisibility(View.GONE);
                registerUser(username, userEmail, mobile, userPassword,passwordConfirm);
            }
        });
    }

    private void registerUser(String username, String userEmail, String mobile, String userPassword,String passwordConfirm) {
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                                firebaseUser.updateProfile(profileChangeRequest)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> profileTask) {
                                                if (profileTask.isSuccessful()) {
                                                    userDetails user = new userDetails(username, userEmail, mobile, userPassword,passwordConfirm);
                                                    DatabaseReference userDetailsRef = FirebaseDatabase.getInstance().getReference("RegisteredUsers");
                                                    userDetailsRef.child(firebaseUser.getUid()).setValue(user)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> databaseTask) {
                                                                    if (databaseTask.isSuccessful()) {
                                                                        Toast.makeText(RegisterActivity.this, "Registration Successful. Please verify your email address.", Toast.LENGTH_SHORT).show();
                                                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                                        finish();
                                                                    } else {
                                                                        Register.setVisibility(View.VISIBLE);
                                                                        loginBack.setVisibility(View.VISIBLE);
                                                                        Toast.makeText(RegisterActivity.this, "Registration failed: " + databaseTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    Register.setVisibility(View.VISIBLE);
                                                    loginBack.setVisibility(View.VISIBLE);
                                                    Toast.makeText(RegisterActivity.this, "Registration failed: " + profileTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Register.setVisibility(View.VISIBLE);
                            loginBack.setVisibility(View.VISIBLE);
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
