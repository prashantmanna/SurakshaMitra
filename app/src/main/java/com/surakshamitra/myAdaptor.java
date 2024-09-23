package com.surakshamitra;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.surakshamitra.DBhelper;

import java.util.ArrayList;

public class myAdaptor extends RecyclerView.Adapter<myAdaptor.ViewHolder> {
    Context context;
    ArrayList<model> arrContact;
    DBhelper dBhelper ;

    Dialog dialog;
    int position;

    private static int lastposition = -1;

    public myAdaptor(Context context, ArrayList<model> arrContact) {
        this.context = context;
        this.arrContact = arrContact;
        dBhelper = new DBhelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        holder.img.setImageResource(arrContact.get(position).img);
        holder.txtContact.setText(arrContact.get(position).contact);
        holder.txtNumber.setText(arrContact.get(position).number);
        this.position = position;
        setAnimation(holder.itemView, position);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.update);
                dialog.setTitle("UPDATE CONTACT");
                TextInputEditText edtName = dialog.findViewById(R.id.edtName);
                TextInputEditText edtNumber = dialog.findViewById(R.id.edtNumber);
                TextView contact = dialog.findViewById(R.id.contact);
                AppCompatButton Update = dialog.findViewById(R.id.add);
                contact.setText(R.string.update_contact);
                Update.setText(R.string.update);

                edtName.setText(arrContact.get(position).contact);
                edtNumber.setText(arrContact.get(position).number);

                Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position >= 0 && position < arrContact.size()) {
                            String id = arrContact.get(position).getId();
                            String name = edtName.getText().toString();
                            String number = edtNumber.getText().toString();

                            if (!id.isEmpty() && !name.isEmpty() && !number.isEmpty()) {

                                boolean isUpdated = dBhelper.updateContact(id, name, number);

                                if (isUpdated) {
                                    arrContact.set(position, new model(id, R.drawable.userperson, name, number));
                                    notifyItemChanged(position);
                                    Toast.makeText(context, "Contact Updated", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(context, "Failed to update contact in the database", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(context, "Please enter valid data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Invalid position", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });


        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete Contact")
                        .setMessage("Are you sure you want to delete?")
                        .setIcon(R.drawable.baseline_delete_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (position >= 0 && position < arrContact.size()) {

                                    String id = arrContact.get(position).getId();
                                    String name = arrContact.get(position).getContact();
                                    String number = arrContact.get(position).getNumber();

                                    Log.d("DeleteDebug", "ID: " + id + " Name: " + name + " Number: " + number);

                                    // Use the correct method to delete the contact
                                    dBhelper.delete_data(name, number);

                                    arrContact.remove(position);
                                    notifyItemRemoved(position);
                                } else {
                                    Log.e("DeleteDebug", "Invalid position: " + position);
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Handle "No" button click
                            }
                        });
                builder.show();

                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrContact.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContact, txtNumber;
        LinearLayout layout;
        ImageView img;

        public ViewHolder(@NonNull View item) {
            super(item);
            layout = item.findViewById(R.id.layout);
            txtContact = item.findViewById(R.id.contact);
            txtNumber = item.findViewById(R.id.number);
            img = item.findViewById(R.id.user);
        }
    }

    private void setAnimation(View v, int position) {
        if (position > lastposition) {
            Animation slide = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            v.startAnimation(slide);
            lastposition = position;
        }
    }


}
