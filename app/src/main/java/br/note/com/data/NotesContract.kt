package br.note.com.data

import br.note.com.data.NotesContract.NoteTable.CREATED_AT
import br.note.com.data.NotesContract.NoteTable.IS_PINNED
import br.note.com.data.NotesContract.NoteTable.TEXT
import br.note.com.data.NotesContract.NoteTable.UPDATED_AT
import br.note.com.data.NotesContract.NoteTable._ID
import br.note.com.data.NotesContract.NoteTable._TABLE_NAME

object NotesContract {

    object NoteTable {
        val _ID = "_id"
        val _TABLE_NAME = "notes"
        val TEXT = "text"
        val IS_PINNED = "is_pinned"
        val CREATED_AT = "created_at"
        val UPDATED_AT = "updated_at"
    }

    val SQL_CREATE_ENTRIES = """CREATE TABLE $_TABLE_NAME (
        $_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        $TEXT TEXT,
        $IS_PINNED INTEGER,
        $CREATED_AT INTEGER,
        $UPDATED_AT INTEGER
    )""".trimMargin()

    val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $_TABLE_NAME"

    val SQL_QUERY_ALL = "SELECT * FROM $_TABLE_NAME ORDER BY $CREATED_AT DESC"
}