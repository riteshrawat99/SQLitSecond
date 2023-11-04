package com.pupup.sqlitsecond

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyHelper(context: Context) : SQLiteOpenHelper(context, "ACDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE ACTABLE(_id INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,MEANING TEXT)")
        db?.execSQL("insert into ACTABLE(NAME,MEANING) VALUES('WWW','World Wide Web')")
        db?.execSQL("insert into ACTABLE(NAME,MEANING) VALUES('GDG','Google Developer Group')")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

}