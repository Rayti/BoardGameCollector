package com.example.boardgamecollector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.boardgamecollector.dao.DbHandler
import com.example.boardgamecollector.handlers.GameApiHandler
import com.example.boardgamecollector.handlers.SimpleGameApiHandler

class AddGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game)

        val searchTitle: TextView = findViewById(R.id.SearchTitleAddGameAct)
        val searchButton: Button = findViewById(R.id.SearchButtonAddGameAct)
        var searchResult: TextView = findViewById(R.id.SearchResultAddGameAct)
        var saveButton: Button = findViewById(R.id.SaveButtonAddGameAct)
        var goBackButton: Button = findViewById(R.id.GoBackButtonAddGameAct)

        var game: GameOLD = GameOLD()
        searchButton.setOnClickListener{
            SimpleGameApiHandler(game, searchResult).execute("https://www.boardgamegeek.com/xmlapi2/search?query=${searchTitle.text}&type=boardgame")
        }

        saveButton.setOnClickListener{
            GameApiHandler(game).execute("https://www.boardgamegeek.com/xmlapi2/thing?id=${game.id}&stats=1")
            val dbHandler = DbHandler(this, null, null, 1)
            dbHandler.addGame(game)
            Toast.makeText(this@AddGameActivity, "Gra dodana do bazy", Toast.LENGTH_SHORT).show()
        }//https://www.boardgamegeek.com/xmlapi2/thing?id=102794&stats=1

        goBackButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}