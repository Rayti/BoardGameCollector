package com.example.boardgamecollector

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseImportGameCollection
import com.example.boardgamecollector.dao.DataBase
import com.example.boardgamecollector.model.Game
import com.example.boardgamecollector.service.BggSearchService
import com.example.boardgamecollector.service.GameService

class ImportCollectionActivity: AppCompatActivity(), AsyncResponseImportGameCollection {

    var tableLayout: TableLayout? = null
    var searchUser: EditText? = null
    var bggSearchService: BggSearchService? = null
    var gameService: GameService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bggSearchService = BggSearchService()
        gameService = GameService(DataBase(this))
        createTable()
        setContentView(tableLayout)
    }

    private fun createTable() {
        if (tableLayout == null) {
            tableLayout = TableLayout(this)
        }
        tableLayout!!.addView(createTextInputRow())
        tableLayout!!.addView(createImportButtonRow())
        tableLayout!!.addView(createUpdateRankButtonRow())
        tableLayout!!.addView(createReturnButtonRow())
    }

    private fun createTextInputRow(): TableRow {
        searchUser = EditText(this)
        addViewParams(searchUser!!, 1.0F)
        val row = TableRow(this)
        row.addView(searchUser)
        return row
    }

    private fun createImportButtonRow(): TableRow {
        val importButton = Button(this)
        importButton.text = "IMPORT COLLECTION"
        addViewParams(importButton, 1.0F)
        importButton.setOnClickListener { v ->
            Log.d("BUTTON", "Clicked IMPORT COLLECTION button")
            if (searchUser?.text.toString().isNotEmpty()) {
                Toast.makeText(this, "Importing games from user ${searchUser!!.text.toString()}", Toast.LENGTH_SHORT)
                bggSearchService!!.importGameCollection(searchUser!!.text.toString(), this)
            }
        }
        val row = TableRow(this)
        row.addView(importButton)
        return row
    }

    private fun createUpdateRankButtonRow(): TableRow {
        val updateButton = Button(this)
        updateButton.text = "UPDATE RANKS"
        addViewParams(updateButton, 1.0F)
        updateButton.setOnClickListener { v ->
            Log.d("BUTTON", "Clicked UPDATE RANKS button")


            //TODO
        }
        val row = TableRow(this)
        row.addView(updateButton)
        return row
    }

    private fun createReturnButtonRow(): TableRow {
        val goBackButton = Button(this)
        goBackButton.text = "RETURN"
        addViewParams(goBackButton, 1.0F)
        goBackButton.setOnClickListener { v ->
            Log.d("BUTTON", "Clicked RETURN button")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val row = TableRow(this)
        row.addView(goBackButton)
        return row
    }

    private fun addViewParams(view: TextView, weight: Float){
        view.gravity = Gravity.CENTER
        view.width = 0
        view.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, weight)
    }

    override fun processFinish(t: ArrayList<Game>) {
        gameService!!.deleteAllGames()
        t.forEach { game -> gameService!!.storeGame(game) }
        Toast.makeText(this, "Games import finished", Toast.LENGTH_SHORT)

    }
}