package com.surakshamitra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DBhelper extends SQLiteOpenHelper {
    private static final String dbname = "contacts";
    public static final int version = 1;
    private final DatabaseReference contactsReference;
    private final DatabaseReference messagesReference;

    public DBhelper(Context context) {
        super(context, dbname, null, version);
        contactsReference = FirebaseDatabase.getInstance().getReference("contacts");
        messagesReference = FirebaseDatabase.getInstance().getReference("messages");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String qry = "create table contact(id integer primary key autoincrement,name text,contact text)";
        db.execSQL(qry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String qry = "DROP TABLE IF EXISTS contact";
        db.execSQL(qry);
        onCreate(db);
    }

    public boolean add_record(String name, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("contact", contact);

        long res = db.insert("contact", null, cv);
        db.close();

        // Store in Firebase Realtime Database
        if (res != -1) {
            String key = contactsReference.push().getKey();
            contactsReference.child(key).setValue(new model(name, contact));
        }

        return res != -1;
    }


    public Cursor readAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String qry = "select * from contact";
        return db.rawQuery(qry, null);
    }

    public boolean updateContact(String id, String name, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("contact", contact);

        int rowsUpdated = db.update("contact", contentValues, "id = ?", new String[]{id});
        db.close();

        if (rowsUpdated > 0) {
            // Update the contact in Firebase
            contactsReference.child(id).setValue(new model(name, contact));
            return true;
        } else {
            return false;
        }
    }


    public boolean delete_data(String name, String number) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the contact exists
        Cursor cursor = db.rawQuery("SELECT * FROM contact WHERE name=? AND contact=?", new String[]{name, number});

        if (cursor.getCount() > 0) {
            // The contact exists, proceed with deletion
            long r = db.delete("contact", "name=? AND contact=?", new String[]{name, number});

            db.close(); // Close the database after the operation.

            // Delete data from Firebase
            if (r != -1) {
                // Query Firebase to find the corresponding node
                contactsReference.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("DBHelper", "Failed to delete value from Firebase.", databaseError.toException());
                    }
                });
            }

            return r != -1;
        } else {
            db.close();
            return false;
        }
    }


    public long sendMessage(String sender, String message) {
        String key = messagesReference.push().getKey();
        messagesReference.child(key).setValue(new messageModel(sender, message));
        return 1; // Return value indicating success
    }

    public List<Message> getMessages() {
        final List<Message> messages = new ArrayList<>();

        messagesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    messages.add(message);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DBHelper", "Failed to read value.", error.toException());
            }
        });

        return messages;
    }

    public ArrayList<model> getAllContactsForSMS() {
        ArrayList<model> contactsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"name", "contact"};

        Cursor cursor = db.query("contact", projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex("name");
            int contactIndex = cursor.getColumnIndex("contact");

            String name = cursor.getString(nameIndex);
            String contact = cursor.getString(contactIndex);

            model contactModel = new model(name, contact);
            contactsList.add(contactModel);
        }

        cursor.close();
        db.close();

        return contactsList;
    }

}
