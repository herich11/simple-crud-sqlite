package com.simplecrudsqlite;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonAdd, buttonDelete, buttonDeleteAll;
    ListView listViewNames;
    ArrayAdapter<String> adapter;
    List<String> namesList;
    DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDeleteAll = findViewById(R.id.buttonDeleteAll);
        listViewNames = findViewById(R.id.listViewNames);

        databaseHelper = new DatabaseHelper(this);

        displayNames();

        buttonAdd.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            if (!name.isEmpty()) {
                databaseHelper.addName(name);
                editTextName.setText("");
                displayNames();
            } else {
                Toast.makeText(MainActivity.this, "Enter a name", Toast.LENGTH_SHORT).show();
            }
        });

        // Click to delete
//        listViewNames.setOnItemClickListener((parent, view, position, id) -> {
//            String name = namesList.get(position);
//            databaseHelper.deleteName(getID(name));
//            displayNames();
//        });

        // Click to delete
        listViewNames.setOnItemClickListener((parent, view, position, id) -> {
            String name = namesList.get(position);
            databaseHelper.deleteName(getID(name));
            namesList.remove(position); // Delete dari list
            adapter.notifyDataSetChanged(); // Change ListView
        });

        // Long click to delete
//        listViewNames.setOnItemLongClickListener((parent, view, position, id) -> {
//            String name = namesList.get(position);
//            databaseHelper.deleteName(getID(name));
//            displayNames();
//            return true;
//        });

        // Click to delete all
        buttonDeleteAll.setOnClickListener(v -> {
            databaseHelper.deleteAllNames();
            displayNames();
        });
    }

    private void displayNames() {
        namesList = databaseHelper.getAllNames();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textViewName, namesList) {
            @SuppressLint("InflateParams")
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    view = inflater.inflate(R.layout.list_item, null);
                }

                TextView textViewName = view.findViewById(R.id.textViewName);
                Button buttonDelete = view.findViewById(R.id.buttonDelete);

                final String name = getItem(position);

                textViewName.setText(name);

                // Set click listener for delete button
                buttonDelete.setOnClickListener((View v) -> {
                    databaseHelper.deleteName(getID(name));
                    displayNames();
                });

                return view;
            }
        };

        listViewNames.setAdapter(adapter);
    }

    private int getID(String name) {
        List<String> names = databaseHelper.getAllNames();
        int id = -1;
        for (String n : names) {
            id++;
            if (n.equals(name)) {
                break;
            }
        }
        return id;
    }
}