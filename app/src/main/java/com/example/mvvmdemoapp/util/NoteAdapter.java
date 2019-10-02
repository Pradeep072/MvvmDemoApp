package com.example.mvvmdemoapp.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmdemoapp.R;
import com.example.mvvmdemoapp.room_pkg.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {

    List<Note> notes=new ArrayList<>();
    private OnNoteItemClickListener listener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK=new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority()==newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item,parent,false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote=getItem(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    public Note getNote(int position){
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;


        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.text_title);
            textViewDescription=itemView.findViewById(R.id.text_description);
            textViewPriority=itemView.findViewById(R.id.text_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if (listener!=null && position!=RecyclerView.NO_POSITION) {
                        listener.onNoteItemClick(getItem(position));
                    }
                }
            });

        }
    }

    public interface OnNoteItemClickListener{
        void onNoteItemClick(Note note);
    }

    public void setOnNoteItemClickListener(OnNoteItemClickListener listener){
        this.listener=listener;
    }
}
