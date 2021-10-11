package com.googel.note.listeners;

import com.googel.note.entities.Note;

public interface NoteListener {
    void onNoteClicked( Note note,int position );
}
