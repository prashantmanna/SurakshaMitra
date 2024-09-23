package com.surakshamitra.ScreenFragments;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.surakshamitra.DBhelper;
import com.surakshamitra.R;
import com.surakshamitra.model;
import com.surakshamitra.myAdaptor;

import java.util.ArrayList;

public class contactFragment extends Fragment {

    private DBhelper dBhelper;
    private ArrayList<model> arrContact = new ArrayList<>();
    private myAdaptor adaptor;

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        dBhelper = new DBhelper(requireContext());

        recyclerView = view.findViewById(R.id.container);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floating);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adaptor = new myAdaptor(requireContext(), arrContact);
        recyclerView.setAdapter(adaptor);

        loadExistingData();

        return view;
    }

    private void showDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.update);
        dialog.setTitle("UPDATE CONTACT");
        EditText edtName = dialog.findViewById(R.id.edtName);
        EditText edtNumber = dialog.findViewById(R.id.edtNumber);
        AppCompatButton add = dialog.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String number = edtNumber.getText().toString();

                if (!name.isEmpty() && !number.isEmpty()) {
                    if (number.length() >= 10) {
                        // Validation for name: Disallow numeric characters
                        if (!name.matches(".*\\d.*")) { // Check if name contains any numeric character
                            // Validation for number: Allow only numeric characters
                            if (number.matches("[0-9]+")) { // Check if number contains only numeric characters
                                // Insert data into the database
                                boolean isInserted = dBhelper.add_record(name, number);
                                if (isInserted) {
                                    // Create a model object with the inserted values
                                    model newModel = new model(R.drawable.userperson, name, number);

                                    // Add the model to your list
                                    arrContact.add(newModel);

                                    // Notify the adapter that the data set has changed
                                    adaptor.notifyDataSetChanged();

                                    // Scroll to the last item
                                    recyclerView.scrollToPosition(arrContact.size() - 1);

                                    Toast.makeText(requireContext(), "Successfully Inserted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), "Failed to insert into the database", Toast.LENGTH_SHORT).show();
                                }
                                // Dismiss the dialog
                                dialog.dismiss();
                                // Hide the keyboard
                                hideKeyboard(v);
                            } else {
                                Toast.makeText(requireContext(), "Please enter a valid phone number (numeric characters only)", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "Please enter a valid name (no numeric characters)", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Please enter a valid mobile number (at least 10 digits)", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Please enter contact name and number", Toast.LENGTH_SHORT).show();
                }
            }
        });



        dialog.show();
    }
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(requireContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Load existing data from the database
    private void loadExistingData() {
        Cursor cursor = dBhelper.readAllData();
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int contactIndex = cursor.getColumnIndex("contact");

            String id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);
            String contact = cursor.getString(contactIndex);

            // Create a model object with the retrieved values
            model newModel = new model(id, R.drawable.userperson, name, contact);

            // Add the model to your list
            arrContact.add(newModel);
        }
        adaptor.notifyDataSetChanged();
    }
}
