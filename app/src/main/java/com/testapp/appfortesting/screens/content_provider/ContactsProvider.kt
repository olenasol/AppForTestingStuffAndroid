package com.testapp.appfortesting.screens.content_provider

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.text.TextUtils
import android.util.Log

class ContactsProvider : ContentProvider() {
    companion object {
        const val LOG_TAG = "myLogs"
        const val DB_NAME = "mydb"
        const val DB_VERSION = 1
        const val CONTACT_TABLE = "contacts"
        const val CONTACT_ID = "_id"
        const val CONTACT_NAME = "name"
        const val CONTACT_EMAIL = "email"
        const val DB_CREATE =
            "create table $CONTACT_TABLE($CONTACT_ID integer primary key autoincrement, $CONTACT_NAME text, $CONTACT_EMAIL text);"
        const val AUTHORITY = "com.testapp.appfortesting.AdressBook"
        const val CONTACT_PATH = "contacts"
        val CONTACT_CONTENT_URI = Uri.parse("content://$AUTHORITY/$CONTACT_PATH")
        const val CONTACT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$CONTACT_PATH"
        const val CONTACT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$AUTHORITY.$CONTACT_PATH"
        const val URI_CONTACTS = 1
        const val URI_CONTACTS_ID = 2
    }

    val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    var dbHelper: DBHelper? = null
    var db: SQLiteDatabase? = null

    override fun onCreate(): Boolean {
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH, URI_CONTACTS)
        uriMatcher.addURI(AUTHORITY, "$CONTACT_PATH/#", URI_CONTACTS_ID)
        dbHelper = DBHelper(context!!)
        return true
    }

    override fun query(uri:Uri,  projection:Array<String>?, selection:String? ,
                 selectionArgs:Array<String>?, sortOrder:String?):Cursor
    {
        var newSortOrder = sortOrder
        var newSelection = selection
        when(uriMatcher.match(uri)) {
            URI_CONTACTS -> {
                if (TextUtils.isEmpty(sortOrder)) {
                    newSortOrder = "$CONTACT_NAME ASC"
                }
            }
            URI_CONTACTS_ID -> {
                val id = uri.lastPathSegment
                newSelection = if (TextUtils.isEmpty(selection)) {
                    "$CONTACT_ID = $id"
                } else {
                    "$selection AND $CONTACT_ID = $id"
                }
            }
        }
        db = dbHelper?.writableDatabase
        val cursor = db?.query (CONTACT_TABLE, projection, newSelection,
        selectionArgs, null, null, newSortOrder)
        cursor?.setNotificationUri(
            context!!.contentResolver,
            CONTACT_CONTENT_URI
        )
        return cursor!!
    }

    override fun insert(uri:Uri ,values: ContentValues?):Uri
    {
        if (uriMatcher.match(uri) != URI_CONTACTS)
            throw IllegalArgumentException ("Wrong URI: $uri")

        db = dbHelper?.writableDatabase
        val rowID = db?.insert(CONTACT_TABLE, null, values)
        val resultUri = ContentUris.withAppendedId (CONTACT_CONTENT_URI, rowID!!)
        context!!.contentResolver.notifyChange(resultUri, null)
        return resultUri
    }

    override fun delete(uri:Uri, selection:String?, selectionArgs:Array<String>?):Int
    {
        var newSelection = selection
        when(uriMatcher.match(uri)) {
            URI_CONTACTS ->
                Log.d(LOG_TAG, "URI_CONTACTS")
            URI_CONTACTS_ID -> {
                val id = uri.lastPathSegment
                Log.d(LOG_TAG, "URI_CONTACTS_ID, $id")
                newSelection = if (TextUtils.isEmpty(selection)) {
                    "$CONTACT_ID = $id"
                } else {
                    "$selection AND $CONTACT_ID = $id"
                }
            }
        }
        db = dbHelper?.writableDatabase
        val cnt = db?.delete (CONTACT_TABLE, newSelection, selectionArgs)
        context!!.contentResolver.notifyChange(uri, null)
        return cnt!!
    }

    override fun update(uri:Uri, values:ContentValues?,selection:String?, selectionArgs:Array<String>?):Int
    {
        Log.d(LOG_TAG, "update, $uri")
        var newSelection = selection
        when(uriMatcher.match(uri)) {
            URI_CONTACTS ->
                Log.d(LOG_TAG, "URI_CONTACTS")
            URI_CONTACTS_ID -> {
                val id = uri.lastPathSegment
                newSelection = if (TextUtils.isEmpty(selection)) {
                    "$CONTACT_ID = $id"
                } else {
                    "$selection AND $CONTACT_ID = $id"
                }
            }
        }
        db = dbHelper?.writableDatabase
        val cnt = db?.update (CONTACT_TABLE, values, newSelection, selectionArgs)
        context!!.contentResolver.notifyChange(uri, null)
        return cnt!!
    }

    override fun getType(uri: Uri):String?
    {
        Log.d(LOG_TAG, "getType, " + uri.toString())
        when(uriMatcher.match(uri)) {
            URI_CONTACTS ->
                return CONTACT_CONTENT_TYPE
            URI_CONTACTS_ID ->
                return CONTACT_CONTENT_ITEM_TYPE
        }
        return null
    }

    class DBHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION)
    {
        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        }


        override fun onCreate(db:SQLiteDatabase) {
            db.execSQL(DB_CREATE)
            val cv = ContentValues()
            for (i in 1..3) {
                cv.put(CONTACT_NAME, "name $i")
                cv.put(CONTACT_EMAIL, "email $i")
                db.insert(CONTACT_TABLE, null, cv)
            }
        }

    }
}
