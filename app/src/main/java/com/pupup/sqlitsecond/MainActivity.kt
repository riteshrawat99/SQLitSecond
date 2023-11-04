package com.pupup.sqlitsecond

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var db : SQLiteDatabase
    lateinit var cursor: Cursor
    lateinit var adapter: SimpleCursorAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // data base query

        val helper = MyHelper(applicationContext)
         db = helper.readableDatabase
         cursor = db.rawQuery("SELECT * FROM ACTABLE order by NAME",null)

        // get reference  form xml
        val getMessage : EditText = findViewById(R.id.getMessage)
        val shortcut : EditText = findViewById(R.id.shortcut)
        val nextBtn: Button = findViewById(R.id.nextBtn)
        val firstBtn : Button = findViewById(R.id.firstBtn)
        val submit : Button = findViewById(R.id.submit)

        val prevBtn : Button =findViewById(R.id.prevBtn)
        val lastBtn : Button = findViewById(R.id.lastBtn)
        val updateBtn : Button = findViewById(R.id.updateBtn)
        val deleteBtn : Button = findViewById(R.id.deleteBtn)
        val clearBtn : Button = findViewById(R.id.clearBtn)
        val viewAll : Button = findViewById(R.id.viewAll)
        val searchView : SearchView = findViewById(R.id.searchView)
        val listView : ListView = findViewById(R.id.listView)

        submit.setOnClickListener {
            if (shortcut.text.toString().isEmpty()) {

                shortcut.setError("Please Fill Values")
            }
            else if (getMessage.text.toString().isEmpty()){
                getMessage.setError("Please fill values")
            }
            else{
                val cv = ContentValues()
                cv.put("NAME", shortcut.text.toString())
                cv.put("MEANING", getMessage.text.toString())
                db.insert("ACTABLE", null, cv)
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle("Add Record")
                alertDialog.setMessage("Insert successfully...!")
                alertDialog.setPositiveButton("Ok") { _, _, ->
                    shortcut.text.clear()
                    getMessage.text.clear()
                    getMessage.requestFocus()
                }
                alertDialog.show()
                cursor.requery()
            }
        }

        firstBtn.setOnClickListener {
            if (cursor.moveToFirst()){
                getMessage.setText(cursor.getString(2))
                shortcut.setText(cursor.getString(1))
            }
        }
        nextBtn.setOnClickListener {
            if (cursor.moveToNext()){
                getMessage.setText(cursor.getString(2))
                shortcut.setText(cursor.getString(1))
            }
        }
        prevBtn.setOnClickListener {
            if (cursor.moveToPrevious()){
                getMessage.setText(cursor.getString(2))
                shortcut.setText(cursor.getString(1))
            }
        }
        lastBtn.setOnClickListener {
            if (cursor.moveToLast()){
                getMessage.setText(cursor.getString(2))
                shortcut.setText(cursor.getString(1))
            }
        }
        clearBtn.setOnClickListener {
            getMessage.setText("")
            shortcut.setText("")
            cursor.requery()
        }
        updateBtn.setOnClickListener {
            val cv  = ContentValues()
            cv.put("NAME",shortcut.text.toString())
            cv.put("MEANING",getMessage.text.toString())
            db.update("ACTABLE",cv,"_id = ?", arrayOf(cursor.getString(0)))
            cursor.requery()

            val alertDialog = AlertDialog.Builder(this@MainActivity)
            alertDialog.setTitle("Update Record")
            alertDialog.setMessage("Record Updatad Successfully...!")
            alertDialog.setPositiveButton("Ok"){_,_,->
                if (cursor.moveToFirst()){
                    shortcut.setText(cursor.getString(1))
                    getMessage.setText(cursor.getString(2))
                }
            }
            alertDialog.show()
        }
        deleteBtn.setOnClickListener {
            db.delete("ACTABLE","_id=?", arrayOf(cursor.getString(0)))
            cursor.requery()

            val alertDialog = AlertDialog.Builder(this@MainActivity)
            alertDialog.setTitle("Delete Record")
            alertDialog.setMessage("Record Delete Successfully...!")
            alertDialog.setPositiveButton("Ok"){_,_,->
                if (cursor.moveToFirst()){
                    shortcut.setText(cursor.getString(1))
                    getMessage.setText(cursor.getString(2))
                }
                else{
                    shortcut.setText("No data found")
                    getMessage.setText("No data found")
                }
            }
            alertDialog.show()
        }


         adapter = SimpleCursorAdapter(applicationContext,android.R.layout.simple_expandable_list_item_2,cursor, arrayOf("NAME","MEANING"),
            intArrayOf(android.R.id.text1,android.R.id.text2),0)

        listView.adapter = adapter
        registerForContextMenu(listView)
        viewAll.setOnClickListener {
            searchView.visibility = View.VISIBLE
            listView.visibility=View.VISIBLE
            adapter.notifyDataSetChanged()
            searchView.queryHint = "Search Among ${cursor.count} Record"
        }
        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                 cursor = db.rawQuery("SELECT * FROM ACTABLE WHERE NAME LIKE '%${p0}%' OR MEANING LIKE '%${p0}%' ",null)
                adapter.changeCursor(cursor)
                return false
            }
        })

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(101,11,1,"DELETE")
        menu?.add(102,12,2,"Update")
        menu?.setHeaderTitle("Remove Data")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId==11){
            db.delete("ACTABLE","_id=?", arrayOf(cursor.getString(0)))
            cursor.requery()
            adapter.notifyDataSetChanged()
        }
        else if (item.itemId == 12){
            Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show()
        }
        return super.onContextItemSelected(item)
    }
}