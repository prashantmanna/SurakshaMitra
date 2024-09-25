package com.surakshamitra.ScreenFragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.surakshamitra.DBhelper;
import com.surakshamitra.R;
import com.surakshamitra.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AlertFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE_SMS = 1000;
    private static final String PERMISSION_SMS = Manifest.permission.SEND_SMS;
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 200;

    private static final int REQUEST_CODE_SEND_MMS = 1;

    ImageView sos;
    DBhelper dBhelper;

    CircleImageView img2;

    CardView cardAlert;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert, container, false);
        sos = view.findViewById(R.id.sos);
        dBhelper = new DBhelper(requireContext());

        cardAlert = view.findViewById(R.id.cardAlert);


        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check and request permissions
                checkAndRequestPermissions();
            }
        });

        return view;
    }


    private String getAudioFilePath() {
        return requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/audio_record.3gp";
    }
    private boolean checkAndRequestAudioPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSION_REQUEST_RECORD_AUDIO);
            return false;
        }
        return true;
    }





    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            // Permissions already granted, proceed to sending alert SMS
            sendAlertSMS();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), PERMISSION_SMS)) {
            // Permission rationale dialog
            showPermissionRationaleDialog();
        } else {
            // Request the permission
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE_SMS);
        }
    }

    private void sendAlertMMS(String audioFilePath, String name, String number) {
        try {
            // Create an intent to send an MMS
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra("address", number);
            sendIntent.putExtra("sms_body", "Alert! This is an emergency message");
            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(audioFilePath)));
            sendIntent.setType("audio/*");
            startActivity(Intent.createChooser(sendIntent, "Send via"));

            Log.d("MMS", "Sent MMS to " + name + " (" + number + ")");
            // Show toast message indicating MMS was sent successfully
            Toast.makeText(requireContext(), "MMS sent to " + name, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("MMS", "Error sending MMS to " + name + " (" + number + ")", e);
            Toast.makeText(requireContext(), "Error sending MMS to " + name, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to sending alert SMS
                sendAlertSMS();
            } else {
                Toast.makeText(requireContext(), "Permission denied. Cannot send SMS.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendAlertSMS() {
        ArrayList<model> contactsList = dBhelper.getAllContactsForSMS();

        if (contactsList.isEmpty()) {
            Toast.makeText(requireContext(), "No contacts found", Toast.LENGTH_SHORT).show();
            return;
        }

        int totalContacts = contactsList.size();
        int sentCount = 0;

        for (model contact : contactsList) {
            String name = contact.getContact();
            String number = contact.getNumber();

            if (isValidPhoneNumber(number)) {
                if (sendSMS(name, number)) {
                    sentCount++;
                }
            } else {
                Toast.makeText(requireContext(), "Invalid phone number: " + number, Toast.LENGTH_SHORT).show();
            }
        }

        if (sentCount == totalContacts) {
            Toast.makeText(requireContext(), "SMS sent to all contacts", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Failed to send SMS to some contacts", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidPhoneNumber(String number) {
        // Simple validation for demonstration purposes
        return number != null && !number.isEmpty() && number.matches("\\d+");
    }

    private boolean sendSMS(String name, String number) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String message = "Alert! This is an emergency message";
            smsManager.sendTextMessage(number, null, message, null, null);
            Log.d("SMS", "Sent SMS to " + name + " (" + number + ")");
            return true; // Return true indicating successful message sending
        } catch (Exception e) {
            Log.e("SMS", "Error sending SMS to " + name + " (" + number + ")", e);
            Toast.makeText(requireContext(), "Error sending SMS to " + name, Toast.LENGTH_SHORT).show();
            return false; // Return false indicating failed message sending
        }
    }

    private void showPermissionRationaleDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
        dialog.setMessage("This app requires SEND_SMS permission for a particular feature")
                .setTitle("PERMISSION REQUIRED")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Request the permission
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE_SMS);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SEND_MMS) {
            if (resultCode == RESULT_OK) {
                // MMS sent successfully
                Log.d("MMS", "MMS sent successfully");
                Toast.makeText(requireContext(), "MMS sent successfully", Toast.LENGTH_SHORT).show();
            } else {
                // MMS sending failed or cancelled
                Log.e("MMS", "MMS sending failed or cancelled");
                Toast.makeText(requireContext(), "MMS sending failed or cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
