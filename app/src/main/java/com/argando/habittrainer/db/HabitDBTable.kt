package com.argando.habittrainer.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.argando.habittrainer.Habit
import com.argando.habittrainer.db.HabitEntry.DESCR_COLUMN
import com.argando.habittrainer.db.HabitEntry.IMAGE_COLUMN
import com.argando.habittrainer.db.HabitEntry.TABLE_NAME
import com.argando.habittrainer.db.HabitEntry.TITLE_COLUMN
import java.io.ByteArrayOutputStream

class HabitDBTable(context: Context) {

    private val dbHelper: HabitTrainerDB = HabitTrainerDB(context)

    fun store(habit: Habit): Long {
        val db = dbHelper.writableDatabase
        val contentValue = ContentValues()
        with(contentValue) {
            put(TITLE_COLUMN, habit.title)
            put(DESCR_COLUMN, habit.description)
            put(IMAGE_COLUMN, toByteArray(habit.imageRes))
        }
        return db.executeTransaction {
            insert(TABLE_NAME, null, contentValue)
        }
    }

    fun readAllHabits(): List<Habit> {
        val db = dbHelper.writableDatabase
        val columns = arrayOf(
            TITLE_COLUMN, DESCR_COLUMN,
            IMAGE_COLUMN
        )
        return db.readColumns(columns = columns, factory = ::Habit)
    }

    private fun <T> SQLiteDatabase.readColumns(
        tableName: String = TABLE_NAME,
        factory: (String, String, Bitmap) -> T,
        columns: Array<String>
    ): MutableList<T> {
        val cursor = defaultQuery(tableName, columns)
        val result = mutableListOf<T>()
        with(cursor) {
            while (moveToNext()) {
                val title = getData(TITLE_COLUMN) { getString(it) }
                val desc = getData(DESCR_COLUMN) { getString(it) }
                val bitmap = getData(IMAGE_COLUMN) {
                    val byteArray = getBlob(it)
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                }
                result.add(factory(title, desc, bitmap))
            }
        }
        cursor.close()
        return result
    }

    private fun SQLiteDatabase.defaultQuery(
        table: String,
        columns: Array<String>,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        groupBy: String? = null,
        having: String? = null,
        orderBy: String? = null
    ): Cursor {
        return query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
    }

    private fun <T> Cursor.getData(
        tableColumn: String,
        function: (columnIndex: Int) -> T
    ): T {
        return function(getColumnIndex(tableColumn))
    }

    private inline fun <T> SQLiteDatabase.executeTransaction(function: SQLiteDatabase.() -> T): T {
        beginTransaction()
        val result = try {
            val value = function()
            setTransactionSuccessful()
            value
        } finally {
            endTransaction()
        }
        close()
        return result
    }

    private fun toByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }
}