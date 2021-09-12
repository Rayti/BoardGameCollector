package com.example.boardgamecollector.handlers

import android.os.AsyncTask
import android.widget.TextView
import com.example.boardgamecollector.GameOLD
import com.example.boardgamecollector.XMLParser
import java.net.HttpURLConnection
import java.net.URL

class SimpleGameApiHandler(var simpleGame: GameOLD, var textView: TextView) : AsyncTask<String, GameOLD, GameOLD>() {

    val CONNECTON_TIMEOUT_MILLISECONDS = 60000

    override fun doInBackground(vararg urls: String?): GameOLD? {
        var urlConnection: HttpURLConnection? = null

        try {
            val url = URL(urls[0])

            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connectTimeout = CONNECTON_TIMEOUT_MILLISECONDS
            urlConnection.readTimeout = CONNECTON_TIMEOUT_MILLISECONDS

            var inSimpleGame =
                XMLParser.parseToSimpleGame(
                    urlConnection.inputStream
                )

            publishProgress(inSimpleGame)
            return inSimpleGame
        } catch (ex: Exception) {

        } finally {
            urlConnection?.disconnect()
        }

        return null
    }

    override fun onProgressUpdate(vararg values: GameOLD?) {
        textView.text = values[0]?.gameTitle
        simpleGame.id = values[0]?.id ?: -1
        simpleGame.gameTitle = values[0]?.gameTitle
    }
}