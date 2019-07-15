package com.testapp.appfortesting.screens.content_provider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.testapp.appfortesting.R

import android.net.Uri
import java.nio.file.Files.delete
import android.content.ContentUris
import android.provider.SyncStateContract.Helpers.update
import android.content.ContentValues
import android.util.Log
import android.widget.SimpleCursorAdapter
import kotlinx.android.synthetic.main.fragment_contacts.*


class ContactsFragment : Fragment() {

    companion object{
        const val LOG_TAG = "myLogs"
        val CONTACT_URI = Uri.parse("content://com.testapp.appfortesting.AdressBook/contacts")
        const val CONTACT_NAME = "name"
        const val CONTACT_EMAIL = "email"

        fun newInstance() = ContactsFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cursor = activity?.contentResolver?.query(CONTACT_URI, null, null, null, null)
        activity?.startManagingCursor(cursor)

        val from = arrayOf("name", "email")
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        val adapter = SimpleCursorAdapter(
            context!!,
            android.R.layout.simple_list_item_2, cursor, from, to
        )
        lvContact.adapter = adapter
        btnInsert.setOnClickListener { onClickInsert() }
        btnDelete.setOnClickListener { onClickDelete() }
        btnUpdate.setOnClickListener { onClickUpdate() }
        btnError.setOnClickListener { onClickError() }
    }

    private fun onClickInsert() {
        val cv = ContentValues()
        cv.put(CONTACT_NAME, "name 4")
        cv.put(CONTACT_EMAIL, "email 4")
        val newUri = activity?.contentResolver?.insert(CONTACT_URI, cv)
        Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString())
    }

    private fun onClickUpdate() {
        val cv = ContentValues()
        cv.put(CONTACT_NAME, "name 5")
        cv.put(CONTACT_EMAIL, "email 5")
        val uri = ContentUris.withAppendedId(CONTACT_URI, 2)
        val cnt = activity?.contentResolver?.update(uri, cv, null, null)
        Log.d(LOG_TAG, "update, count = $cnt")
    }

    private fun onClickDelete() {
        val uri = ContentUris.withAppendedId(CONTACT_URI, 1)
        val cnt = activity?.contentResolver?.delete(uri, null, null)
        Log.d(LOG_TAG, "delete, count = $cnt")
    }

    private fun onClickError() {
        val uri = Uri.parse("content://${ContactsProvider.AUTHORITY}/phones")
        try {
            val cursor = activity?.contentResolver?.
                query(uri, null, null, null, null)
        } catch (ex: Exception) {
            Log.d(LOG_TAG, "Error: " + ex.javaClass + ", " + ex.message)
        }

    }
}
