package com.example.notes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.customobject.TextViewCustom;
import com.example.notes.databinding.ItemNotesBinding;
import com.example.notes.entities.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NotesViewHolder> {

    private Context context;
    private List<Note> noteList;
    private LayoutInflater layoutInflater;

    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context);
        }
        ItemNotesBinding notesBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_notes, parent, false);
        return new NotesViewHolder(notesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.itemNotesBinding.setNote(note);
        holder.setNotes(note);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setNotes(List<Note> notes) {
        this.noteList = notes;
        notifyDataSetChanged();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        ItemNotesBinding itemNotesBinding;
        TextViewCustom txtNoteSubtitle;

        public NotesViewHolder(ItemNotesBinding itemNotesBinding) {
            super(itemNotesBinding.getRoot());
            this.itemNotesBinding = itemNotesBinding;
            txtNoteSubtitle = itemView.findViewById(R.id.txt_subtitle);
        }

        void setNotes(Note notes) {
            if (notes.getSubtitle().trim().isEmpty()) {
                txtNoteSubtitle.setVisibility(View.GONE);
            }
        }

    }

}
