package com.surakshamitra.Screens.AllScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.surakshamitra.R;

public class ForgetActivity extends AppCompatActivity {

    MaterialButton reset, back;
    TextInputEditText edtResetEmail;
    FirebaseAuth auth;

    ProgressBar progress;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        reset = findViewById(R.id.resetBtn);
        edtResetEmail = findViewById(R.id.resetEmail);
        back = findViewById(R.id.backBtn);
        progress = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = String.valueOf(edtResetEmail.getText());
                if (TextUtils.isEmpty(email)) {
                    edtResetEmail.setError("Email ID is required");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtResetEmail.setError("Please enter a valid email address");
                } else {
                    // Check if email exists
                    checkIfEmailExistsAndReset();
                }
            }
        });
    }

    private void checkIfEmailExistsAndReset() {
        // Check if the email exists in Firebase Authentication
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            if (result != null && result.getSignInMethods() != null
                                    && result.getSignInMethods().size() > 0) {
                                // Email exists, proceed with password reset
                                forgotPass();
                            } else {
                                // Email does not exist
                                Toast.makeText(ForgetActivity.this, "This email is not registered", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Error occurred while checking email existence
                            Toast.makeText(ForgetActivity.this, "Error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void forgotPass() {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgetActivity.this, "Verify Your Email", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgetActivity.this, LoginActivity.class));
                    finish();
                    progress.setVisibility(View.VISIBLE);
                    reset.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(ForgetActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                    reset.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
