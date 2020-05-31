package com.example.myapplication.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;


public class Note_Firebase {
    private int id; // default value
    private String noteText;
    private long noteDate;

    @Exclude // we don't want to store this value on database so ignore it
    private boolean checked = false;

    public Note_Firebase() {
    }

    public Note_Firebase(String noteText, long noteDate) {
        this.noteText = noteText;
        this.noteDate = noteDate;
    }
    public Note_Firebase(Note n) {
        this.id=n.getId();
        this.noteText = n.getNoteText();
        this.noteDate = n.getNoteDate();
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public long getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", noteDate=" + noteDate +
                '}';
    }
}
