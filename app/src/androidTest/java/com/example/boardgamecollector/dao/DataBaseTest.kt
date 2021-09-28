package com.example.boardgamecollector.dao

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.boardgamecollector.model.Game
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

        val createdGame = createGame(1L)

        createdGame.artists = arrayListOf("artist1", "artist2")
        createdGame.designers = arrayListOf("designer1", "designer2")

        val id = db.addGame(createdGame)

        //val game = db.getGame(1L)

        //db.addGames(mutableListOf(createGame(22L), createGame(23L), createGame(24L)))
        //val game: Game? = db.getGame(25L)

        //db.addGame(createGame(5))

        val game = db.getGame(id)

        val createdGame2 = createGame(2L)
        createdGame2.id = game?.id
        createdGame2.artists = arrayListOf("artist1x", "artist2x")
        createdGame2.designers = arrayListOf("designer1x", "designer2x", "designer3x")
        db.updateGame(createdGame2)

        val game2 = db.getGame(id)

        val games = db.getGames()

        System.out.println(games)
        System.out.println(game)
        System.out.println(game2)

        //games.forEach { game -> System.out.println(game) }


    }

    fun createGame(id: Long): Game{
        return Game(null, "title$id", id.toInt(), null/*"designerName$id"*/, null /*"artistName$id" */, //TODO
        "description$id", Date(id), Date(id + 100L), "price$id", "scd$id", "eanUpc$id",
        id.toInt(), "prodCode$id", id.toInt(), "category$id", "comment$id", "imageName$id", null)
    }
}