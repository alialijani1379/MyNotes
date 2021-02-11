package com.example.notes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notes.entities.Note;
import com.example.notes.repository.NotesRepository;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {
    private NotesRepository notesRepository;
    private LiveData<List<Note>> allNotes;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        notesRepository = new NotesRepository(application);
        allNotes = notesRepository.getAllNotes();
    }

    public void insert(Note note) {
        notesRepository.insert(note);
    }

    public void update(Note note) {
        notesRepository.update(note);
    }

    public void delete(Note note) {
        notesRepository.delete(note);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

}
