package com.example.mvvmdemoapp.room_pkg;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*Dao(Data Access Object) should be Interface or Abstract classes*/
@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update (Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("Select * FROM note_table ORDER BY priority DESC")
    LiveData<List<Note>> getAllNotes();
}
