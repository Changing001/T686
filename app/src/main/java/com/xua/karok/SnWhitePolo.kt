package com.xua.karok

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class SnWhitePolo : ContentProvider() {
    override fun getType(uri: Uri) = null

    override fun insert(uri: Uri, values: ContentValues?) = null


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String?>?) = 0

    override fun onCreate() = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String?>?
    ) = 0
}