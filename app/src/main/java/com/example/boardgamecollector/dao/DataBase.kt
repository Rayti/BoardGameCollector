package com.example.boardgamecollector.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.boardgamecollector.to.Game
import java.util.*
import kotlin.collections.ArrayList

class DataBase(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "BGGDB"

        //GAMES
        val GAMES_TABLE = "games"
        val GAME_ID = "game_id"
        val TITLE = "title"
        val PUBLISHED_YEAR = "published_year"
        val DESIGNER_NAME = "designer_name"
        val ARTIST_NAME = "artist_name"
        val DESCRIPTION = "description"
        val ORDER_DATE = "order_date"
        val ADDED_DATE = "added_date"
        val PRICE = "price"
        val SCD = "scd" // Sugerowana cena detaliczna
        val EAN_UPC = "ean_upc" //EAN/UPC
        val BGG_ID ="bgg_id" //Board Game Geek ID
        val PROD_CODE = "prod_code"
        val RANK = "rank"
        val CATEGORY = "category"
        val COMMENT = "comment"
        val IMAGE_NAME = "image_name"

        //DESIGNERS
        val DESIGNERS_TABLE = "designers"
        val DESIGNER_ID = "designer_id"
        val NAME = "name"

        //ARTISTS
        val ARTISTS_TABLE = "artists"
        val ARTIST_ID = "artist_id"
        // val NAME = "name"

        //RANKING
        val RANKING_TABLE = "ranking"
        val RANKING_ID = "ranking_id"
        val DATE = "date"
        //val RANK = "rank"

        //LOCATIONS
        val LOCATIONS_TABLE = "locations"
        val LOCATION_ID = "location_id"
        // val GAME_ID = "game_id"
        val LOCATION = "location"
        // val comment = "comment"
    }

    /**
     * @return true if successfully added to db, false otherwise
     */
    fun addGame(game: Game): Boolean{
        val db = this.writableDatabase
        val success = db.insert(GAMES_TABLE, null, getGameContentValues(game))
        db.close()
        game.id = success
        return success != -1L
    }

    /**
     * @return true if successfully added to db, false otherwise.
     */
    fun addGames(games: List<Game>): Boolean{
        var result: Boolean = true
        for (game: Game in games) {
            val db = this.writableDatabase
            val success = db.insert(GAMES_TABLE, null, getGameContentValues(game))
            db.close()
            result = success != -1L
            game.id = success
        }
        return result
    }

    /**
     * If game does not have [Game.id] yet return false.
     */
    fun updateGame(game: Game): Boolean{
        val db = this.writableDatabase
        if(game.id == null) return false
        val where = "$GAME_ID = ${game.id}"
        val success = db.update(GAMES_TABLE, getGameContentValues(game), where, null)
        db.close()
        return success == 1
    }

    /**
     * If any game has null [Game.id] return false.
     * @return true if all list members are updated
     */
    fun updateGames(games: List<Game>): Boolean{
        if (games.stream().anyMatch { game -> game.id == null }) return false
        var success = 0
        for (game: Game in games) {
            val db = this.writableDatabase
            val where = "$GAME_ID = ${game.id}"
            success += db.update(GAMES_TABLE, getGameContentValues(game), where, null)
            db.close()
        }
        return success == games.size
    }

    /**
     * @return false if game has null [Game.id] or
     */
    fun deleteGame(game: Game): Boolean{
        val db = this.writableDatabase
        if(game.id == null) return false
        val where = "$GAME_ID = ${game.id}"
        val success = db.delete(GAMES_TABLE, where, null)
        db.close()
        return success == 1
    }

    /**
     * If any game has null [Game.id] return false
     * @return true if all list members are deleted
     */
    fun deleteGames(games: List<Game>): Boolean{
        if (games.stream().anyMatch { game -> game.id == null }) return false
        var success = 0
        for(game: Game in games) {
            val db = this.writableDatabase
            val where = "$GAME_ID = ${game.id}"
            success += db.delete(GAMES_TABLE, where, null)
            db.close()
        }
        return success == games.size
    }

    fun getGame(id: Long): Game?{
        val query = "SELECT * FROM $GAMES_TABLE WHERE $GAME_ID = $id"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            return null
        }
        var game: Game? = null
        if (cursor!!.moveToFirst()) {
            game = parseToGame(cursor)
        }
        cursor.close()
        db.close()
        return game
    }

    /**
     * @return all games available in database.
     */
    fun getGames(): List<Game>{
        val games: ArrayList<Game> = ArrayList()
        val query = "SELECT * FROM $GAMES_TABLE"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        if (cursor!!.moveToFirst()) {
            do {
                games.add(parseToGame(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return games
    }

    private fun parseToGame(cursor: Cursor): Game {
        return Game(
                cursor.getLong(cursor.getColumnIndex(GAME_ID)),
                cursor.getString(cursor.getColumnIndex(TITLE)),
                cursor.getInt(cursor.getColumnIndex(PUBLISHED_YEAR)),
                cursor.getString(cursor.getColumnIndex(DESIGNER_NAME)),
                cursor.getString(cursor.getColumnIndex(ARTIST_NAME)),
                cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
                Date(cursor.getLong(cursor.getColumnIndex(ORDER_DATE))),
                Date(cursor.getLong(cursor.getColumnIndex(ADDED_DATE))),
                cursor.getString(cursor.getColumnIndex(PRICE)),
                cursor.getString(cursor.getColumnIndex(SCD)),
                cursor.getString(cursor.getColumnIndex(EAN_UPC)),
                cursor.getInt(cursor.getColumnIndex(BGG_ID)),
                cursor.getString(cursor.getColumnIndex(PROD_CODE)),
                cursor.getInt(cursor.getColumnIndex(RANK)),
                cursor.getString(cursor.getColumnIndex(CATEGORY)),
                cursor.getString(cursor.getColumnIndex(COMMENT)),
                cursor.getString(cursor.getColumnIndex(IMAGE_NAME))
        )
    }

    /**
     * If [GAME_ID] is null, field with this key is not added to content values.
     */
    private fun getGameContentValues(game: Game): ContentValues{
        val values = ContentValues();
        if (game.id != null) values.put(GAME_ID, game.id)
        values.put(TITLE, game.title)
        values.put(PUBLISHED_YEAR, game.publishedYear)
        values.put(DESIGNER_NAME, game.designerName)
        values.put(ARTIST_NAME, game.artistName)
        values.put(DESCRIPTION, game.description)
        values.put(ORDER_DATE, game.orderDate?.time)
        values.put(ADDED_DATE, game.addedDate?.time)
        values.put(PRICE, game.price)
        values.put(SCD, game.scd)
        values.put(EAN_UPC, game.eanUpc)
        values.put(BGG_ID, game.bggId)
        values.put(PROD_CODE, game.prodCode)
        values.put(RANK, game.rank)
        values.put(CATEGORY, game.category)
        values.put(COMMENT, game.comment)
        values.put(IMAGE_NAME, game.imageName)
        return values
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_GAMES_TABLE = "CREATE TABLE $GAMES_TABLE ( " +
                "$GAME_ID INT PRIMARY KEY, " +
                "$TITLE TEXT, " +
                "$PUBLISHED_YEAR INT, " +
                "$DESIGNER_NAME TEXT, " +
                "$ARTIST_NAME TEXT, " +
                "$DESCRIPTION TEXT, " +
                "$ORDER_DATE INT, " +
                "$ADDED_DATE INT, " +
                "$PRICE TEXT, " +
                "$SCD TEXT, " +
                "$EAN_UPC TEXT, " +
                "$BGG_ID INTEGER, " +
                "$PROD_CODE TEXT," +
                "$RANK INTEGER, " +
                "$CATEGORY TEXT, " +
                "$COMMENT TEXT, " +
                "$IMAGE_NAME TEXT )"

        val CREATE_DESIGNERS_TABLE = "CREATE TABLE $DESIGNERS_TABLE ( " +
                "$DESIGNER_ID INTEGER PRIMARY KEY, " +
                "$NAME TEXT )"

        val CREATE_ARTISTS_TABLE = "CREATE TABLE $ARTISTS_TABLE ( " +
                "$ARTIST_ID INTEGER PRIMARY KEY, " +
                "$NAME TEXT )"

        val CREATE_RANKING_TABLE = "CREATE TABLE $RANKING_TABLE ( " +
                "$RANKING_ID INTEGER PRIMARY KEY, " +
                "$DATE INT, " +
                "$RANK INTEGER )"

        val CREATE_LOCATIONS_TABLE = "CREATE TABLE $LOCATIONS_TABLE ( " +
                "$LOCATION_ID INTEGER PRIMARY KEY, " +
                "$GAME_ID INTEGER, " + //TODO
                "$LOCATION TEXT, " +
                "$COMMENT TEXT )"

        db?.execSQL(CREATE_GAMES_TABLE)
        db?.execSQL(CREATE_DESIGNERS_TABLE)
        db?.execSQL(CREATE_ARTISTS_TABLE)
        db?.execSQL(CREATE_RANKING_TABLE)
        db?.execSQL(CREATE_LOCATIONS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS"
        db!!.execSQL("$DROP_TABLE $GAMES_TABLE")
        db.execSQL("$DROP_TABLE $DESIGNERS_TABLE")
        db.execSQL("$DROP_TABLE $ARTISTS_TABLE")
        db.execSQL("$DROP_TABLE $RANKING_TABLE")
        db.execSQL("$DROP_TABLE $LOCATIONS_TABLE")
        onCreate(db)
    }
}