package com.surakshamitra.ScreenFragments;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surakshamitra.R;
import com.surakshamitra.UserFragment;
import com.surakshamitra.contactAdaptor1;
import com.surakshamitra.DBhelper;
import com.surakshamitra.model;

import java.util.ArrayList;
import java.util.List;

public class chatfragment extends Fragment {

    private RecyclerView recyclerView;
    private contactAdaptor1 adapter;
    private DBhelper dBhelper;
    private List<model> contacts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatfragment, container, false);

        dBhelper = new DBhelper(requireContext());
        recyclerView = view.findViewById(R.id.recycler_viewchat);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Implement item click listener
        contactAdaptor1.OnItemClickListener itemClickListener = new contactAdaptor1.OnItemClickListener() {
            @Override
            public void onItemClick(model contact) {
                // Open chat interface with the clicked contact
                openChatWithContact(contact);
            }
        };

        adapter = new contactAdaptor1(contacts, itemClickListener);
        recyclerView.setAdapter(adapter);
        loadExistingData();

        return view;
    }

    private void loadExistingData() {
        contacts.clear(); // Clear existing data to avoid duplication
        Cursor cursor = dBhelper.readAllData();
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int contactIndex = cursor.getColumnIndex("contact");

            String id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);
            String contact = cursor.getString(contactIndex);

            model newModel = new model(id, R.drawable.user, name, contact);
            contacts.add(newModel);
        }
        adapter.notifyDataSetChanged(); // Notify adapter about data change
    }

    // Method to open chat interface with the selected contact
    private void openChatWithContact(model contact) {
        // Start UserFragment and pass necessary data
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        UserFragment userFragment = UserFragment.newInstance(contact.getContact(), contact.getNumber());

        ft.replace(R.id.container, userFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
