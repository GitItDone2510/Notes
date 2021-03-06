package com.perapps.notes.ui.notes

import androidx.lifecycle.*
import com.perapps.notes.data.local.entities.Note
import com.perapps.notes.other.Event
import com.perapps.notes.other.Resource
import com.perapps.notes.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _forceUpdate = MutableLiveData(false)

    private val _allNotes = _forceUpdate.switchMap {
        repository.getAllNotes().asLiveData(viewModelScope.coroutineContext)
    }.switchMap {
        MutableLiveData(Event(it))
    }

    val allNotes: LiveData<Event<Resource<List<Note>>>> = _allNotes

    fun syncAllNotes() = _forceUpdate.postValue(true)

    fun insertNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

    fun deleteNote(noteId: String) = viewModelScope.launch {
        repository.deleteNote(noteId)
    }

    fun deleteLocallyDeletedNoteId(deletedNoteId: String) = viewModelScope.launch {
        repository.deleteLocallyDeletedNoteId(deletedNoteId)
    }
}