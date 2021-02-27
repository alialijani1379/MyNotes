package com.example.notes.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Interface.ListenerDelete;
import com.example.notes.Interface.ListenerUpdate;
import com.example.notes.R;
import com.example.notes.customobject.TextViewCustom;
import com.example.notes.databinding.ItemNotesBinding;
import com.example.notes.entities.Note;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NotesViewHolder> {

    private Context context;
    private List<Note> noteList;
    private ListenerUpdate listenerUpdate;
    private ListenerDelete listenerDelete;
    private LayoutInflater layoutInflater;

    public NoteAdapter(Context context, List<Note> noteList, ListenerUpdate listenerUpdate, ListenerDelete listenerDelete) {
        this.context = context;
        this.noteList = noteList;
        this.listenerUpdate = listenerUpdate;
        this.listenerDelete = listenerDelete;
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
        holder.itemView.setOnClickListener(v -> listenerUpdate.onListenerUpdate(note, position));
        holder.itemView.setOnLongClickListener(v -> {
            listenerDelete.onListenerDelete(note);
            return false;
        });
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
        LinearLayout layoutNote;
        KenBurnsView imgNote;

        public NotesViewHolder(ItemNotesBinding itemNotesBinding) {
            super(itemNotesBinding.getRoot());
            this.itemNotesBinding = itemNotesBinding;
            txtNoteSubtitle = itemView.findViewById(R.id.txt_subtitle);
            layoutNote = itemView.findViewById(R.id.layout_notes);
            imgNote = itemView.findViewById(R.id.img_note);
        }

        void setNotes(Note notes) {
            if (notes.getSubtitle().trim().isEmpty()) {
                txtNoteSubtitle.setVisibility(View.GONE);
            }

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (notes.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(notes.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#535353"));
            }

            if (!(notes.getImagePath().equals(""))) {
                imgNote.setImageBitmap(BitmapFactory.decodeFile(notes.getImagePath()));
                imgNote.setVisibility(View.VISIBLE);
            } else {
                imgNote.setVisibility(View.GONE);
            }
        }

    }

    public void filterList(List<Note> filterList) {
        noteList = filterList;
        notifyDataSetChanged();
    }

}
