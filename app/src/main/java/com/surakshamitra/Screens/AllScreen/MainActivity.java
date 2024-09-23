package com.surakshamitra.Screens.AllScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.surakshamitra.Profile;
import com.surakshamitra.R;
import com.surakshamitra.ScreenFragments.AlertFragment;
import com.surakshamitra.ScreenFragments.chatfragment;
import com.surakshamitra.ScreenFragments.contactFragment;
import com.surakshamitra.ScreenFragments.homefragment;
import com.surakshamitra.ScreenFragments.safetyfragment;
import com.surakshamitra.Settings;
import com.surakshamitra.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    NavigationView navigationView;
    Toolbar toolbar;
    ActivityMainBinding mainBinding;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    ImageView camera;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        mainBinding.navBar.setBackground(null);
        auth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.navBar);
        frameLayout = findViewById(R.id.container);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_drawer);
        camera = findViewById(R.id.camera);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        setSupportActionBar(toolbar);
        // to make the Navigation drawer icon always appear on the action bar
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.homepage) {
                    replaceFragment(new homefragment(), false);
                } else if (id == R.id.alert) {
                    replaceFragment(new AlertFragment(), false);
                } else if (id == R.id.chats) {
                    replaceFragment(new chatfragment(), false);
                } else if (id == R.id.tips) {
                    replaceFragment(new safetyfragment(), false);
                } else if (id == R.id.Contacts) {
                    replaceFragment(new contactFragment(), false);
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.homepage);

    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Pass the event to ActionBarDrawerToggle if it matches the toggle button
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (addToBackStack) {
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
        } else {
            fragmentTransaction.replace(R.id.container, fragment);
        }

        fragmentTransaction.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int item = menuItem.getItemId();
        if(item == R.id.profile)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container,new Profile());
            drawerLayout.close();
            ft.commit();
        } else if (item == R.id.settings) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container,new Settings());
            drawerLayout.close();
            ft.commit();

        }else {
            drawerLayout.close();
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.logut_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custcardview));

            AppCompatButton logout = dialog.findViewById(R.id.btnDialogLogout);
            AppCompatButton cancel = dialog.findViewById(R.id.btnDialogCancel);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSharedPreferences();
                    auth.signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit App");
        builder.setMessage("Are you sure you want to exit?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.LoginID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            showDialog();
        }

    }
}
