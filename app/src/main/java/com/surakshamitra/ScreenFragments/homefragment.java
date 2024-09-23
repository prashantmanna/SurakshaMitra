package com.surakshamitra.ScreenFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.surakshamitra.GpsDialogUtils;
import com.surakshamitra.MapActivity;
import com.surakshamitra.R;
import com.surakshamitra.contactsDial;
import com.surakshamitra.currentUserLocation;
import com.surakshamitra.model;

import java.util.ArrayList;

public class homefragment extends Fragment {

    CardView myLocation;
    ConstraintLayout policeCall,ambulanceCall,childCall,womenCall;

    ImageButton imgBtn1,imgBtn2,imgBtn3,imgBtn4,imgBtn5;
    private static final int MY_SMS_PERMISSION_REQUEST = 2;
    private TextInputEditText mobileNumberEditText;
    private Button sendLocationButton;
    CardView img1, img2, img3, img4;
    LottieAnimationView sendAnime;
    ConstraintLayout cardlocation;
    private static final int MY_PHONE_CALL_PERMISSION_REQUEST = 1;

    FloatingActionButton contactsCall;
    MaterialButton medicalCall,policeCallBtn,womenHelpBtn,childHelpBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homefragment, container, false);
        ImageSlider imageSlider = view.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.women8, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.women7, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.women9, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.women6, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.women4, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        medicalCall = view.findViewById(R.id.medicalCall);
        policeCallBtn = view.findViewById(R.id.policeCallBtn);
        womenHelpBtn = view.findViewById(R.id.womenHelpBtn);
        childHelpBtn = view.findViewById(R.id.childHelpBtn);

        img1 = view.findViewById(R.id.ambulance);
        img2 = view.findViewById(R.id.police);
        img3 = view.findViewById(R.id.women);
        img4 = view.findViewById(R.id.child);
        sendAnime = view.findViewById(R.id.sendAnime);
        sendAnime.animate().setDuration(20).setStartDelay(0);
        cardlocation = view.findViewById(R.id.cardLocation);
        myLocation = view.findViewById(R.id.myLocation);

        contactsCall = view.findViewById(R.id.contactsCall);
        contactsCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(requireContext(), contactsDial.class);
               startActivity(intent);
            }
        });


        medicalCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+"+91108"));
                    startActivity(intent);
                }
                else
                {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PHONE_CALL_PERMISSION_REQUEST);
                }
            }
        });


        policeCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+"+91112"));
                    startActivity(intent);
                }
                else
                {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PHONE_CALL_PERMISSION_REQUEST);
                }
            }
        });
        womenHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+"+911091"));
                    startActivity(intent);
                }
                else
                {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, MY_PHONE_CALL_PERMISSION_REQUEST);
                }
            }
        });

        childHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED)
                {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "+911098"));
                    startActivity(callIntent);
                }

                else
                {
                    askForPermission();
                    ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CALL_PHONE},1);

                }
            }
        });

        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container,new currentUserLocation());
                ft.commit();

            }
        });

        cardlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container,new currentUserLocation());
                ft.commit();
                getCurrentLocation();
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        imgBtn1 = view.findViewById(R.id.police2);
        imgBtn2 = view.findViewById(R.id.doctor);
        imgBtn3 = view.findViewById(R.id.train);
        imgBtn4 = view.findViewById(R.id.bus);
        imgBtn5 = view.findViewById(R.id.shop);

        imgBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), MapActivity.class);
                startActivity(intent);
            }
        });
        imgBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),MapActivity.class);
                startActivity(intent);

            }
        });
        imgBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),MapActivity.class);
                startActivity(intent);
            }
        });
        imgBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(requireContext(),MapActivity.class);
               startActivity(intent);
            }
        });
        imgBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(),MapActivity.class);
                startActivity(intent);
            }
        });

        if (!GpsDialogUtils.isGpsEnabled(requireContext())) {
            GpsDialogUtils.showEnableGpsDialog(requireContext());
        }

        checkAndRequestPermissions();

        return view;
    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Task<Location> task = fusedLocationClient.getLastLocation();
            task.addOnSuccessListener(requireActivity(), location -> {
                if (location != null) {
                    sendLiveLocationToContacts(location.getLatitude(), location.getLongitude());
                } else {
                    // Handle case when location is null
                    Toast.makeText(getContext(), "Failed to get current location", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_SMS_PERMISSION_REQUEST);
        }
    }

    private void sendLiveLocationToContacts(double latitude, double longitude) {
        ArrayList<model> contacts = getAllContactsForSMS();
        String message = "I'm currently at: https://maps.google.com/?q=" + latitude + "," + longitude;
        for (model contact : contacts) {
            sendSMS(contact.getContact(), message);
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getContext(), "SMS sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to send SMS to " + phoneNumber, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



    private void askForPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + "+917045708641"));
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "You cannot call without accepting this permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, MY_SMS_PERMISSION_REQUEST);
        }
    }

    private ArrayList<model> getAllContactsForSMS() {
        ArrayList<model> contactsList = new ArrayList<>();
        return contactsList;
    }
}
