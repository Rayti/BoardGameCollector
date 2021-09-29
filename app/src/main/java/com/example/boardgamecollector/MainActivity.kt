package com.example.boardgamecollector

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.*
import androidx.core.view.children
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseImportGameCollection
import com.example.boardgamecollector.dao.DataBase
import com.example.boardgamecollector.model.Game
import com.example.boardgamecollector.service.GameService

class MainActivity : AppCompatActivity() {

    var tableLayout: TableLayout? = null
    var gameService: GameService? = null
    var cachedGames: ArrayList<Game> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameService = GameService(DataBase(this))
        cachedGames = gameService!!.getStoredGames()
        createTable(cachedGames)
        val scrollView = ScrollView(this)
        scrollView.addView(tableLayout)
        setContentView(scrollView)
    }

    private fun createTable(games: ArrayList<Game>) {
        Log.d("DEBUG", "CREATING TABLE")

        if (tableLayout == null) {
            tableLayout = TableLayout(this)
        }
        cleanTableLayout(tableLayout!!)
        createButtonRows().forEach { row -> tableLayout!!.addView(row) }
        tableLayout!!.addView(createHeaderRow())
        for (i in 0 until games.size) {
            tableLayout!!.addView(createGameRow(i + 1, games[i]))
        }
    }

    private fun createButtonRows(): ArrayList<TableRow> {
        val addButton = Button(this)
        addButton.text = "Add new game"
        addViewParams(addButton, 2F)
        addButton.setOnClickListener { v ->
            Log.d("BUTTON", "Clicked ADD NEW GAME button")
            val  intent = Intent(this, AddGameActivity::class.java)
            startActivity(intent)
        }

        val sortDateButton = Button(this)
        sortDateButton.text = "Sort by date"
        addViewParams(sortDateButton, 1F)
        sortDateButton.setOnClickListener { v ->
            Log.d("BUTTON", "Clicked SORT BY DATE button")
            gameService!!.sortGamesByDate(cachedGames)
            createTable(cachedGames)
            Toast.makeText(this, "Sorted by date", Toast.LENGTH_SHORT).show()
        }

        val sortRankButton = Button(this)
        addViewParams(sortRankButton, 1F)
        sortRankButton.text = "Sort by rank"
        sortRankButton.setOnClickListener { v ->
            Log.d("BUTTON", "Clicked SORT BY RANK button")
            gameService!!.sortGamesByRank(cachedGames)
            createTable(cachedGames)
            Toast.makeText(this, "Sorted by rank", Toast.LENGTH_SHORT).show()
        }


        val row = TableRow(this)
        addViewsToTableRow(row, addButton, sortDateButton, sortRankButton)

        val row2 = TableRow(this)
        val bggImportButton = Button(this)
        bggImportButton.setText("IMPORT USER COLLECTION")
        bggImportButton.setOnClickListener { v ->
            val intent = Intent(this, ImportCollectionActivity::class.java)
            startActivity(intent)
        }
        addViewParams(bggImportButton, 1.0F)
        row2.addView(bggImportButton)
        return arrayListOf(row, row2)
    }

    private fun createHeaderRow(): TableRow {
        val field1 = TextView(this)
        addViewParams(field1, 1F)
        field1.text = "Num"

        val field2 = TextView(this)
        field2.text = "Image"
        addViewParams(field2, 2F)

        val field3 = TextView(this)
        field3.text = "Description"
        addViewParams(field3, 10F)

        val row = TableRow(this)
        addViewsToTableRow(row, field1, field2, field3)
        return row
    }

    fun createGameRow(number:Int, game:Game): TableRow {
        val field1 = TextView(this)
        addViewParams(field1, 1F)
        field1.text = number.toString()

        val delButton = Button(this)
        delButton.text = "DELETE"
        addViewParams(delButton, 0.2F)
        delButton.setOnClickListener {v ->
            Log.d("BUTTON", "Clicked DELETE button")
            val view = v.parent.parent.parent as ViewManager
            view.removeView(v.parent.parent as ViewGroup)
            gameService?.deleteGame(game)
            Toast.makeText(this, "Game ${game.title} deleted from device", Toast.LENGTH_SHORT).show()
        }

        val linearLayout = LinearLayout(this)
        addLinearLayoutParams(linearLayout, 1F)
        linearLayout.addView(field1)
        linearLayout.addView(delButton)

        val field2 = TextView(this)
        addViewParams(field2, 2F)
        field2.maxLines = 2
        field2.text = game.image

        val field3 = TextView(this)
        addViewParams(field3, 10F)
        val text = "${game.title} (${game.publishedYear}) \n ${game.description}"
        val spannable = SpannableString(text)
        spannable.setSpan(ForegroundColorSpan(Color.BLUE), 0, "${game.title}".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE )
        field3.maxLines = 6
        field3.setText(spannable, TextView.BufferType.SPANNABLE)
        field3.setOnClickListener {v ->
            Log.d("DEBUG", "MainActivity sent id = ${game.id}")
            val intent = Intent(this, GameDetailsActivity::class.java).apply {
                putExtra("GAME_MESSAGE", game.id)
            }
            startActivity(intent)
        }

        val row = TableRow(this)
        addViewsToTableRow(row, linearLayout, field2, field3)
        return row
    }

    private fun cleanTableLayout(layout: TableLayout) {
        layout.removeAllViews()
    }

    private fun addViewParams(view: TextView, weight: Float){
        view.gravity = Gravity.CENTER
        view.width = 0
        view.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, weight)
    }

    private fun addLinearLayoutParams(layout: LinearLayout, weight: Float) {
        layout.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, weight)
    }

    private fun addViewsToTableRow(row: TableRow, vararg views: View) {
        views.forEach { v -> row.addView(v) }
    }
}//https://www.boardgamegeek.com/xmlapi2/search?query=kawerna&type=boardgame


