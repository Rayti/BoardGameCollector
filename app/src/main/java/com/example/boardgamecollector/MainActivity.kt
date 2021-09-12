package com.example.boardgamecollector

import android.app.ActionBar
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.boardgamecollector.dao.DbHandler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView: TextView = findViewById(R.id.textView)
        var requestButton: Button = findViewById(R.id.requestButton)
        var addGameButtonMainAct: Button = findViewById(R.id.AddGameButtonMainAct)

        var tv: TableLayout = findViewById(R.id.tableLayoutMainAct)

        tv.removeAllViewsInLayout()

        val dbHandler: DbHandler = DbHandler(this, null, null, 1)
        var gameList = dbHandler.findAllGames()

        for (i in -1..gameList.size){
            var tr: TableRow = TableRow(applicationContext)
            tr.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT)
            if (i == -1) {
                var b3: TextView = TextView(applicationContext)
                b3.text = "id"
                b3.setTextColor(Color.BLUE);
                b3.textSize = 15F;
                tr.addView(b3)
            }

        }


        addGameButtonMainAct.setOnClickListener{
            val intent = Intent(this, AddGameActivity::class.java)
            startActivity(intent)
        }

    }


}//https://www.boardgamegeek.com/xmlapi2/search?query=kawerna&type=boardgame