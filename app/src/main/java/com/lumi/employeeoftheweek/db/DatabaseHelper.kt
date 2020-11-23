package com.lumi.employeeoftheweek.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.lumi.employeeoftheweek.db.DatabaseContract.MemberColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbmemberapp"
        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_MEMBER = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.MemberColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${DatabaseContract.MemberColumns.NAME} TEXT NOT NULL," +
                "${DatabaseContract.MemberColumns.DESCRIPTION} TEXT NOT NULL," +
                "${DatabaseContract.MemberColumns.DATE} TEXT NOT NULL)"
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_MEMBER)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}