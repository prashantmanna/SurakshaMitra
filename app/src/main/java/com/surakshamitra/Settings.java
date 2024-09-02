package com.surakshamitra;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class Settings extends Fragment {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private static final int CONTACTS_PERMISSION_REQUEST_CODE = 2;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 3;
    private static final int MICROPHONE_PERMISSION_REQUEST_CODE = 4;
    private static final int PHONE_PERMISSION_REQUEST_CODE = 5;

    private Switch cameraSwitch, contactsSwitch, locationSwitch, microphoneSwitch, phoneSwitch;
    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        cameraSwitch = view.findViewById(R.id.cameraSwitch);
        contactsSwitch = view.findViewById(R.id.contactswitch);
        locationSwitch = view.findViewById(R.id.locationwitch);
        microphoneSwitch =view.findViewById(R.id.microphoneSwitch);
        phoneSwitch = view.findViewById(R.id.phoneSwitch);

        cameraSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    requestCameraPermission();
                }
                else
                {
                    denyCameraPermission();
                }
            }
        });

        contactsSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    requestContactsPermission();
                }
                else {
                    // User has turned off the switch, handle the logic for denying permission
                    denyContactsPermission();
                }
            }
        });

        locationSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    requestLocationPermission();
                }
                else {
                    denyLocationPermission();
                }
            }
        });

        microphoneSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    requestMicrophonePermission();
                }
                else {
                    denyMicrophonePermission();
                }
            }
        });

        phoneSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    requestPhonePermission();
                }
                else {
                    denyPhonePermission();
                }
            }
        });

        return view;
    }

    private void denyLocationPermission() {
        // You can show a message to the user indicating that the contacts permission is required for certain features of the app
        Toast.makeText(requireContext(), "Please enable Location permission to access this feature", Toast.LENGTH_SHORT).show();
        // You can also programmatically turn on the switch again if necessary
        contactsSwitch.setChecked(false);
    }

    private void denyMicrophonePermission() {
        // You can show a message to the user indicating that the contacts permission is required for certain features of the app
        Toast.makeText(requireContext(), "Please enable record audio permission to access this feature", Toast.LENGTH_SHORT).show();
        // You can also programmatically turn on the switch again if necessary
        contactsSwitch.setChecked(false);

    }

    private void denyPhonePermission() {
// You can show a message to the user indicating that the contacts permission is required for certain features of the app
        Toast.makeText(requireContext(), "Please enable call phone permission to access this feature", Toast.LENGTH_SHORT).show();
        // You can also programmatically turn on the switch again if necessary
        contactsSwitch.setChecked(false);
    }

    private void denyCameraPermission() {// You can show a message to the user indicating that the contacts permission is required for certain features of the app
        Toast.makeText(requireContext(), "Please enable camera permission to access this feature", Toast.LENGTH_SHORT).show();
        // You can also programmatically turn on the switch again if necessary
        contactsSwitch.setChecked(false);

    }

    private void denyContactsPermission() {
        // You can show a message to the user indicating that the contacts permission is required for certain features of the app
        Toast.makeText(requireContext(), "Please enable contacts permission to access this feature", Toast.LENGTH_SHORT).show();
        // You can also programmatically turn on the switch again if necessary
        contactsSwitch.setChecked(false);
    }


    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            Toast.makeText(requireContext(), "Camera permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestContactsPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, CONTACTS_PERMISSION_REQUEST_CODE);
        } else {
            Toast.makeText(requireContext(), "Contacts permission is already granted", Toast.LENGTH_SHORT).show();
        }
    }


    private void requestLocationPermission() {
        if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);

        }
        else
        {
            Toast.makeText(requireContext(), "Location permission is already granted", Toast.LENGTH_SHORT).show();
        }

    }

    private void requestMicrophonePermission() {
        if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.RECORD_AUDIO},MICROPHONE_PERMISSION_REQUEST_CODE);
        }
        else
        {
            Toast.makeText(requireContext(), "Record Audio Permission Granted", Toast.LENGTH_SHORT).show();
        }

    }

    private void requestPhonePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE}, PHONE_PERMISSION_REQUEST_CODE);
        }
        else
        {
            Toast.makeText(requireContext(), "Call Phone permission granted", Toast.LENGTH_SHORT).show();
        }
    }

}