package com.example.notes.asynctask;

import android.os.AsyncTask;

import com.example.notes.dao.NoteDao;
import com.example.notes.entities.Note;

public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao noteDao;

    public DeleteAsyncTask(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        noteDao.delete(notes[0]);
        return null;
    }
}
