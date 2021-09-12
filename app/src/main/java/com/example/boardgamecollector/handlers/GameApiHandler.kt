package com.example.boardgamecollector.handlers

import android.os.AsyncTask
import com.example.boardgamecollector.GameOLD
import com.example.boardgamecollector.XMLParser
import java.net.HttpURLConnection
import java.net.URL

class GameApiHandler(var game: GameOLD) : AsyncTask<String, GameOLD, GameOLD>() {
    val CONNECTON_TIMEOUT_MILLISECONDS = 60000

    override fun doInBackground(vararg urls: String?): GameOLD? {
        var urlConnection: HttpURLConnection? = null

        try {
            val url = URL(urls[0])

            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connectTimeout = CONNECTON_TIMEOUT_MILLISECONDS
            urlConnection.readTimeout = CONNECTON_TIMEOUT_MILLISECONDS

            var inGame =
                XMLParser.parseToGame(
                    urlConnection.inputStream
                )
            inGame.gameTitle = game.gameTitle

            publishProgress(inGame)
            //Log.d(javaClass.simpleName, inSimpleGame)
            return inGame
        } catch (ex: Exception) {

        } finally {
            urlConnection?.disconnect()
        }

        return null
    }

    override fun onProgressUpdate(vararg values: GameOLD?) {
        game.id = values[0]?.id ?: -1
        game.gameTitle = values[0]?.gameTitle
        game.originalGameTitle = values[0]?.originalGameTitle
        game.rank = values[0]?.rank ?: -1
    }
}