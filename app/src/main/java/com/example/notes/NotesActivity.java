package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    ArrayList<Note> notes;
    notesAdapter notesAdapter = new notesAdapter(notes, this);
    RecyclerView noteList;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int noteId = notes.get(position).getNoteID();
            Intent intent = new Intent(NotesActivity.this, MainActivity.class);
            intent.putExtra("noteId", noteId);
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        initListButton();
        initSettingsButton();
        initAddNoteButton();
        initDeleteSwitch();
        notesAdapter.setOnItemClickListener(onItemClickListener);
        String sortBy = getSharedPreferences("MyNotesPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "title");
        String sortOrder = getSharedPreferences("MyNotesPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");


        NotesDBHelper ds = new NotesDBHelper(this);

        try {
            ds.open();
            notes = ds.getAllNotes(sortBy, sortOrder);
            ds.close();

            RecyclerView noteList = findViewById(R.id.rvNotes);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

            noteList.setLayoutManager(layoutManager);

            notesAdapter noteAdapter = new notesAdapter(notes, this);

            noteList.setAdapter(noteAdapter);


        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving Notes", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        String sortBy = getSharedPreferences("MyNotesPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "title");
        String sortOrder = getSharedPreferences("MyNotesPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");
        NotesDBHelper ds = new NotesDBHelper(this);
        try{
            ds.open();
            notes = ds.getAllNotes(sortBy, sortOrder);
            ds.close();
            if (notes.size() > 0 ) {
                noteList = findViewById(R.id.rvNotes);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                noteList.setLayoutManager(layoutManager);
                notesAdapter = new notesAdapter(notes, this);
                noteList.setAdapter(notesAdapter);
            } else {
                Intent intent = new Intent(NotesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Error Retrieving Notes", Toast.LENGTH_LONG).show();
        }
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Boolean status = compoundButton.isChecked();
                notesAdapter.setDelete(status);
                notesAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initListButton() {
    }


    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.settingsButton);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(NotesActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void initAddNoteButton() {
        Button newNote = findViewById(R.id.buttonAddNote);
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}