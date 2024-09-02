package com.surakshamitra;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Profile extends Fragment {

    private EditText editTextName, editTextEmail, editTextMobile,editTextPassword;
    private Button buttonUpdate;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ImageView uploadImage;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("RegisteredUsers").child(user.getUid());

        editTextName = view.findViewById(R.id.uptName);
        editTextEmail = view.findViewById(R.id.uptEmail);
        editTextMobile = view.findViewById(R.id.uptMobile);
        editTextPassword = view.findViewById(R.id.uptPassword);
        buttonUpdate = view.findViewById(R.id.updateuserProfile);
        uploadImage = view.findViewById(R.id.uploadImage);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to pick an image from the gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                // Start the Intent
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        // Retrieve current user details and populate the EditText fields
        if (user != null) {
            editTextName.setText(user.getDisplayName());
            editTextEmail.setText(user.getEmail());


        }

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        return view;
    }

    private void updateUserProfile() {
        String username = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String mobile = editTextMobile.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordConfirm = editTextPassword.getText().toString().trim();


        mDatabase.child("username").setValue(username);
        mDatabase.child("email").setValue(email);
        mDatabase.child("mobile").setValue(mobile);
        mDatabase.child("password").setValue(password);
        mDatabase.child("passwordConfirm").setValue(passwordConfirm);
        // mDatabase.child("mobile").setValue(mobile);

        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the request code is the same as what is passed to startActivityForResult()
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            // Create a reference to store the image in Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("profile_images/" + mAuth.getCurrentUser().getUid());

            // Upload the image to Firebase Storage
            storageRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully, get the download URL
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    // Save the download URL in Firebase Realtime Database
                                    mDatabase.child("profileImageUrl").setValue(downloadUri.toString());

                                    // Optionally, display the uploaded image
                                    uploadImage.setImageURI(uri);

                                    Toast.makeText(requireContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle unsuccessful uploads
                            Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
