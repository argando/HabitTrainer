package com.argando.habittrainer.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HabitTrainerDB(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val SQL_CREATE_ENTRIES = "CREATE TABLE ${HabitEntry.TABLE_NAME} " +
            "(${HabitEntry.ID_COLUMN} INTEGER PRIMARY KEY," +
            "${HabitEntry.TITLE_COLUMN} TEXT," +
            "${HabitEntry.DESCR_COLUMN} TEXT," +
            "${HabitEntry.IMAGE_COLUMN} BLOB)"

    private val SQL_DELETE_ENTRY = "DROP TABLE IF EXISTS ${HabitEntry.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRY)
        onCreate(db)
    }
}