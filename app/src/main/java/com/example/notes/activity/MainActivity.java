package com.example.notes.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.notes.Interface.ListenerDelete;
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
import java.util.Collections;
import java.util.List;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ListenerUpdate, ListenerDelete {

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
    private ImageView imgState;
    private ImageView imgReverse;
    private TextViewCustom txtTime;
    private EditText edtSearch;
    private RecyclerView recyclerView;
    private ShimmerLayout shimmerLayout;
    private FloatingActionButton fbAdd;
    private List<Note> notes = new ArrayList<>();
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;
    private NoteAdapter adapter;
    private AlertDialog dialogDelete;
    private LottieAnimationView lottie;
    private Animation animLottie;

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
        notesViewModel.getAllNotes().observe(this, notes -> {
            adapter.setNotes(notes);
            if (notes.size() == 0) {
                lottie.setVisibility(View.VISIBLE);
                animLottie = AnimationUtils.loadAnimation(this, R.anim.anim_lottie);
                lottie.setAnimation(animLottie);
            } else {
                lottie.setVisibility(View.GONE);
            }
            imgReverse.setOnClickListener(v -> {
                Collections.reverse(notes);
                adapter.notifyDataSetChanged();
            });
        });
        shimmerLayout.stopShimmerAnimation();
        shimmerLayout.setVisibility(View.GONE);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
//                        if (!(s.toString().trim().isEmpty())) {
//                            filter(s.toString(), notes);
//                        }
                if (notes.size() != 0) {
                    adapter.searchNotes(s.toString());
                }
            }
        });

    }

    private void filter(String text, List<Note> notes) {
        List<Note> filterList = new ArrayList<>();
        for (Note model : notes) {
            if (model.getTitle().toLowerCase().contains(text.toLowerCase())
                    || model.getSubtitle().toLowerCase().contains(text.toLowerCase())
                    || model.getNoteText().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(model);
            }
        }
        adapter.filterList(filterList);
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

            imgState.setImageResource(R.drawable.ic_tick);

        } else {
            Toast.makeText(this, "Insert NOT OK", Toast.LENGTH_SHORT).show();
        }

        imgState.setVisibility(View.GONE);
    }

    private void setUpRecyclerView() {
        notes = new ArrayList<>();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.smoothScrollToPosition(0);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new NoteAdapter(this, notes, this, this);
//        notesAdapter = new NotesAdapter(this, notes);
        recyclerView.setAdapter(adapter);
//        notesAdapter.submitList(notes);
    }

    @Override
    public void onListenerUpdate(Note note, int position) {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra(CreateNoteActivity.ID, note.getId());
        intent.putExtra(TITLE_U, note.getTitle());
        intent.putExtra(SUBTITLE_U, note.getSubtitle());
        intent.putExtra(NOTE_U, note.getNoteText());
        intent.putExtra(DATE_U, note.getDateTime());
        intent.putExtra(WEB_LINK_U, note.getWebLink());
        intent.putExtra(IMAGE_PATH_U, note.getImagePath());
        intent.putExtra(COLOR_U, note.getColor());
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }

    @Override
    public void onListenerDelete(Note note) {
        imgState.setVisibility(View.VISIBLE);
        NotesViewModel notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        imgState.setOnClickListener(v -> showDialogDelete(notesViewModel, note));
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

    private void showDialogDelete(NotesViewModel notesViewModel, Note note) {
        if (dialogDelete == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.item_delete_note, null);
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }

            TextViewCustom txtCancel = view.findViewById(R.id.txt_cancel_note);
            TextViewCustom txtDelete = view.findViewById(R.id.txt_delete_note);

            txtDelete.setOnClickListener(v -> {
                notesViewModel.delete(note);
                dialogDelete.dismiss();
                imgState.setVisibility(View.GONE);
            });

            txtCancel.setOnClickListener(v -> dialogDelete.dismiss());
            builder.setView(view);
            dialogDelete = builder.create();
            if (dialogDelete.getWindow() != null) {
                dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialogDelete.getWindow().setGravity(Gravity.BOTTOM);
            }
        }
        dialogDelete.show();
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
        imgState = mainBinding.imgState;
        imgReverse = mainBinding.imgReverse;
        txtTime = mainBinding.txtTime;
        edtSearch = mainBinding.edtSearch;
        recyclerView = mainBinding.rvNotes;
        shimmerLayout = mainBinding.shimmerLayout;
        fbAdd = mainBinding.fbAddNotes;
        lottie = mainBinding.lottieAnimation;
    }

}