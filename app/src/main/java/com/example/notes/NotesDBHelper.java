package com.example.notes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesDBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase database;

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_DATE = "date";

    private static final String CREATE_TABLE_NOTES = "CREATE TABLE " + TABLE_NOTES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT NOT NULL, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_PRIORITY + " INTEGER, " +
            COLUMN_DATE + " INTEGER" + ")";
    public void open() throws SQLException{
        database = getWritableDatabase();
    }
    public NotesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public long insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, note.getTitle());
        contentValues.put(COLUMN_DESCRIPTION, note.getDescription());
        contentValues.put(COLUMN_PRIORITY, note.getPriority());
        contentValues.put(COLUMN_DATE, note.getDate().getTime());

        long id = db.insert(TABLE_NOTES, null, contentValues);
        db.close();
        return id;
    }
    //Gets a note and updates it the note by checking ID
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, note.getTitle());
        contentValues.put(COLUMN_DESCRIPTION, note.getDescription());
        contentValues.put(COLUMN_PRIORITY, note.getPriority());
        contentValues.put(COLUMN_DATE, note.getDate().getTime());

        int rowsAffected = db.update(TABLE_NOTES, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(note.getNoteID())});
        db.close();
        return rowsAffected;
    }
    //Goes into dbs and deletes the note based on given ID
    public boolean deleteNote(int noteId) {
        boolean didDelete = false;
        try{
            System.out.println("Deleting");
            didDelete = database.delete(TABLE_NOTES,COLUMN_ID+" = "+noteId, null)>0;
        }catch(Exception e){

        }
        return didDelete;
    }

    //Gets all notes from the DBS
    @SuppressLint("Range")
    public ArrayList<Note> getAllNotes(String orderBy) {
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null, null, null, null, null, orderBy);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setNoteID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                note.setPriority(cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY)));

                long dueDateLong = cursor.getLong(cursor.getColumnIndex(COLUMN_DATE));
                Date dueDate = new Date(dueDateLong);
                note.setNoteDueDate(dueDate);

                notes.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notes;
    }
    @SuppressLint("Range")
    //Gets all notes from DBS ordered and sorted in an ArrayList
    public ArrayList<Note> getAllNotes(String orderBy, String sortby) {
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+NotesDBHelper.TABLE_NOTES+" ORDER BY "+orderBy+" "+sortby;
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setNoteID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                note.setPriority(cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY)));

                long dueDateLong = cursor.getLong(cursor.getColumnIndex(COLUMN_DATE));
                Date dueDate = new Date(dueDateLong);
                note.setNoteDueDate(dueDate);

                notes.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notes;
    }
    @SuppressLint("Range")
    //Returns Note Object based given on noteID
    public Note getNote(int noteId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES,
                new String[]{COLUMN_ID, COLUMN_TITLE,COLUMN_DESCRIPTION,
                        COLUMN_PRIORITY, COLUMN_DATE},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(noteId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Date dueDate;
            long dueDateLong;
            Note note = new Note();
            note.setNoteID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            note.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            note.setPriority(cursor.getInt(cursor.getColumnIndex(COLUMN_PRIORITY)));

            //Due Date
            dueDateLong = cursor.getLong(cursor.getColumnIndex(COLUMN_DATE));
            dueDate = new Date(dueDateLong);
            note.setNoteDueDate(dueDate);
            ;
            return note;
        }

        if (cursor != null) {
            cursor.close();
        }

        return null;
    }
    //Adds note to the DBS
    public void addNote(Note note) {
        long dueDate = note.getDate().getTime();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, note.getTitle());
        contentValues.put(COLUMN_DESCRIPTION, note.getDescription());
        contentValues.put(COLUMN_PRIORITY, note.getPriority());
        contentValues.put(COLUMN_DATE, dueDate);

        db.insert(TABLE_NOTES, null, contentValues);
        db.close();
    }
}
