package com.surakshamitra;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.surakshamitra.ScreenFragments.AlertFragment;
import com.surakshamitra.ScreenFragments.chatfragment;
import com.surakshamitra.ScreenFragments.contactFragment;
import com.surakshamitra.ScreenFragments.homefragment;
import com.surakshamitra.ScreenFragments.safetyfragment;

import java.util.ArrayList;

public class contactsDial extends AppCompatActivity {

    RecyclerView recyclerViewContacts;
    ArrayList<model> contactList;
    contactcallAdaptor adapter;

    DBhelper dBhelper;

    Toolbar toolbar;

    BottomNavigationView bottomNavigationView;

    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_dial);

        toolbar = findViewById(R.id.toolDial);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        frameLayout = findViewById(R.id.fragment_container);

        bottomNavigationView = findViewById(R.id.navBar1);

        dBhelper = new DBhelper(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.homepage) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, new homefragment());
                    recyclerViewContacts.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                    getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);

                    ft.addToBackStack(null);
                    ft.commit();
                }else if (id == R.id.alert) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, new AlertFragment());
                    recyclerViewContacts.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                    // Set custom layout for ActionBar
                    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                    getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);

                    ft.addToBackStack(null);
                    ft.commit();

                } else if (id == R.id.chats) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, new chatfragment());
                    recyclerViewContacts.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                    // Set custom layout for ActionBar
                    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                    getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);

                    ft.addToBackStack(null);
                    ft.commit();
                } else if (id == R.id.tips) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, new safetyfragment());
                    recyclerViewContacts.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                    // Set custom layout for ActionBar
                    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                    getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);

                    ft.addToBackStack(null);
                    ft.commit();
                } else if (id == R.id.Contacts) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, new contactFragment());
                    recyclerViewContacts.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                    // Set custom layout for ActionBar
                    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                    getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);

                    ft.addToBackStack(null);
                    ft.commit();
                }
                return true;
            }
        });

        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        contactList = new ArrayList<>();


        contactList = dBhelper.getAllContactsForSMS();


        adapter = new contactcallAdaptor(this, contactList);


        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));


        recyclerViewContacts.setAdapter(adapter);
    }
}
