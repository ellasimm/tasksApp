package com.example.notes;

import java.util.Date;

public class Note {
    private int noteID;
    private String title;
    private String description;
    private int priority;
    private Date date;

    public Note() {
    }

    public Note(int noteID, String title, String content, int priority, Date noteCreationDate, Date noteDueDate) {
        this.noteID = noteID;
        this.title = title;
        this.description = content;
        this.priority = priority;
        this.date = noteDueDate;
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getDate() {
        return date;
    }
    @Override
    public String toString() {
        return "Note{" +
                "noteID=" + noteID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", date=" + date +
                '}';
    }

    public void setNoteDueDate(Date date) {
        this.date = date;
    }
    //Returns High/Medium/Low based on priority level.
    public static String priorityText(int priority){
        String level;
        switch(priority){
            case 1:
                level = "Low";
                break;
            case 2:
                level = "Med";
                break;
            case 3:
                level = "High";
                break;
            default:
                level = null;
        }
        return level;
    }
}