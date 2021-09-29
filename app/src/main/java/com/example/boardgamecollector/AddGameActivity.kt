package com.example.boardgamecollector

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseFindGameById
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseSearchGameByTitle
import com.example.boardgamecollector.dao.DataBase
import com.example.boardgamecollector.model.Game
import com.example.boardgamecollector.service.BggSearchService
import com.example.boardgamecollector.service.GameService

class AddGameActivity : AppCompatActivity(), AsyncResponseSearchGameByTitle{
    var tableLayout: TableLayout? = null
    var searchTitle: EditText? = null
    var bggSearchService: BggSearchService? = null
    var gameService: GameService? = null
    var cachedGames: ArrayList<Game> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bggSearchService = BggSearchService()
        gameService = GameService(DataBase(this))

        val scrollView = ScrollView(this)
        createTable(cachedGames)

        scrollView.addView(tableLayout)
        setContentView(scrollView)
    }

    private fun createTable(games: ArrayList<Game>) {
        Log.d("DEBUG", "CREATING TABLE for ${games.size} games")
        if (tableLayout == null) {
            tableLayout = TableLayout(this)
        }
        tableLayout!!.removeAllViews()
        tableLayout!!.addView(createFirstRow())
        tableLayout!!.addView(createSecondRow())
        tableLayout!!.addView(createHeaderRow())
        val testGame = Game()
        games.forEach { game -> tableLayout!!.addView(createGameRow(game)) }
    }

    private fun createFirstRow(): TableRow {
        searchTitle = EditText(this)
        searchTitle!!.text = Editable.Factory.getInstance().newEditable("")

        addViewParams(searchTitle!!, 1F)

        val tableRow = TableRow(this)
        tableRow.addView(searchTitle)
        return tableRow
    }

    private fun createSecondRow(): TableRow{
        val searchButton = Button(this)
        searchButton.setText("SEARCH")
        addViewParams(searchButton, 1F)
        searchButton.setOnClickListener { v ->
            Log.d("BUTTON", "Clicked SEARCH button (${searchTitle?.text.toString()})")
            Toast.makeText(this, "Searching through BoardGameGeek website", Toast.LENGTH_SHORT).show()
            if (searchTitle?.text.toString().isNotEmpty()) {
                bggSearchService?.searchGamesByTitle(searchTitle?.text.toString(), this)
            }
        }

        val goToMain = Button(this)
        addViewParams(goToMain, 1F)
        goToMain.setText("RETURN")
        goToMain.setOnClickListener { v ->
            Log.d("BUTTON", "Clicked RETURN button")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val tableRow = TableRow(this)
        tableRow.addView(searchButton)
        tableRow.addView(goToMain)
        return tableRow
    }

    private fun createHeaderRow(): TableRow {

        val field1 = TextView(this)
        addViewParams(field1, 1F)
        field1.text = "Title"

        val field2 = TextView(this)
        field2.text = "Year"
        addViewParams(field2, 1F)

        val field3 = TextView(this)
        addViewParams(field3, 1F)
        field3.text = "Action"

        val row = TableRow(this)
        row.addView(field1)
        row.addView(field2)
        row.addView(field3)
        return row
    }

    private fun createGameRow(game: Game): TableRow {
        val title = TextView(this)
        addViewParams(title, 1F)
        title.text = game.title

        val year = TextView(this)
        addViewParams(year, 1F)
        year.text = game.publishedYear.toString()

        val importButton = Button(this)
        addViewParams(importButton, 1F)
        importButton.text = "IMPORT"
        importButton.setOnClickListener { v ->
            Log.d("BUTTON", "Clicked IMPORT button")
            Toast.makeText(this, "Importing game ${game.title}", Toast.LENGTH_SHORT).show()
            if (game.bggId != null) {
                bggSearchService!!.findGameThingById(game.bggId!!, this.AsyncResponseFindGameByIdImpl())
            }
        }

        val row = TableRow(this)
        row.addView(title)
        row.addView(year)
        row.addView(importButton)
        return row
    }

    private fun addViewParams(view: TextView, weight: Float){
        view.gravity = Gravity.CENTER
        view.width = 0
        view.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, weight)
    }

    override fun processFinish(t: ArrayList<Game>) {
        val temporal = searchTitle?.text.toString()
        createTable(t)
        searchTitle?.setText(temporal)
        Toast.makeText(this, "Searching finished", Toast.LENGTH_SHORT).show()
    }

    private fun getContext(): Context {
        return this
    }

    inner class AsyncResponseFindGameByIdImpl: AsyncResponseFindGameById{
        override fun processFinish(t: Game?) {
            if (t != null) {
                gameService?.storeGame(t)
                Toast.makeText(getContext(), "Importing finished", Toast.LENGTH_SHORT).show()
            }
        }
    }
}