package com.example.boardgamecollector.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.boardgamecollector.GameOLD

class DbHandler(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?,
                version: Int) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "gameDB.db"
        val TABLE_GAMES = "games"
        val COLUMN_ID = "_id"
        val COLUMN_GAME_TITLE = "game_title"
        val COLUMN_ORIGINAL_GAME_TITLE = "original_game_title"
        val COLUMN_RELEASE_DATE = "release_date"
        val COLUMN_DESCRIPTION = "description"
        val COLUMN_ORDER_DATE = "order_date"
        val COLUMN_COLLECTION_ADDED_DATE = "collection_added_date"
        val COLUMN_PRICE = "price"
        val COLUMN_SUGGESTED_RETAIL_PRICE = "suggested_retail_price"
        val COLUMN_CODE_EAN = "code_ean"
        val COLUMN_ID_BGG = "id_bgg"
        val COLUMN_PRODUCTION_CODE = "production_code"
        val COLUMN_RANK = "rank"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_GAMES_TABLE = ("CREATE TABLE " + TABLE_GAMES + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_GAME_TITLE + " TEXT, " +
                COLUMN_ORIGINAL_GAME_TITLE + " TEXT, " +
                COLUMN_RELEASE_DATE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_ORDER_DATE + " TEXT, " +
                COLUMN_COLLECTION_ADDED_DATE + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_SUGGESTED_RETAIL_PRICE + " TEXT, " +
                COLUMN_CODE_EAN + " TEXT, " +
                COLUMN_ID_BGG + " TEXT, " +
                COLUMN_PRODUCTION_CODE + " TEXT, " +
                COLUMN_RANK + " INTEGER )")
        db?.execSQL(CREATE_GAMES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        onCreate(db)
    }

    fun findAllGames(): List<GameOLD> {
        val query = "SELECT * FROM $TABLE_GAMES"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var gameList: MutableList<GameOLD> = mutableListOf()


        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            val gameTitle = cursor.getString(1)
            gameList.add(GameOLD(id, gameTitle, null, null, null,
                    null, null, null, null,
                    null, null, null, 0))
            while (cursor.moveToNext()) {
                val id = Integer.parseInt(cursor.getString(0))
                val gameTitle = cursor.getString(1)
                gameList.add(GameOLD(id, gameTitle, null, null, null,
                        null, null, null, null,
                        null, null, null, 0))
            }
        }
        return gameList
    }

    fun findGame(gameTitle: String): GameOLD? {
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_GAME_TITLE LIKE \"$gameTitle\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var game: GameOLD? = null

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            val gameTitle = cursor.getString(1)
            game = GameOLD(id, gameTitle, null, null, null,
                    null, null, null, null,
                    null, null, null, 0)
        }
        db.close()
        return game
    }

    fun deleteGame(gameTitle: String): Boolean {
        var result = false
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_GAME_TITLE LIKE \"$gameTitle\""

        val db = this.writableDatabase;
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(0)
            db.delete(TABLE_GAMES, COLUMN_ID + " = ?", arrayOf(id.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

    fun addGame(game: GameOLD) {
        val values = ContentValues()
        values.put(COLUMN_GAME_TITLE, game.gameTitle)

        val db = this.writableDatabase
        db.insert(TABLE_GAMES, null, values)
        db.close()
    }
}