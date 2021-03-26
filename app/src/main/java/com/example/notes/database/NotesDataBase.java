package com.example.notes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notes.dao.NoteDao;
import com.example.notes.entities.Note;

@Database(entities = Note.class, version = 2, exportSchema = false)
public abstract class NotesDataBase extends RoomDatabase {

    private static NotesDataBase notesDataBase;
    public abstract NoteDao noteDao();

    public static NotesDataBase getInstance(Context context) {
        if (notesDataBase == null) {
            notesDataBase = Room.databaseBuilder(context.getApplicationContext()
                    , NotesDataBase.class
                    , "notes_db")
                    .build();
        }
        return notesDataBase;
    }
}
