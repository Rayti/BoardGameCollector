package com.example.boardgamecollector

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.*
import androidx.core.view.children
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseImportGameCollection
import com.example.boardgamecollector.model.Game
import com.example.boardgamecollector.service.BggSearchService

class MainActivity : AppCompatActivity(), AsyncResponseImportGameCollection {

    var tableLayout: TableLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bggSearchService = BggSearchService()
        bggSearchService.importGameCollection("rahdo", this)

        createTable(ArrayList())

        val scrollView = ScrollView(this)
        scrollView.addView(tableLayout)
        setContentView(scrollView)


    }



    private fun createTable(games: ArrayList<Game>) {
        if (tableLayout == null) {
            tableLayout = TableLayout(this)
        }
        cleanTableLayout(tableLayout!!)
        tableLayout!!.addView(createButtonRow())
        tableLayout!!.addView(createHeaderRow())
        for (i in 0 until games.size) {
            tableLayout!!.addView(createGameRow(i + 1, games[i]))
        }
    }

    private fun createButtonRow(): TableRow {
        val addButton = Button(this)
        addButton.text = "Add new game"
        addViewParams(addButton, 2F)

        val sortDateButton = Button(this)
        sortDateButton.text = "Sort by date"
        addViewParams(sortDateButton, 1F)

        val sortRankButton = Button(this)
        addViewParams(sortRankButton, 1F)
        sortRankButton.text = "Sort by rank"

        val row = TableRow(this)
        addViewsToTableRow(row, addButton, sortDateButton, sortRankButton)
        return row
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

        val field2 = TextView(this)
        addViewParams(field2, 2F)
        field2.text = game.image

        val field3 = TextView(this)
        addViewParams(field3, 10F)
        val text = "${game.title} (${game.publishedYear}) \n ${game.description}"
        val spannable = SpannableString(text)
        spannable.setSpan(ForegroundColorSpan(Color.BLUE), 0, "${game.title}".length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE )
        field3.setText(spannable, TextView.BufferType.SPANNABLE)

        val row = TableRow(this)
        addViewsToTableRow(row, field1, field2, field3)
        return row
    }

    private fun cleanTableLayout(layout: TableLayout) {
        layout.children.iterator().forEach { child ->
            val parent = child.parent as ViewManager
            parent.removeView(child)
        }
    }

    private fun addViewParams(view: TextView, weight: Float){
        view.gravity = Gravity.CENTER
        view.width = 0
        view.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, weight)
    }

    private fun addViewsToTableRow(row: TableRow, vararg views: TextView) {
        views.forEach { v -> row.addView(v) }
    }

    override fun processFinish(t: ArrayList<Game>) {
        createTable(t)
    }
}//https://www.boardgamegeek.com/xmlapi2/search?query=kawerna&type=boardgame


