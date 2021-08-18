package com.argando.habittrainer.db

import android.provider.BaseColumns

val DATABASE_NAME = "habit.db"
val DATABASE_VERSION = 1

object HabitEntry: BaseColumns {
    val TABLE_NAME = "habit"
    val ID_COLUMN = "id"
    val TITLE_COLUMN = "title"
    val DESCR_COLUMN = "description"
    val IMAGE_COLUMN = "image"
}