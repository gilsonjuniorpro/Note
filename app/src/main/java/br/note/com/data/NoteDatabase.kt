package br.note.com.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import br.note.com.data.NotesContract.NoteTable.CREATED_AT
import br.note.com.data.NotesContract.NoteTable.IS_PINNED
import br.note.com.data.NotesContract.NoteTable.TEXT
import br.note.com.data.NotesContract.NoteTable.UPDATED_AT
import br.note.com.data.NotesContract.NoteTable._TABLE_NAME
import br.note.com.pojo.Note
import java.util.*
import org.jetbrains.anko.db.transaction


class NoteDatabase(context: Context) {

    private val helper = NotesOpenHelper(context)

    fun getAll(): List<Note> {
        return helper.readableDatabase.query(_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                BaseColumns._ID + " DESC").use(this::allFromCursor)
    }

    fun loadAllByIds(vararg ids: Int): List<Note> {
        val questionMarks = ids.map { "?" }.joinToString { ", " }
        val args = ids.map { "$it" }.toTypedArray()
        val selection = "${BaseColumns._ID} IN ($questionMarks)"
        return helper.readableDatabase.query(_TABLE_NAME,
                null,
                selection,
                args,
                null,
                null,
                CREATED_AT).use(this::allFromCursor)
    }

    fun insert(vararg notes: Note) {
        helper.writableDatabase.transaction {
            fromNotes(notes).forEach {
                insert(_TABLE_NAME, null, it)
            }
        }
    }

    fun update(note: Note) {
        val values = fromNote(note)
        helper.writableDatabase.update(_TABLE_NAME,
                values,
                BaseColumns._ID + " = ?",
                arrayOf("${note.id}"))
    }

    fun delete(note: Note) {
        helper.writableDatabase.delete(_TABLE_NAME,
                BaseColumns._ID + " = ?",
                arrayOf("${note.id}"))
    }

    private fun fromCursor(cursor: Cursor): Note {
        var col = 0
        return Note().apply {
            id = cursor.getInt(col++)
            text = cursor.getString(col++)
            isPinned = cursor.getInt(col++) != 0
            createdAt = Date(cursor.getLong(col++))
            updatedAt = Date(cursor.getLong(col))
        }
    }

    private fun allFromCursor(cursor: Cursor): List<Note> {
        val retval = mutableListOf<Note>()
        while (cursor.moveToNext()) {
            retval.add(fromCursor(cursor))
        }
        return retval
    }

    private fun fromNote(note: Note): ContentValues {
        return ContentValues().apply {
            val id = note.id
            if (id != -1) {
                put(BaseColumns._ID, id)
            }
            put(TEXT, note.text)
            put(IS_PINNED, note.isPinned)
            put(CREATED_AT, note.createdAt.time)
            put(UPDATED_AT, note.updatedAt!!.time)
        }
    }

    private fun fromNotes(notes: Array<out Note>): List<ContentValues> {
        return notes.map(this::fromNote)
    }
}
