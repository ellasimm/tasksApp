package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initNotes();
        initSettings();
        initSortByClick();
        initSortOrderClick();
    }

    private void initSettings() {
        String sortBy = getSharedPreferences("MyNotesPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "title");
        String sortOrder = getSharedPreferences("MyNotesPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");

        RadioButton rbTitle = findViewById(R.id.radioTitle);
        RadioButton rbDueDate = findViewById(R.id.radioDate);
        RadioButton rbPriority = findViewById(R.id.radioPriority);

        if (sortBy.equalsIgnoreCase("title")) {
            rbTitle.setChecked(true);
        } else if (sortBy.equalsIgnoreCase("priority")) {
            rbPriority.setChecked(true);
        } else {
            rbDueDate.setChecked(true);
        }

        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);

        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        } else {
            rbDescending.setChecked(true);
        }
    }

    private void initSortByClick() {

        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton rbTitle = findViewById(R.id.radioTitle);
                RadioButton rbPriority = findViewById(R.id.radioPriority);

                if (rbTitle.isChecked()) {
                    getSharedPreferences("MyNotesPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "title").apply();
                } else if(rbPriority.isChecked()){
                    getSharedPreferences("MyNotesPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "priority").apply();
                }
                else {
                    getSharedPreferences("MyNotesPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "date").apply();
                }
            }
        });
    }

    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton rbAscending = findViewById(R.id.radioAscending);

                if (rbAscending.isChecked()) {

                    getSharedPreferences("MyNotesPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "ASC").apply();
                }
                else {
                    getSharedPreferences("MyNotesPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "DESC").apply();
                }
            }
        });
    }

    private void initNotes() {
        ImageButton ibList = findViewById(R.id.notesButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, NotesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}