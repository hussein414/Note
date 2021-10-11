package com.googel.note.adpters;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.googel.note.R;
import com.googel.note.entities.Note;
import com.googel.note.listeners.NoteListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> noteList;
    private NoteListener noteListener;
    private Timer timer;
    private List<Note> notesSours;

    public NoteAdapter( List<Note> noteList, NoteListener noteListener ) {
        this.noteList = noteList;
        this.noteListener = noteListener;
        this.notesSours = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.item_notes, parent, false));
    }

    @Override
    public void onBindViewHolder( @NonNull NoteViewHolder holder, int position ) {
        holder.setNote(noteList.get(position));
        holder.layoutNote.setOnClickListener(v -> {
            noteListener.onNoteClicked(noteList.get(position), position);
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public int getItemViewType( int position ) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView Title;
        TextView subtitle;
        TextView DateTime;
        LinearLayout layoutNote;
        RoundedImageView imageNote;

        public NoteViewHolder( @NonNull View itemView ) {
            super(itemView);
            Title = itemView.findViewById(R.id.textTitle);
            subtitle = itemView.findViewById(R.id.textSubtitle);
            DateTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.LayoutNote);
            imageNote = itemView.findViewById(R.id.imageNote);
        }

        void setNote( Note note ) {
            Title.setText(note.getTitle());
            if (note.getSubtitle().trim().isEmpty()) {
                subtitle.setVisibility(View.GONE);
            } else {
                subtitle.setText(note.getSubtitle());
            }
            DateTime.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (note.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }
            if (note.getImagePath() != null) {
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
            } else {
                imageNote.setVisibility(View.GONE);
            }
        }
    }

    public void SearchNote( final String SearchKeyword ) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                if (SearchKeyword.trim().isEmpty()) {
                    noteList = notesSours;
                } else {
                    ArrayList<Note> temp = new ArrayList<>();
                    for (Note note : notesSours) {
                        if (note.getTitle().toLowerCase().contains(SearchKeyword.toLowerCase())
                                || note.getSubtitle().toLowerCase().contains(SearchKeyword.toLowerCase())
                                || note.getNoteText().toLowerCase().contains(SearchKeyword.toLowerCase())) {
                            temp.add(note);
                        }
                    }
                    noteList = temp;
                }
                new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
            }
        }, 500);
    }

    public void CancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
