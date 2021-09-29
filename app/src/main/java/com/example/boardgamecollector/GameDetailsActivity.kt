package com.example.boardgamecollector

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.boardgamecollector.dao.DataBase
import com.example.boardgamecollector.model.Game
import com.example.boardgamecollector.service.GameService

class GameDetailsActivity: AppCompatActivity() {

    var tableLayout: TableLayout? = null
    var editableFields: HashMap<String, Pair<TextView, EditText>>? = null
    var gameService: GameService? = null
    var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getIntExtra("GAME_MESSAGE", -1)
        Log.d("DEBUG", "GamesDetailsActivity received id = $id")
        gameService = GameService(DataBase(this))
        game = gameService!!.getStoredGame(id.toInt())
        createTable(game!!)

        val scrollView = ScrollView(this)
        scrollView.addView(tableLayout)
        setContentView(scrollView)
    }

    private fun createTable(game: Game) {
        Log.d("DEBUG", "CREATING TABLE")
        if (tableLayout == null) {
            tableLayout = TableLayout(this)
        }
        tableLayout!!.removeAllViews()
        createEditTextsAndViewTexts(game)
        createRows().forEach { row -> tableLayout!!.addView(row) }

    }

    private fun createRows(): ArrayList<TableRow> {
        val rows = ArrayList<TableRow>()
        editableFields?.forEach { entry ->
            val row = TableRow(this)
            row.addView(entry.value.first)
            row.addView(entry.value.second)
            rows.add(row)
        }
        rows.add(createSaveRowLayout())
        return rows
    }

    private fun createSaveRowLayout(): TableRow {
        val saveRow = TableRow(this)
        val saveButton = Button(this)
        addViewParams(saveButton, 1.0F)
        saveButton.setText("SAVE")
        saveButton.setOnClickListener { v ->
            editableFields!!.forEach { entry ->
                Log.d("BUTTON", "Clicked SAVE button")
                game!!.setFieldByString(entry.key, entry.value.second.text.toString())
            }
            gameService!!.editStoredGame(game!!)
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        }

        val returnButton = Button(this)
        returnButton.setText("RETURN")
        addViewParams(returnButton, 1.0F)
        returnButton.setOnClickListener { v ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        saveRow.addView(returnButton)

        saveRow.addView(saveButton)
        return saveRow
    }

    private fun createEditTextsAndViewTexts(game: Game){
        val map: HashMap<String, Pair<TextView, EditText>> = HashMap()
        val names = arrayListOf("title", "published year", "description", "price", "scd", "production code", "comment", "rank")
        names.forEach { name -> map[name] = Pair(createTextView(name), createEditText(game.getFieldByString(name))) }
        editableFields = map
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(this)
        addViewParams(textView, 1.0F)
        textView.text = text
        return textView
    }

    private fun createEditText(text: String?): EditText {
        val editText = EditText(this)
        addViewParams(editText, 1.0F)
        editText.setText(text)
        return editText
    }

    private fun addViewParams(view: TextView, weight: Float){
        view.gravity = Gravity.CENTER
        view.width = 0
        view.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, weight)
    }


}