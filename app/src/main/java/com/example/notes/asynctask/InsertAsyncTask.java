package com.example.notes.asynctask;

import android.os.AsyncTask;

import com.example.notes.dao.NoteDao;
import com.example.notes.entities.Note;

public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao noteDao;

    public InsertAsyncTask(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        noteDao.update(notes[0]);
        return null;
    }
}
