package com.szareckii.mynotes.ui.note

import com.szareckii.mynotes.data.NotesRepository
import com.szareckii.mynotes.data.entity.Note
import com.szareckii.mynotes.data.model.NoteResult
import com.szareckii.mynotes.ui.base.BaseViewModel
import androidx.lifecycle.Observer

class NoteViewModel : BaseViewModel<Note?, NoteViewState>() {

    private val noteObserver = Observer { result: NoteResult? ->
        result ?: return@Observer
        when(result) {
            is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(result.data as? Note)
            is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
        }
    }

//    private val repositoryNote = NotesRepository.getNoteById(id)
    private val repositoryNote = NotesRepository

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private var pendingNote: Note? = null

    fun save(note : Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }

//        repositoryNote.getNoteById(id).removeObserver(noteObserver)
    }

    fun loadNote(id: String) {
        repositoryNote.getNoteById(id).observeForever(noteObserver)
    }

}