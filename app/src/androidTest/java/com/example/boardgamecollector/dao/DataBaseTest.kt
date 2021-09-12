package com.example.boardgamecollector.dao

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.example.boardgamecollector.to.Game
import junit.framework.TestCase
import java.util.*
import java.util.logging.Logger

/**
 * Testing if db works :D
 */
class DataBaseTest : TestCase(){

    private val logger: Logger = Logger.getAnonymousLogger()

    fun testDb() {

        var context = ApplicationProvider.getApplicationContext<Context>()

        val db = DataBase(context)
        //db.addGame(createGame(1L))

        //val game = db.getGame(1L)

        db.addGames(mutableListOf(createGame(5L), createGame(6L), createGame(7L)))
        val games = db.getGames()


        Log.d("tag", games.toString())


    }

    fun createGame(id: Long): Game{
        return Game(id, "title$id", id.toInt(), "designerName$id", "artistName$id",
        "description$id", Date(id), Date(id + 100L), "price$id", "scd$id", "eanUpc$id",
        id.toInt(), "prodCode$id", id.toInt(), "category$id", "comment$id", "imageName$id")
    }
}