package com.example.myapplication.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import com.example.myapplication.model.Note;


@Dao
public interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);


    @Delete
    void deleteNote(Note... note);


    @Update
    void updateNote(Note note);


    @Query("SELECT * FROM notes")
    List<Note> getNotes();


    @Query("SELECT * FROM notes WHERE id = :noteId")
    Note getNoteById(int noteId);



    @Query("DELETE FROM notes WHERE id = :noteId")
    void deleteNoteById(int noteId);

}
