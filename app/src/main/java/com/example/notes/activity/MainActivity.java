package com.example.notes.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notes.Interface.ListenerUpdate;
import com.example.notes.R;
import com.example.notes.adapter.NoteAdapter;
import com.example.notes.adapter.NotesAdapter;
import com.example.notes.customobject.TextViewCustom;
import com.example.notes.databinding.ActivityMainBinding;
import com.example.notes.entities.Note;
import com.example.notes.viewmodel.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ListenerUpdate {

    //<editor-fold desc="--Declaration--">
    public static final String TITLE_U = "title_u";
    public static final String SUBTITLE_U = "subtitle_u";
    public static final String NOTE_U = "note_u";
    public static final String DATE_U = "date_u";
    public static final String WEB_LINK_U = "web_link_u";
    public static final String IMAGE_PATH_U = "image_path_u";
    public static final String COLOR_U = "color_u";
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    private ActivityMainBinding mainBinding;
    private TextViewCustom txtTime;
    private RecyclerView recyclerView;
    private ShimmerLayout shimmerLayout;
    private FloatingActionButton fbAdd;
    private List<Note> notes;
    private NotesAdapter notesAdapter;
    private NotesViewModel notesViewModel;
    private NoteAdapter adapter;
    private int noteClickedPosition = -1;
    //</editor-fold>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        bindViews(mainBinding);
        setTime();
        fbAdd.setOnClickListener(this);

        shimmerLayout.startShimmerAnimation();
        setUpRecyclerView();
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        notesViewModel.getAllNotes().observe(this, notes -> adapter.setNotes(notes));
        shimmerLayout.stopShimmerAnimation();
        shimmerLayout.setVisibility(View.GONE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(CreateNoteActivity.TITLE);
            String subtitle = data.getStringExtra(CreateNoteActivity.SUBTITLE);
            String note = data.getStringExtra(CreateNoteActivity.NOTE);
            String dateTime = data.getStringExtra(CreateNoteActivity.DATE_TIME);
            String imagePath = data.getStringExtra(CreateNoteActivity.IMAGE_PATH);
            String color = data.getStringExtra(CreateNoteActivity.COLOR);
            String webLink = data.getStringExtra(CreateNoteActivity.WEB_LINK);
            Note note1 = new Note(title, dateTime, subtitle, note, imagePath, color, webLink);
            NotesViewModel notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
            notesViewModel.insert(note1);
            Toast.makeText(this, "Insert OK", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(CreateNoteActivity.ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Can't be Updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(CreateNoteActivity.TITLE);
            String subtitle = data.getStringExtra(CreateNoteActivity.SUBTITLE);
            String note = data.getStringExtra(CreateNoteActivity.NOTE);
            String dateTime = data.getStringExtra(CreateNoteActivity.DATE_TIME);
            String imagePath = data.getStringExtra(CreateNoteActivity.IMAGE_PATH);
            String color = data.getStringExtra(CreateNoteActivity.COLOR);
            String webLink = data.getStringExtra(CreateNoteActivity.WEB_LINK);

            Note note1 = new Note(title, dateTime, subtitle, note, imagePath, color, webLink);
            note1.setId(id);
            NotesViewModel notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
            notesViewModel.update(note1);
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Insert NOT OK", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpRecyclerView() {
        notes = new ArrayList<>();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.smoothScrollToPosition(0);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new NoteAdapter(this, notes, this);
//        notesAdapter = new NotesAdapter(this, notes);
        recyclerView.setAdapter(adapter);
//        notesAdapter.submitList(notes);
    }

    @Override
    public void onListenerUpdate(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(this, CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra(TITLE_U, note.getTitle());
        intent.putExtra(SUBTITLE_U, note.getSubtitle());
        intent.putExtra(NOTE_U, note.getNoteText());
        intent.putExtra(DATE_U, note.getDateTime());
        intent.putExtra(WEB_LINK_U, note.getWebLink());
        intent.putExtra(IMAGE_PATH_U, note.getImagePath());
        intent.putExtra(COLOR_U, note.getColor());
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fb_add_notes:
                Intent createNoteActivity = new Intent(this, CreateNoteActivity.class);
                startActivityForResult(createNoteActivity, REQUEST_CODE_ADD_NOTE);
                break;
        }
    }

    private void setTime() {
        Calendar calendar = Calendar.getInstance();

        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            //morning
            txtTime.setText(R.string.good_morning);
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            //afternoon
            txtTime.setText(R.string.good_afternoon);
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            //evening
            txtTime.setText(R.string.good_evening);
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            //night
            txtTime.setText(R.string.good_night);
        }
    }

    private void bindViews(ActivityMainBinding mainBinding) {
        txtTime = mainBinding.txtTime;
        recyclerView = mainBinding.rvNotes;
        shimmerLayout = mainBinding.shimmerLayout;
        fbAdd = mainBinding.fbAddNotes;
    }

}