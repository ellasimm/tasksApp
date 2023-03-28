package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText editTitle, editDescription, editPriority;
    private TextView editDate;
    private Button saveButton, datePickerButton;
    private RadioGroup radioGroupPriority;
    private RadioButton radioHigh, radioMedium, radioLow;
    private NotesDBHelper notesDBHelper;
    private int noteId;
    private DatePickerDialog datePickerDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initChangeDateButton();
        initSettings();
        initNotes();
        initToggleButton();
        setForEditing(false);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editTextDescription);
        radioGroupPriority = findViewById(R.id.radioGroup1);

        radioHigh = findViewById(R.id.radioHigh);
        radioMedium = findViewById(R.id.radioMedium);
        radioLow = findViewById(R.id.radioLow);

        saveButton = findViewById(R.id.buttonSave);

        notesDBHelper = new NotesDBHelper(this);

        datePickerButton = findViewById(R.id.buttonChange);
        datePickerButton.setText(getTodaysDate());

        noteId = getIntent().getIntExtra("noteId", -1);
        if (noteId != -1) {
            Note note = notesDBHelper.getNote(noteId);
            editTitle.setText(note.getTitle());
            editDescription.setText(note.getDescription());
            initSavedNotePriority(note.getPriority());
            datePickerButton.setText(getDate(note.getDate().getTime()));

        }else{

        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = editTitle.getText().toString();
                String description = editDescription.getText().toString();

                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {

                    int priority = getSelectPriority();
                    Date date = new Date(getDate(datePickerButton.getText().toString()));

                    Note note = new Note();
                    note.setTitle(title);
                    note.setDescription(description);
                    note.setPriority(priority);
                    note.setNoteDueDate(date);

                    if (noteId == -1) {
                        notesDBHelper.addNote(note);
                    } else {
                        note.setNoteID(noteId);
                        notesDBHelper.updateNote(note);
                    }

                    // Return to MainActivity
                    setResult(RESULT_OK);
                    finish();
                    Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

    private void initToggleButton() {
        final ToggleButton editToggle = (ToggleButton)findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForEditing(editToggle.isChecked());
            }
        });
    }

    private void setForEditing(boolean enabled) {
        EditText editTitle= findViewById(R.id.editTitle);
        EditText editDescription= findViewById(R.id.editTextDescription);
        RadioButton low = findViewById(R.id.radioLow);
        RadioButton med = findViewById(R.id.radioMedium);
        RadioButton high = findViewById(R.id.radioHigh);
        Button  buttonChange = findViewById(R.id.buttonChange);
        Button buttonSave= findViewById(R.id.buttonSave);

        editTitle.setEnabled(enabled);
        editDescription.setEnabled(enabled);
        low.setEnabled(enabled);
        med.setEnabled(enabled);
        high.setEnabled(enabled);
        buttonChange.setEnabled(enabled);
        buttonSave.setEnabled(enabled);


        if (enabled) {
            editTitle.requestFocus();
        }
        else {
            ScrollView s = findViewById(R.id.scrollView);
            s.fullScroll(ScrollView.FOCUS_UP);
        }
    }


    private void initSavedNotePriority(int priority) {
        switch (priority) {
            case 3:
                radioHigh.setChecked(true);
                break;
            case 2:
                radioMedium.setChecked(true);
                break;
            case 1:
                radioLow.setChecked(true);
                break;
        }

    }

    private int getSelectPriority() {
        if (radioHigh.isChecked()) {
            return 3;
        } else if (radioMedium.isChecked()) {
            return 2;

        } else {
            return 1;
        }
    }

    private void initChangeDateButton() {
        datePickerButton = findViewById(R.id.buttonChange);
        initDatePicker();
        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(v);
            }
        });

    }


    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month++;
                String date = makeDateString(day, month, year);
                datePickerButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String getMonthFormat(int month) {
        String currentMonth = "";
        switch (month) {
            case 1:
                currentMonth = "JAN";
                break;
            case 2:
                currentMonth = "FEB";
                break;
            case 3:
                currentMonth = "MAR";
                break;
            case 4:
                currentMonth = "APR";
                break;
            case 5:
                currentMonth = "MAY";
                break;
            case 6:
                currentMonth = "JUN";
                break;
            case 7:
                currentMonth = "JUL";
                break;
            case 8:
                currentMonth = "AUG";
                break;
            case 9:
                currentMonth = "SEP";
                break;
            case 10:
                currentMonth = "OCT";
                break;
            case 11:
                currentMonth = "NOV";
                break;
            case 12:
                currentMonth = "DEC";
                break;
        }
        return currentMonth;
    }

    private long getDate(String date) {
        long dateLong = 0;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
            Date d = dateFormat.parse(date);
            dateLong = d.getTime();
            return dateLong;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateLong;
    }

    private String getDate(long date){
        Date d = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
        return sdf.format(d).toUpperCase(Locale.ROOT);
    }

    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month++;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);

    }


    private void openDatePicker(View view){
        datePickerDialog.show();
    }

    private void launchMain(View v) {
        Intent i = new Intent(this, NotesActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
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
}