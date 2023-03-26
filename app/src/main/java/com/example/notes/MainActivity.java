package com.example.notes;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSettings();
        initNotes();

        initToggleButton();
        setForEditing(false);
        initChangeDateButton();
    }


    private void initSettings() {
        ImageButton ibList = findViewById(R.id.settingsButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initNotes() {
        ImageButton ibList = findViewById(R.id.notesButton);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initToggleButton() {
        final ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForEditing(editToggle.isChecked());
            }
        });
    }

    private void setForEditing(boolean enabled) {
        EditText editTask = findViewById(R.id.editTask);
        EditText editDescription = findViewById(R.id.editTextDescription);
        Button saveButton = findViewById(R.id.buttonSave);
        Button changeButton = findViewById(R.id.buttonChange);
        //RadioButton lowPriority = findViewById(R.id.radioTitle);
       // RadioButton mediumPriority = findViewById(R.id.radioDate);
       // RadioButton highPriority = findViewById(R.id.radioHigh);
        RadioGroup priority = findViewById(R.id.radioGroup1);

        editTask.setEnabled(enabled);
        editDescription.setEnabled(enabled);
        saveButton.setEnabled(enabled);
        changeButton.setEnabled(enabled);
        //lowPriority.setEnabled(enabled);
       // mediumPriority.setEnabled(enabled);
       // highPriority.setEnabled(enabled);
        priority.setEnabled(enabled);

    }

    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        TextView dueDate = findViewById(R.id.textDueDate);
        dueDate.setText(DateFormat.format("MM/dd/yyyy", selectedTime));
    }

    private void initChangeDateButton() {
        Button changeDate = findViewById(R.id.buttonChange);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(fm, "DatePick");
            }
        });
    }
}