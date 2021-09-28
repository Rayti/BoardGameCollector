package com.example.boardgamecollector.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.boardgamecollector.model.Game
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
        val IMAGE = "image"

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

    fun addGame(game: Game): Int{
        val artists = game.artists
        val designers = game.designers
        val db = this.writableDatabase

        //insert game
        var id = db.insert(GAMES_TABLE, null, getGameContentValues(game)).toInt()
        game.id = id

        addArtists(game.artists, game.id, db)
        addDesigners(game.designers, game.id, db)
        setLocationIdInGameEntity(game.location, db)

        db.close()
        return id
    }

    fun updateGame(game: Game){
        val db = this.writableDatabase
        if(game.id == null) return

        val where = "$GAME_ID = ${game.id}"
        db.update(GAMES_TABLE, getGameContentValues(game), where, null)
        db.delete(DESIGNERS_TABLE, "$GAME_ID = ${game.id}", null)
        db.delete(ARTISTS_TABLE, "$GAME_ID = ${game.id}", null)
        addArtists(game.artists, game.id, db)
        addDesigners(game.designers, game.id, db)
        setLocationIdInGameEntity(game.location, db)
        db.close()
    }

    fun deleteGame(game: Game){
        val db = this.writableDatabase
        if(game.id == null) return

        val where = "$GAME_ID = ${game.id}"
        db.delete(GAMES_TABLE, where, null)
        db.delete(ARTISTS_TABLE, "$GAME_ID = ${game.id}", null)
        db.delete(DESIGNERS_TABLE, "$GAME_ID = ${game.id}", null)
        db.close()
    }

    fun getGame(id: Int): Game?{
        val db = this.readableDatabase
        var gameCursor: Cursor?
        var artistCursor: Cursor? = null
        var designerCursor: Cursor? = null
        var locationCursor: Cursor? = null
        try {
            val gameQuery = "SELECT * FROM $GAMES_TABLE WHERE $GAME_ID = $id"
            gameCursor = db.rawQuery(gameQuery, null)
            System.out.println(gameCursor.count)

            val artistQuery = "SELECT $NAME FROM $ARTISTS_TABLE WHERE $GAME_ID = $id"
            artistCursor = db.rawQuery(artistQuery, null)

            val designerQuery = "SELECT $NAME FROM $DESIGNERS_TABLE WHERE $GAME_ID = $id"
            designerCursor = db.rawQuery(designerQuery, null)

            val locationQuery = "SELECT c.$LOCATION AS $LOCATION FROM $LOCATIONS_TABLE c, $GAMES_TABLE g WHERE g.$LOCATION_ID = c.$LOCATION_ID"
            locationCursor = db.rawQuery(locationQuery, null)

        } catch (e: SQLiteException) {
            return null
        }
        var game: Game? = null
        if (gameCursor!!.moveToFirst()) {
            System.out.println("FOUND GAME")
            game = parseToGame(gameCursor)
            if (locationCursor.moveToFirst()) game.location = locationCursor.getString(locationCursor.getColumnIndex(LOCATION))
            game.artists = parseArtistsOrDesignersQuery(artistCursor)
            game.designers = parseArtistsOrDesignersQuery(designerCursor)
        }
        gameCursor.close()
        artistCursor.close()
        designerCursor.close()
        locationCursor.close()
        db.close()
        return game
    }

    fun getGames(location: String): ArrayList<Game> {
        val db = this.writableDatabase
        val ids = ArrayList<Int>()
        var locationId: Int = -1
        try {
            val cursor = db.rawQuery("SELECT $LOCATION_ID FROM $LOCATIONS_TABLE WHERE $LOCATION = $location", null)
            if (cursor.moveToFirst()) {
                locationId = cursor.getInt(cursor.getColumnIndex(LOCATION_ID))
                val cursorGame = db.rawQuery("SELECT $GAME_ID FROM $GAMES_TABLE WHERE $LOCATION_ID = $locationId", null)
                if (cursorGame.moveToFirst()) {
                    do {
                        ids.add(cursorGame.getInt(cursorGame.getColumnIndex(GAME_ID)))
                    }while (cursor.moveToNext())
                }
            }
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        if (locationId == -1) {
            return ArrayList()
        }
        db.close()

        val games = ArrayList<Game>()
        ids.forEach { id -> getGame(id)?.let { games.add(it) } }
        return games
    }

    fun getGames(): ArrayList<Game> {
        val db = this.writableDatabase
        val ids = ArrayList<Int>()
        try {
            val cursor = db.rawQuery("SELECT $GAME_ID FROM $GAMES_TABLE", null)
            if (cursor.moveToFirst()) {
                do {
                    ids.add(cursor.getInt(cursor.getColumnIndex(GAME_ID)))
                }while(cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        db.close()

        val games = ArrayList<Game>()

        ids.forEach { id ->
            getGame(id)?.let { games.add(it) }
        }
        return  games
    }

    fun countGames(location: String): Int {
        val db = this.readableDatabase

        val locationIdQuery = "SELECT $LOCATION_ID FROM $LOCATIONS_TABLE WHERE $LOCATION = $location"

        try {
            val cursor = db.rawQuery("SELECT count(*) AS cc FROM $GAMES_TABLE WHERE $LOCATION_ID = ($locationIdQuery)", null)
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex("cc"))
            }
            return 0
        } catch (e: SQLiteException) {
            return 0
        }

    }

    fun addLocation(location: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(LOCATION, location)
        db.insert(LOCATIONS_TABLE, null, values)
        db.close()
    }

    fun deleteLocation(location: String) {
        val db = this.writableDatabase
        db.delete(LOCATIONS_TABLE, "$LOCATION = $location", null)
        db.close()
    }

    fun updateLocation(oldLocation: String, newLocation: String) {
        val db = this.writableDatabase
        db.execSQL("UPDATE $LOCATIONS_TABLE SET $LOCATION = $newLocation WHERE $LOCATION = $oldLocation")
        db.close()
    }

    fun getLocations(): ArrayList<String> {
        val db = this.writableDatabase
        val locations = ArrayList<String>()
        try {
            val cursor = db.rawQuery("SELECT $LOCATION FROM $LOCATIONS_TABLE", null)
            if (cursor.moveToFirst()) {
                do {
                    locations.add(cursor.getString(cursor.getColumnIndex(LOCATION)))
                }while (cursor.moveToNext())
            }
        } catch (e: SQLiteException) {
            return ArrayList()
        }
        return locations
    }

    private fun addArtists(artists: ArrayList<String>?, id: Int?,  db: SQLiteDatabase) {
        if(artists == null || id == null) return
        artists.forEach { artist ->
            val values = ContentValues()
            values.put(GAME_ID, id)
            values.put(NAME, artist)
            db.insert(ARTISTS_TABLE, null, values)
        }
    }

    private fun addDesigners(designers: ArrayList<String>?, id: Int?, db: SQLiteDatabase) {
        if(designers == null || id == null) return
        designers.forEach { designer ->
            val values = ContentValues()
            values.put(GAME_ID, id)
            values.put(NAME, designer)
            db.insert(DESIGNERS_TABLE, null, values)
        }
    }

    private fun setLocationIdInGameEntity(location: String?, db: SQLiteDatabase) {
        if (location != null) {
            db.execSQL("UPDATE $GAMES_TABLE SET $LOCATION_ID = (SELECT $LOCATION_ID FROM $LOCATIONS_TABLE WHERE $LOCATION = $location)")
        }
    }

    private fun parseToGame(cursor: Cursor): Game { //TODO handle multiple artists and designers
        return Game(
                cursor.getInt(cursor.getColumnIndex(GAME_ID)),
                cursor.getString(cursor.getColumnIndex(TITLE)),
                cursor.getInt(cursor.getColumnIndex(PUBLISHED_YEAR)),
                null,
                null,
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
                cursor.getString(cursor.getColumnIndex(IMAGE)),
                null
        )
    }

    private fun parseArtistsOrDesignersQuery(cursor: Cursor): ArrayList<String> {
        val list: ArrayList<String> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex(NAME)))
            } while (cursor.moveToNext())
        }
        return list
    }

    /**
     * If [GAME_ID] is null, field with this key is not added to content values.
     */
    private fun getGameContentValues(game: Game): ContentValues{
        val values = ContentValues();
        if (game.id != null) values.put(GAME_ID, game.id)
        values.put(TITLE, game.title)
        values.put(PUBLISHED_YEAR, game.publishedYear)
        //values.put(DESIGNER_NAME, game.designerName)  //TODO handle multiple artists and designers
        //values.put(ARTIST_NAME, game.artistName)
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
        values.put(IMAGE, game.image)
        return values
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_GAMES_TABLE = "CREATE TABLE $GAMES_TABLE( " +
                "$GAME_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$TITLE TEXT, " +
                "$PUBLISHED_YEAR INT, " +
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
                "$IMAGE TEXT, " +
                "$LOCATION_ID INT REFERENCES $LOCATIONS_TABLE($LOCATION_ID))"

        val CREATE_DESIGNERS_TABLE = "CREATE TABLE $DESIGNERS_TABLE( " +
                "$DESIGNER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$GAME_ID INTEGER REFERENCES $GAMES_TABLE($GAME_ID), " +
                "$NAME TEXT );"

        val CREATE_ARTISTS_TABLE = "CREATE TABLE $ARTISTS_TABLE( " +
                "$ARTIST_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$GAME_ID INTEGER REFERENCES $GAMES_TABLE($GAME_ID), " +
                "$NAME TEXT )"

        val CREATE_RANKING_TABLE = "CREATE TABLE $RANKING_TABLE( " +
                "$RANKING_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$GAME_ID INTEGER REFERENCES $GAMES_TABLE($GAME_ID), " +
                "$DATE INT, " +
                "$RANK INTEGER )"

        val CREATE_LOCATIONS_TABLE = "CREATE TABLE $LOCATIONS_TABLE( " +
                "$LOCATION_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$LOCATION TEXT, " +
                "$COMMENT TEXT )"
        db?.execSQL("PRAGMA foreign_keys = 1")
        db?.execSQL(CREATE_LOCATIONS_TABLE)
        db?.execSQL(CREATE_GAMES_TABLE)
        db?.execSQL(CREATE_DESIGNERS_TABLE)
        db?.execSQL(CREATE_ARTISTS_TABLE)
        db?.execSQL(CREATE_RANKING_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS"
        db!!.execSQL("$DROP_TABLE $RANKING_TABLE")
        db.execSQL("$DROP_TABLE $ARTISTS_TABLE")
        db.execSQL("$DROP_TABLE $DESIGNERS_TABLE")
        db.execSQL("$DROP_TABLE $LOCATIONS_TABLE")
        db.execSQL("$DROP_TABLE $GAMES_TABLE")

        onCreate(db)
    }

}