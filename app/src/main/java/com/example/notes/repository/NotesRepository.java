package com.example.notes.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.notes.asynctask.DeleteAsyncTask;
import com.example.notes.asynctask.InsertAsyncTask;
import com.example.notes.asynctask.UpdateAsyncTask;
import com.example.notes.dao.NoteDao;
import com.example.notes.database.NotesDataBase;
import com.example.notes.entities.Note;

import java.util.List;

public class NotesRepository {

    private NotesDataBase notesDataBase;
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NotesRepository(Application application) {
        notesDataBase = NotesDataBase.getInstance(application);
        noteDao = notesDataBase.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note) {
        InsertAsyncTask insertAsyncTask = new InsertAsyncTask(noteDao);
        insertAsyncTask.execute(note);
    }

    public void update(Note note) {
        UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(noteDao);
        updateAsyncTask.execute(note);
    }

    public void delete(Note note) {
        DeleteAsyncTask deleteAsyncTask = new DeleteAsyncTask(noteDao);
        deleteAsyncTask.execute(note);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

}
