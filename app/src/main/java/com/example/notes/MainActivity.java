package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

        // Initialize views and database helper
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
        // Check if we are editing an existing note
        noteId = getIntent().getIntExtra("noteId", -1);
        if (noteId != -1) {
            // Load note details based on the Tapped memo
            Note note = notesDBHelper.getNote(noteId);
            editTitle.setText(note.getTitle());
            editDescription.setText(note.getDescription());
            initSavedNotePriority(note.getPriority());
            //Converted Time
            datePickerButton.setText(getDate(note.getDate().getTime()));

        }else{

        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//On Save click, Retrieve Objects from EditText, Text, and Times
                String title = editTitle.getText().toString();
                String content = editDescription.getText().toString();
                int priority = getSelectPriority();
                //Returns the time of the device
                Date creationDate = new Date(System.currentTimeMillis());
//Create a date object
                Date noteDueDate = new Date(getDate(datePickerButton.getText().toString()));
                //Set everything to Note Object
                Note note = new Note();
                note.setTitle(title);
                note.setDescription(content);
                note.setPriority(priority);
                note.setNoteDueDate(noteDueDate);
//If note doesn't have an ID
                if (noteId == -1) {
                    // Save a new note to DBS
                    notesDBHelper.addNote(note);
                } else {
                    // Update an existing note
                    note.setNoteID(noteId);
                    notesDBHelper.updateNote(note);
                }

                // Return to MainActivity
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    //Inits the radio buttons to be pre checked based on Priority.
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

    //Returns the priority based on selected radio buttons
    private int getSelectPriority() {
        if (radioHigh.isChecked()) {
            return 3;
        } else if (radioMedium.isChecked()) {
            return 2;

        } else {
            return 1;
        }
    }
    //Inits Change Date button
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

    //Make the date given a String
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }
    //Inits the DatePicker Dialog functions
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
    //Returns the Month formated in MMM from given month
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
    //Returns a Long based on the Set date from picker
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
    //Returns Date in a "MMM dd yyyy" String
    private String getDate(long date){
        Date d = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
        return sdf.format(d).toUpperCase(Locale.ROOT);
    }
    //Returns todays date into a String.
    private String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month++;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);

    }

    //Opens the Datepicker Dialog
    private void openDatePicker(View view){
        datePickerDialog.show();
    }

    private void launchMain(View v) {
        Intent i = new Intent(this, NotesActivity.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}