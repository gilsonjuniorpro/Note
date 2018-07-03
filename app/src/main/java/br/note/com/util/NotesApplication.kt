package br.note.com.util

import android.app.Application
import br.note.com.data.DataStore

class NotesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DataStore.init(this)
    }
}