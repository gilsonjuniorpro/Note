package br.note.com.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.note.com.R
import br.note.com.data.DataStore
import br.note.com.pojo.Note
import br.note.com.util.layoutInflater
import kotlinx.android.synthetic.main.item_note.view.*
import java.util.*

class NotesAdapter(private val context: Context) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private var notes: List<Note> = ArrayList()
    private var isRefreshing = false

    init {
        setHasStableIds(true)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        refresh()
    }

    override fun getItemId(position: Int): Long {
        return notes[position].id.toLong()
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(context.layoutInflater.inflate(R.layout.item_note, parent, false))
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.text.text = note.text
        holder.btDelete.setOnClickListener({showDialog(note)})
        holder.btEdit.setOnClickListener({toast("Edit Button was clicked")})
    }

    fun refresh() {
        if (isRefreshing) return
        isRefreshing = true
        DataStore.execute {
            val notes = DataStore.notes.getAll()
            Handler(Looper.getMainLooper()).post {
                this@NotesAdapter.notes = notes
                notifyDataSetChanged()
                isRefreshing = false
            }
        }
    }

    class NotesViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text = itemView.text
        val btDelete = itemView.btDelete
        val btEdit = itemView.btEdit
    }


    private fun delete(note: Note) {
        DataStore.execute {
            DataStore.notes.delete(note)
        }

        refresh()
    }


    private fun showDialog(note: Note) {
        var dialog: AlertDialog

        val builder = AlertDialog.Builder(context)

        builder.setTitle("Do you really want to delete this note?")

        builder.setPositiveButton("Yes") { _, _ ->
            toast("Note deleted")
            delete(note)
        }

        builder.setNegativeButton("No") { _, _ ->
            toast("Operation canceled")
        }

        dialog = builder.create()

        dialog.show()
    }


    fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}