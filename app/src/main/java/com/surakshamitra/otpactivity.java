package com.surakshamitra;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpactivity extends AppCompatActivity {

    EditText otp1, otp2, otp3, otp4, otp5, otp6,editText;
    Button btnVerify;
    TextView resendOtp, number;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        otp1 = findViewById(R.id.edtOtp1);
        otp2 = findViewById(R.id.edtOtp2);
        otp3 = findViewById(R.id.edtOtp3);
        otp4 = findViewById(R.id.edtOtp4);
        otp5 = findViewById(R.id.edtOtp5);
        otp6 = findViewById(R.id.edtOtp6);
        btnVerify = findViewById(R.id.btnOtpVerify);
        resendOtp = findViewById(R.id.resendotp);
        number = findViewById(R.id.userNumber);
        progressBar = findViewById(R.id.progress_verify_otp);

        number.setText(String.format(
                "+91-%s", getIntent().getStringExtra("mobile")
        ));

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP();
            }
        });

        numberOtpMove();
    }

    private void numberOtpMove() {
        otp1.addTextChangedListener(new GenericTextWatcher(editText, otp1));
        otp2.addTextChangedListener(new GenericTextWatcher(editText, otp2));
        otp3.addTextChangedListener(new GenericTextWatcher(editText, otp3));
        otp4.addTextChangedListener(new GenericTextWatcher(editText, otp4));
        otp5.addTextChangedListener(new GenericTextWatcher(editText, otp5));
        otp6.addTextChangedListener(new GenericTextWatcher(editText, otp6));
    }


    private class GenericTextWatcher implements TextWatcher {

        private final EditText editText;
        private final View view;

        public GenericTextWatcher(EditText editText, View view) {
            this.editText = editText;
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            if (text.length() == 1) {
                EditText nextEditText = null;
                if (editText == otp1) {
                    nextEditText = otp2;
                } else if (editText == otp2) {
                    nextEditText = otp3;
                } else if (editText == otp3) {
                    nextEditText = otp4;
                } else if (editText == otp4) {
                    nextEditText = otp5;
                } else if (editText == otp5) {
                    nextEditText = otp6;
                }

                if (nextEditText != null) {
                    nextEditText.requestFocus();
                }
            }
        }

    }



    private void verifyOTP() {
        String otp = otp1.getText().toString() +
                otp2.getText().toString() +
                otp3.getText().toString() +
                otp4.getText().toString() +
                otp5.getText().toString() +
                otp6.getText().toString();

        if (TextUtils.isEmpty(otp) || otp.length() != 6) {
            Toast.makeText(this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        String verificationId = getIntent().getStringExtra("verificationId");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Verification successful, navigate to the next activity
                            startActivity(new Intent(otpactivity.this, MainActivity.class));
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // Invalid verification code
                                Toast.makeText(otpactivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            } else {
                                // Other error
                                Toast.makeText(otpactivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
